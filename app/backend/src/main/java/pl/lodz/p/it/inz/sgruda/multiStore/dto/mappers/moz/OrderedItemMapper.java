package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mop.ProductMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderedItemDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;

public class OrderedItemMapper implements Mapper<OrderedItemEntity, OrderedItemDTO> {
    private ProductMapper productMapper;

    public OrderedItemMapper() {
        this.productMapper = new ProductMapper();
    }

    @Override
    public OrderedItemDTO toDTO(OrderedItemEntity entity) {
        OrderedItemDTO dto = new OrderedItemDTO();

        dto.setId(entity.getId());
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

        entity.setVersion(dto.getVersion());
        return entity;
    }

    @Override
    public OrderedItemEntity createCopyOf(OrderedItemEntity entity, OrderedItemDTO dto) {
        OrderedItemEntity entityCopy = new OrderedItemEntity();
        entityCopy.setId(dto.getId());
        entityCopy.setIdentifier(entity.getIdentifier());
        entityCopy.setOrderedNumber(entity.getOrderedNumber());
        entityCopy.setProductEntity(entity.getProductEntity());
        entityCopy.setVersion(dto.getVersion());
        return entityCopy;
    }
}
