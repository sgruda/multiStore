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
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.BasketDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderedItemDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.http.UnauthorizedRequestException;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.OrderSubmitService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.security.CurrentUser;
import pl.lodz.p.it.inz.sgruda.multiStore.security.UserPrincipal;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz.CheckerMozDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log
@Validated
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/order")
public class OrderSubmitEndpoint {
    private OrderSubmitService orderSubmitService;
    private CheckerMozDTO checkerMozDTO;

    @Autowired
    public OrderSubmitEndpoint(OrderSubmitService orderSubmitService, CheckerMozDTO checkerMozDTO) {
        this.orderSubmitService = orderSubmitService;
        this.checkerMozDTO = checkerMozDTO;
    }

    @PostMapping("/submit")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> submitOrder(@Valid @RequestBody OrderRequest orderRequest, @CurrentUser UserPrincipal currentUser) {
        if(!orderRequest.getBasketDTO().getOwnerEmail().equals(currentUser.getEmail())) {
            log.severe("Error: UnauthorizedRequest. Buyer email in BasketDTO doesn't equals to current authenticated user.");
            throw new UnauthorizedRequestException();
        }
        try {
            checkerMozDTO.checkBasketDTOSignature(orderRequest.getBasketDTO());
            BasketEntity basketEntity = orderSubmitService.getBasketEntity(orderRequest.getBasketDTO().getOwnerEmail());
            checkerMozDTO.checkBasketDTOVersion(basketEntity, orderRequest.getBasketDTO());
            List<OrderedItemEntity> orderedItemEntityList = new ArrayList<>();
            for(OrderedItemDTO itemDTO : orderRequest.getBasketDTO().getOrderedItemDTOS()) {
                orderedItemEntityList.add(orderSubmitService.getOrderedItemsEntityByIdentifier(itemDTO.getIdentifier()));
            }
            basketEntity.setOrderedItemEntities(orderedItemEntityList);
            orderSubmitService.createOrder(basketEntity, orderRequest.getAddress());
        } catch(AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "order.correctly.submitted"));
    }

    @PostMapping("/total-price")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> getTotalPrice(@Valid @RequestBody BasketDTO basketDTO, @CurrentUser UserPrincipal currentUser) {
        double totalPrice = -1;
        if(!basketDTO.getOwnerEmail().equals(currentUser.getEmail())) {
            log.severe("Error: UnauthorizedRequest. Buyer email in BasketDTO doesn't equals to current authenticated user.");
            throw new UnauthorizedRequestException();
        }
        try {
            checkerMozDTO.checkBasketDTOSignature(basketDTO);
            BasketEntity basketEntity = orderSubmitService.getBasketEntity(basketDTO.getOwnerEmail());
            checkerMozDTO.checkBasketDTOVersion(basketEntity, basketDTO);
            List<OrderedItemEntity> orderedItemEntityList = new ArrayList<>();
            for(OrderedItemDTO itemDTO : basketDTO.getOrderedItemDTOS()) {
                orderedItemEntityList.add(orderSubmitService.getOrderedItemsEntityByIdentifier(itemDTO.getIdentifier()));
            }
            totalPrice = orderSubmitService.calcPrice(orderedItemEntityList);
        } catch(AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("{\"totalPrice\": " + totalPrice + "}");
    }

    @Getter
    private static class OrderRequest {
        @Valid
        private BasketDTO basketDTO;

        @NotNull(message = "validation.notnull")
        @Size(max = 64, message = "validation.size")
        @Pattern(regexp = "[0-9A-Za-z-/]+", message = "validation.pattern")
        private String address;
    }
}
