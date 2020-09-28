package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers;

import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;

public interface Mapper<Entity, Dto, T> {
    Dto toDTO(Entity entity);
    Entity createFromDto(Dto dto, T additionalPropertyToSet);
    Entity updateEntity(Entity entity, Dto dto) throws DTOSignatureException, DTOVersionException;
}