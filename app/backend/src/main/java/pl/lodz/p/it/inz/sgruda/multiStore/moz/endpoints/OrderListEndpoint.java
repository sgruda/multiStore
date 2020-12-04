package pl.lodz.p.it.inz.sgruda.multiStore.moz.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.OrderListService;
import pl.lodz.p.it.inz.sgruda.multiStore.security.CurrentUser;
import pl.lodz.p.it.inz.sgruda.multiStore.security.UserPrincipal;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz.SignMozDTOUtil;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log
@Validated
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/orders")
public class OrderListEndpoint {
    private OrderListService orderListService;
    private SignMozDTOUtil signMozDTOUtil;

    @Autowired
    public OrderListEndpoint(OrderListService orderListService, SignMozDTOUtil signMozDTOUtil) {
        this.orderListService = orderListService;
        this.signMozDTOUtil = signMozDTOUtil;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Map<String, Object>> getAllOrdersPage(@Valid @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "5") int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<OrderEntity> pageOrderEntities = orderListService.getOrderListPage(paging);

        Map<String, Object> response = this.prepareResponseFromEntities(pageOrderEntities);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Map<String, Object>> getClientOrdersPage(@Valid @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") int size,
                                                                   @CurrentUser UserPrincipal userPrincipal) {
        Pageable paging = PageRequest.of(page, size);
        Page<OrderEntity> pageOrderEntities = orderListService.getOrderListPageForEmail(paging, userPrincipal.getEmail());

        Map<String, Object> response = this.prepareResponseFromEntities(pageOrderEntities);

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> prepareResponseFromEntities(Page<OrderEntity> pageOrderEntities) {
        OrderMapper orderMapper = new OrderMapper();
        List<OrderDTO> orderDTOS = pageOrderEntities.getContent()
                .stream()
                .map(entity -> orderMapper.toDTO(entity))
                .collect(Collectors.toList());
        orderDTOS.forEach(dto -> signMozDTOUtil.signOrderDTO(dto));

        Map<String, Object> response = new HashMap<>();
        response.put("orders", orderDTOS);
        response.put("currentPage", pageOrderEntities.getNumber());
        response.put("totalItems", pageOrderEntities.getTotalElements());
        response.put("totalPages", pageOrderEntities.getTotalPages());

        return response;
    }
}
