package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AuthenticationDataDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AuthenticationDataEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;


public class AuthenticationDataMapper implements Mapper<AuthenticationDataEntity, AuthenticationDataDTO, AccountEntity> {
    private HashMethod hashMethod;
    private ForgotPasswordTokenMapper forgotPasswordTokenMapper;

    public AuthenticationDataMapper() {
        this.hashMethod = new HashMethod();
        this.forgotPasswordTokenMapper = new ForgotPasswordTokenMapper();
    }
    @Override
    public AuthenticationDataDTO toDTO(AuthenticationDataEntity entity) {
        AuthenticationDataDTO dto = new AuthenticationDataDTO();
        dto.setIdHash(hashMethod.hash(entity.getId()));
        dto.setUsername(entity.getUsername());
        dto.setEmailVerified(entity.isEmailVerified());
        if(entity.getForgotPasswordTokenEntity() != null)
            dto.setForgotPasswordTokenDTO(forgotPasswordTokenMapper.toDTO(entity.getForgotPasswordTokenEntity()));
        dto.setVersion(entity.getVersion());
        return dto;
    }
    @Override
    public AuthenticationDataEntity createFromDto(AuthenticationDataDTO dto, AccountEntity owner) {
        AuthenticationDataEntity entity = new AuthenticationDataEntity();
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setEmailVerified(dto.isEmailVerified());
        entity.setForgotPasswordTokenEntity(forgotPasswordTokenMapper.createFromDto(dto.getForgotPasswordTokenDTO(), owner));
        return entity;
    }
    @Override
    public AuthenticationDataEntity updateEntity(AuthenticationDataEntity entity, AuthenticationDataDTO dto) {
        if(dto.getPassword() != null)
            entity.setPassword(dto.getPassword());
        entity.setEmailVerified(dto.isEmailVerified());
        if(dto.getForgotPasswordTokenDTO() != null)
            entity.setForgotPasswordTokenEntity(forgotPasswordTokenMapper.updateEntity(entity.getForgotPasswordTokenEntity(), dto.getForgotPasswordTokenDTO()));
        return entity;
    }
}
