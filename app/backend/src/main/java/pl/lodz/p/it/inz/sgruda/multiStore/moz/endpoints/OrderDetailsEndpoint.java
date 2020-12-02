package pl.lodz.p.it.inz.sgruda.multiStore.moz.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.moz.OrderMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.OrderDetailsService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
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
    private SignMozDTOUtil signMozDTOUtil;

    @Autowired
    public OrderDetailsEndpoint(OrderDetailsService orderDetailsService, SignMozDTOUtil signMozDTOUtil) {
        this.orderDetailsService = orderDetailsService;
        this.signMozDTOUtil = signMozDTOUtil;
    }

    @GetMapping
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
}
