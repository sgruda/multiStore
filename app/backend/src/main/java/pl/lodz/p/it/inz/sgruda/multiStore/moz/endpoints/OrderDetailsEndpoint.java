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
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz.CheckerMozDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz.SignMozDTOUtil;

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
    private SignMozDTOUtil signMozDTOUtil;
    private CheckerMozDTO checkerMozDTO;


    @Autowired
    public OrderDetailsEndpoint(OrderDetailsService orderDetailsService, OrderChangeStatusService orderChangeStatusService,
                                SignMozDTOUtil signMozDTOUtil, CheckerMozDTO checkerMozDTO) {
        this.orderDetailsService = orderDetailsService;
        this.orderChangeStatusService = orderChangeStatusService;
        this.signMozDTOUtil = signMozDTOUtil;
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
        signMozDTOUtil.signOrderDTO(orderDTO);
        return ResponseEntity.ok(orderDTO);
    }

    @PutMapping("/change-status")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> changeOrderStatus(@Valid @RequestBody OrderDTO orderDTO) {
        OrderEntity orderEntity;
        try {
            checkerMozDTO.checkOrderDTOSignature(orderDTO);
            orderEntity = orderChangeStatusService.getOrderByIdentifier(orderDTO.getIdentifier());
            checkerMozDTO.checkOrderDTOVersion(orderEntity, orderDTO);
            orderChangeStatusService.changeStatus(orderEntity);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "order.status.correctly.changed"));
    }
}
