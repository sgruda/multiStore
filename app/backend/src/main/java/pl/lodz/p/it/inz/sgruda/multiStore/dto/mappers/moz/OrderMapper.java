package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;

import java.util.stream.Collectors;

public class OrderMapper implements Mapper<OrderEntity, OrderDTO> {
    private OrderedItemMapper orderedItemMapper;

    public OrderMapper() {
        this.orderedItemMapper = new OrderedItemMapper();
    }

    @Override
    public OrderDTO toDTO(OrderEntity entity) {
        OrderDTO dto = new OrderDTO();

        dto.setId(entity.getId());
        dto.setIdentifier(entity.getIdentifier());
        dto.setOrderDate(entity.getOrderDate());
        dto.setBuyerEmail(entity.getAccountEntity().getEmail());
        dto.setOrderedItemDTOS(
                entity.getOrderedItemEntities().stream()
                        .map(entityItem -> orderedItemMapper.toDTO(entityItem))
                        .collect(Collectors.toSet())
        );
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setStatus(entity.getStatusEntity().getStatusName().name());
        dto.setAddress(entity.getAddress());
        dto.setVersion(entity.getVersion());

        return dto;
    }

    @Override
    public OrderEntity updateEntity(OrderEntity entity, OrderDTO dto) {
        return entity;
    }
}
