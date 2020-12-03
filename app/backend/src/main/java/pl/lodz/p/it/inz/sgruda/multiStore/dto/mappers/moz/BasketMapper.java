package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.moz;

import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.BasketDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;

import java.util.stream.Collectors;

public class BasketMapper implements Mapper<BasketEntity, BasketDTO> {
    private HashMethod hashMethod;
    private OrderedItemMapper orderedItemMapper;

    public BasketMapper() {
        this.hashMethod = new HashMethod();
        this.orderedItemMapper = new OrderedItemMapper();
    }

    @Override
    public BasketDTO toDTO(BasketEntity entity) {
        BasketDTO dto = new BasketDTO();

        dto.setIdHash(hashMethod.hash(entity.getId()));
        dto.setOrderedItemDTOS(
                entity.getOrderedItemsEntities().stream()
                .map(entityItem -> orderedItemMapper.toDTO(entityItem))
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
