package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mop;

import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.PromotionDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;

public class PromotionMapper implements Mapper<PromotionEntity, PromotionDTO> {
    private HashMethod hashMethod;

    public PromotionMapper() {
        this.hashMethod = new HashMethod();
    }

    @Override
    public PromotionDTO toDTO(PromotionEntity entity) {
       PromotionDTO dto = new PromotionDTO();

       dto.setIdHash(hashMethod.hash(entity.getId()));
       dto.setName(entity.getName());
       dto.setDiscount(entity.getDiscount());
       dto.setOnCategory(entity.getCategoryEntity().getCategoryName().name());
       dto.setActive(entity.isActive());
       dto.setVersion(entity.getVersion());

       return dto;
    }

    @Override
    public PromotionEntity updateEntity(PromotionEntity entity, PromotionDTO dto) {
        entity.setDiscount(dto.getDiscount());
        return entity;
    }
}
