package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mop.ProductMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderedItemDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;

public class OrderedItemMapper implements Mapper<OrderedItemEntity, OrderedItemDTO> {
    private HashMethod hashMethod;
    private ProductMapper productMapper;

    public OrderedItemMapper() {
        this.hashMethod = new HashMethod();
        this.productMapper = new ProductMapper();
    }

    @Override
    public OrderedItemDTO toDTO(OrderedItemEntity entity) {
        OrderedItemDTO dto = new OrderedItemDTO();

        dto.setIdHash(hashMethod.hash(entity.getId()));
        dto.setIdentifier(entity.getIdentifier());
        dto.setIdentifier(entity.getIdentifier());
        dto.setOrderedNumber(entity.getOrderedNumber());
        dto.setOrderedProduct(productMapper.toDTO(entity.getProductEntity()));
        dto.setVersion(entity.getVersion());

        return dto;
    }

    @Override
    public OrderedItemEntity updateEntity(OrderedItemEntity entity, OrderedItemDTO dto) {
        entity.setOrderedNumber(dto.getOrderedNumber());
        return entity;
    }
}
