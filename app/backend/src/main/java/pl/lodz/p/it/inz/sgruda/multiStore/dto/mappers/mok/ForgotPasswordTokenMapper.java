package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok;

import lombok.extern.java.Log;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.ForgotPasswordTokenDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.ForgotPasswordTokenEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;
@Log
public class ForgotPasswordTokenMapper implements Mapper<ForgotPasswordTokenEntity, ForgotPasswordTokenDTO> {
    private HashMethod hashMethod;

    public ForgotPasswordTokenMapper() {
        this.hashMethod = new HashMethod();
    }
    @Override
    public ForgotPasswordTokenDTO toDTO(ForgotPasswordTokenEntity entity) {
        ForgotPasswordTokenDTO dto = new ForgotPasswordTokenDTO();
        dto.setIdHash(hashMethod.hash(entity.getId()));
        dto.setExpireDate(entity.getExpireDate());
        dto.setHash(entity.getHash());
        dto.setOwnerUsername(entity.getAccountEntity().getUsername());
        dto.setVersion(entity.getVersion());
        return dto;
    }

    @Override
    public ForgotPasswordTokenEntity updateEntity(ForgotPasswordTokenEntity entity, ForgotPasswordTokenDTO dto) {
        entity.setExpireDate(dto.getExpireDate());
        entity.setHash(dto.getHash());
        return entity;
    }
}
