package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.BasketDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;

import java.util.stream.Collectors;

public class BasketMapper implements Mapper<BasketEntity, BasketDTO> {
    private HashMethod hashMethod;
    private OrderedItemsMapper orderedItemsMapper;

    public BasketMapper() {
        this.hashMethod = new HashMethod();
        this.orderedItemsMapper = new OrderedItemsMapper();
    }

    @Override
    public BasketDTO toDTO(BasketEntity entity) {
        BasketDTO dto = new BasketDTO();

        dto.setIdHash(hashMethod.hash(entity.getId()));
        dto.setOrderedItemsDTOS(
                entity.getOrderedItemsEntities().stream()
                .map(entityItem -> orderedItemsMapper.toDTO(entityItem))
                .collect(Collectors.toSet())
        );
        dto.setOwnerEmail(entity.getAccountEntity().getEmail());
        dto.setVersion(entity.getVersion());

        return dto;
    }

    @Override
    public BasketEntity updateEntity(BasketEntity entity, BasketDTO dto) {
        return entity;
    }
}
