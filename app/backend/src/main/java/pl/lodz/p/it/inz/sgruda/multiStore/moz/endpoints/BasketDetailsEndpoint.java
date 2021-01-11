package pl.lodz.p.it.inz.sgruda.multiStore.moz.endpoints;

import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.moz.BasketMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.BasketDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderedItemDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.moz.UnauthorizedAttemptOfAccessToBasketException;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.BasketDetailsService;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.BasketHandlerService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.security.CurrentUser;
import pl.lodz.p.it.inz.sgruda.multiStore.security.UserPrincipal;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.CheckerSimpleDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz.CheckerMozDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz.SignMozDTOUtil;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;


@Log
@Validated
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/basket")
public class BasketDetailsEndpoint {
    private BasketDetailsService basketDetailsService;
    private BasketHandlerService basketHandlerService;
    private SignMozDTOUtil signMozDTOUtil;
    private CheckerMozDTO checkerMozDTO;
    private CheckerSimpleDTO checkerSimpleDTO;

    @Autowired
    public BasketDetailsEndpoint(BasketDetailsService basketDetailsService, BasketHandlerService basketHandlerService,
                                 SignMozDTOUtil signMozDTOUtil, CheckerMozDTO checkerMozDTO, CheckerSimpleDTO checkerSimpleDTO) {
        this.basketDetailsService = basketDetailsService;
        this.basketHandlerService = basketHandlerService;
        this.signMozDTOUtil = signMozDTOUtil;
        this.checkerMozDTO = checkerMozDTO;
        this.checkerSimpleDTO = checkerSimpleDTO;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> getOwnBasket(@CurrentUser UserPrincipal currentUser) {
        BasketEntity basketEntity;
        try {
            basketEntity = basketDetailsService.getBasketEntityByOwnerEmail(currentUser.getEmail());
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        BasketMapper basketMapper = new BasketMapper();
        BasketDTO basketDTO = basketMapper.toDTO(basketEntity);
        signMozDTOUtil.signBasketDTO(basketDTO);
        return ResponseEntity.ok(basketDTO);
    }

    @GetMapping("/size")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> getOwnBasketSize(@CurrentUser UserPrincipal currentUser) {
        int size;
        try {
            size = basketDetailsService.getBasketSizeByOwnerEmail(currentUser.getEmail());
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("{\"size\": " + size + "}");
    }

    @PutMapping("/item/edit")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> editItemInBasket(@Valid @RequestBody OrderedItemDTO itemDTO, @CurrentUser UserPrincipal currentUser) {
        BasketEntity basketEntity;
        OrderedItemEntity orderedItemEntity;
        try {
            checkerMozDTO.checkOrderedItemDTOSignature(itemDTO);
            basketEntity = basketHandlerService.getBasketEntityByOwnerEmail(currentUser.getEmail());
            orderedItemEntity = basketHandlerService.getOrderedItemEntity(itemDTO.getIdentifier());
            orderedItemEntity.setOrderedNumber(itemDTO.getOrderedNumber());
            checkerMozDTO.checkOrderedItemDTOVersion(orderedItemEntity, itemDTO);
            basketHandlerService.editOrderedItemInBasket(orderedItemEntity, basketEntity);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "ordered.item.correctly.edited"));
    }

    @PutMapping("/add")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> addToBasket(@Valid @RequestBody AddToBasketRequest request, @CurrentUser UserPrincipal currentUser) {
        BasketEntity basketEntity;
        ProductEntity productEntity;
        try {
            if(!request.getBasketDTO().getOwnerEmail().equals(currentUser.getEmail()))
                throw new UnauthorizedAttemptOfAccessToBasketException();
            checkerMozDTO.checkBasketDTOSignature(request.getBasketDTO());
            checkerSimpleDTO.checkSignature(request.getOrderedItemDTO().getOrderedProduct());
            productEntity = basketHandlerService.getProductEntityByTitle(request.getOrderedItemDTO().getOrderedProduct().getTitle());
            checkerSimpleDTO.checkVersion(productEntity, request.getOrderedItemDTO().getOrderedProduct());
            basketEntity = basketHandlerService.getBasketEntityByOwnerEmail(currentUser.getEmail());
            checkerMozDTO.checkBasketDTOVersion(basketEntity, request.getBasketDTO());
            Set<OrderedItemEntity> orderedItemEntitySet = new HashSet<>();
            for(OrderedItemDTO itemDTO : request.getBasketDTO().getOrderedItemDTOS()) {
                orderedItemEntitySet.add(basketHandlerService.getOrderedItemEntityOrCreateNew(
                        itemDTO.getIdentifier(), itemDTO.getOrderedNumber(), itemDTO.getOrderedProduct().getTitle(), null)
                );
            }
            orderedItemEntitySet.add(basketHandlerService.getOrderedItemEntityOrCreateNew(
                    request.getOrderedItemDTO().getIdentifier(),
                    request.getOrderedItemDTO().getOrderedNumber(),
                    request.getOrderedItemDTO().getOrderedProduct().getTitle(),
                    productEntity)
            );
            basketEntity.setOrderedItemEntities(orderedItemEntitySet);
            basketHandlerService.addToBasket(orderedItemEntitySet, basketEntity);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "product.correctly.added.to.basket"));
    }

    @PutMapping("/remove")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> removeFromBasket(@Valid @RequestBody BasketDTO basketDTO, @CurrentUser UserPrincipal currentUser) {
        BasketEntity basketEntity;
        try {
            if(!basketDTO.getOwnerEmail().equals(currentUser.getEmail()))
                throw new UnauthorizedAttemptOfAccessToBasketException();
            checkerMozDTO.checkBasketDTOSignature(basketDTO);
            basketEntity = basketHandlerService.getBasketEntityByOwnerEmail(currentUser.getEmail());
            checkerMozDTO.checkBasketDTOVersion(basketEntity, basketDTO);
            Set<OrderedItemEntity> orderedItemEntitySet = new HashSet<>();
            for(OrderedItemDTO itemDTO : basketDTO.getOrderedItemDTOS()) {
                orderedItemEntitySet.add(basketHandlerService.getOrderedItemEntityOrCreateNew(
                        itemDTO.getIdentifier(), itemDTO.getOrderedNumber(), itemDTO.getOrderedProduct().getTitle(), null)
                );
            }
            basketEntity.setOrderedItemEntities(orderedItemEntitySet);
            basketHandlerService.removeFromBasket(orderedItemEntitySet, basketEntity);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "product.correctly.removed.from.basket"));
    }

    @Getter
    private static class AddToBasketRequest {
        @Valid
        private BasketDTO basketDTO;

        @Valid
        private OrderedItemDTO orderedItemDTO;
    }
}
