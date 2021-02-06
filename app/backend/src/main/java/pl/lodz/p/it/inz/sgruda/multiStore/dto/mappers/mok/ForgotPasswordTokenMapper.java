package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok;

import lombok.extern.java.Log;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.ForgotPasswordTokenDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.ForgotPasswordTokenEntity;

@Log
public class ForgotPasswordTokenMapper implements Mapper<ForgotPasswordTokenEntity, ForgotPasswordTokenDTO> {

    public ForgotPasswordTokenMapper() {

    }
    @Override
    public ForgotPasswordTokenDTO toDTO(ForgotPasswordTokenEntity entity) {
        ForgotPasswordTokenDTO dto = new ForgotPasswordTokenDTO();
        dto.setId(entity.getId());
        dto.setExpireDate(entity.getExpireDate());
        dto.setToken(entity.getToken());
        dto.setOwnerUsername(entity.getAccountEntity().getUsername());
        dto.setVersion(entity.getVersion());
        return dto;
    }

    @Override
    public ForgotPasswordTokenEntity updateEntity(ForgotPasswordTokenEntity entity, ForgotPasswordTokenDTO dto) {
        entity.setExpireDate(dto.getExpireDate());
        entity.setToken(dto.getToken());
        return entity;
    }
}
