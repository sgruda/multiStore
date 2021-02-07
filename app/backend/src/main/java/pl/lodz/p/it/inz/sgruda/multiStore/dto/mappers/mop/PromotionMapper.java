package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mop;

import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.ProductDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.PromotionDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;

public class PromotionMapper implements Mapper<PromotionEntity, PromotionDTO> {

    public PromotionMapper() {

    }

    @Override
    public PromotionDTO toDTO(PromotionEntity entity) {
       PromotionDTO dto = new PromotionDTO();

        dto.setId(entity.getId());
       dto.setName(entity.getName());
       dto.setDiscount(entity.getDiscount());
       dto.setOnCategory(entity.getCategoryEntity().getCategoryName().name());
       dto.setExpireDate(entity.getExpireDate());
       dto.setActive(entity.isActive());
       dto.setVersion(entity.getVersion());

       return dto;
    }

    @Override
    public PromotionEntity updateEntity(PromotionEntity entity, PromotionDTO dto) {
        entity.setDiscount(dto.getDiscount());
        entity.setActive(dto.isActive());
        entity.setVersion(dto.getVersion());
        return entity;
    }

    public PromotionEntity createCopyOf(PromotionEntity entity, ProductDTO dto) {
        PromotionEntity entityCopy = new PromotionEntity();
        entityCopy.setId(dto.getId());
        entityCopy.setName(entity.getName());
        entityCopy.setDiscount(entity.getDiscount());
        entityCopy.setCategoryEntity(entity.getCategoryEntity());
        entityCopy.setActive(entity.isActive());
        entityCopy.setExpireDate(entity.getExpireDate());
        entityCopy.setVersion(dto.getVersion());
        return entityCopy;
    }
}
