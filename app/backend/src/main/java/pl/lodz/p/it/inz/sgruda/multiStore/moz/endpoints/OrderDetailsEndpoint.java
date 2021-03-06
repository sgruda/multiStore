package pl.lodz.p.it.inz.sgruda.multiStore.moz.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.moz.OrderMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.OrderChangeStatusService;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.OrderDetailsService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.security.CurrentUser;
import pl.lodz.p.it.inz.sgruda.multiStore.security.UserPrincipal;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz.CheckerMozDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz.HashMozDTOUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Log
@Validated
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/order")
public class OrderDetailsEndpoint {
    private OrderDetailsService orderDetailsService;
    private OrderChangeStatusService orderChangeStatusService;
    private HashMozDTOUtil hashMozDTOUtil;
    private CheckerMozDTO checkerMozDTO;


    @Autowired
    public OrderDetailsEndpoint(OrderDetailsService orderDetailsService, OrderChangeStatusService orderChangeStatusService,
                                HashMozDTOUtil hashMozDTOUtil, CheckerMozDTO checkerMozDTO) {
        this.orderDetailsService = orderDetailsService;
        this.orderChangeStatusService = orderChangeStatusService;
        this.hashMozDTOUtil = hashMozDTOUtil;
        this.checkerMozDTO = checkerMozDTO;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> getOrder(@Valid @NotNull(message = "validation.notnull")
                                          @Size(min = 36, max = 36, message = "validation.size")
                                          @Pattern(regexp = "[0-9A-Za-z-]+", message = "validation.pattern")
                                          @RequestParam(value = "id") String identifier) {
        OrderEntity orderEntity;
        try {
            orderEntity = orderDetailsService.getOrderByIdentifier(identifier);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        OrderMapper orderMapper = new OrderMapper();
        OrderDTO orderDTO = orderMapper.toDTO(orderEntity);
        hashMozDTOUtil.hashOrderDTO(orderDTO);
        return ResponseEntity.ok(orderDTO);
    }

    @PutMapping("/change-status")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> changeOrderStatus(@Valid @RequestBody OrderDTO orderDTO, @CurrentUser UserPrincipal userPrincipal) {
        OrderEntity orderEntity;
        try {
            checkerMozDTO.checkOrderDTOHash(orderDTO);
            orderEntity = orderChangeStatusService.getOrderByIdentifier(orderDTO.getIdentifier());
            OrderMapper orderMapper = new OrderMapper();
            OrderEntity entityCopy = orderMapper.createCopyOf(orderEntity, orderDTO);

            orderChangeStatusService.changeStatus(entityCopy, userPrincipal.getId());
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "order.status.correctly.changed"));
    }
}
