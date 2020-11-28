package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;

public interface Mapper<Entity, Dto> {
    Dto toDTO(Entity entity);
    Entity updateEntity(Entity entity, Dto dto);
}