package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mop;

import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.ProductDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;


public class ProductMapper  implements Mapper<ProductEntity, ProductDTO> {

    public ProductMapper() {

    }

    @Override
    public ProductDTO toDTO(ProductEntity entity) {
        ProductDTO dto = new ProductDTO();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setInStore(entity.getInStore());
        dto.setPrice(entity.getPrice());
        dto.setType(entity.getType().name());
        dto.setCategory(entity.getCategoryEntity().getCategoryName().name());
        dto.setActive(entity.isActive());
        dto.setVersion(entity.getVersion());

        return dto;
    }

    @Override
    public ProductEntity updateEntity(ProductEntity entity, ProductDTO dto)  {
        entity.setDescription(dto.getDescription());
        entity.setInStore(dto.getInStore());
        entity.setPrice(dto.getPrice());
        return entity;
    }
}
