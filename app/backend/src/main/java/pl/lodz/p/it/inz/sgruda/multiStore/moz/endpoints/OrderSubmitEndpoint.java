package pl.lodz.p.it.inz.sgruda.multiStore.moz.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
import java.util.HashSet;
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
    public ResponseEntity<?> submitOrder(@Valid @RequestBody BasketDTO basketDTO, @CurrentUser UserPrincipal currentUser) {
        if(!basketDTO.getOwnerEmail().equals(currentUser.getEmail())) {
            log.severe("Error: UnauthorizedRequest. Buyer email in BasketDTO doesn't equals to current authenticated user.");
            throw new UnauthorizedRequestException();
        }
        try {
            checkerMozDTO.checkBasketDTOSignature(basketDTO);
            BasketEntity basketEntity = orderSubmitService.getBasketEntity(basketDTO.getOwnerEmail());
            checkerMozDTO.checkBasketDTOVersion(basketEntity, basketDTO);
            Set<OrderedItemEntity> orderedItemEntitySet = new HashSet<>();
            for(OrderedItemDTO itemDTO : basketDTO.getOrderedItemDTOS()) {
                orderedItemEntitySet.add(orderSubmitService.getOrderedItemsEntityByIdentifier(itemDTO.getIdentifier()));
            }
            basketEntity.setOrderedItemEntities(orderedItemEntitySet);
            orderSubmitService.createOrder(basketEntity);
        } catch(AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "order.correctly.submitted"));
    }
}
