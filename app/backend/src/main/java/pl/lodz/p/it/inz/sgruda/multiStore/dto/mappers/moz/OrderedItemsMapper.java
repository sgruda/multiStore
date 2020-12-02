package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mop.ProductMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.OrderedItemsDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemsEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;

public class OrderedItemsMapper implements Mapper<OrderedItemsEntity, OrderedItemsDTO> {
    private HashMethod hashMethod;
    private ProductMapper productMapper;

    public OrderedItemsMapper() {
        this.hashMethod = new HashMethod();
        this.productMapper = new ProductMapper();
    }

    @Override
    public OrderedItemsDTO toDTO(OrderedItemsEntity entity) {
        OrderedItemsDTO dto = new OrderedItemsDTO();

        dto.setIdHash(hashMethod.hash(entity.getId()));
        dto.setIdentifier(entity.getIdentifier());
        dto.setIdentifier(entity.getIdentifier());
        dto.setOrderedNumber(entity.getOrderedNumber());
        dto.setOrderedProduct(productMapper.toDTO(entity.getProductEntity()));
        dto.setVersion(entity.getVersion());

        return dto;
    }

    @Override
    public OrderedItemsEntity updateEntity(OrderedItemsEntity entity, OrderedItemsDTO dto) {
        entity.setOrderedNumber(dto.getOrderedNumber());
        return entity;
    }
}
