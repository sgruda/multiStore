package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers;

public interface Mapper<Entity, Dto> {
    Dto toDTO(Entity entity);
    Entity updateEntity(Entity entity, Dto dto);
}