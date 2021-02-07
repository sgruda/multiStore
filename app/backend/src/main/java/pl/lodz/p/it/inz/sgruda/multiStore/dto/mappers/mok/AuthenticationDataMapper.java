package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AuthenticationDataDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AuthenticationDataEntity;


public class AuthenticationDataMapper implements Mapper<AuthenticationDataEntity, AuthenticationDataDTO> {
    private ForgotPasswordTokenMapper forgotPasswordTokenMapper;

    public AuthenticationDataMapper() {
        this.forgotPasswordTokenMapper = new ForgotPasswordTokenMapper();
    }
    @Override
    public AuthenticationDataDTO toDTO(AuthenticationDataEntity entity) {
        AuthenticationDataDTO dto = new AuthenticationDataDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmailVerified(entity.isEmailVerified());
        if(entity.getForgotPasswordTokenEntity() != null)
            dto.setForgotPasswordTokenDTO(forgotPasswordTokenMapper.toDTO(entity.getForgotPasswordTokenEntity()));
        dto.setVersion(entity.getVersion());
        return dto;
    }

    @Override
    public AuthenticationDataEntity updateEntity(AuthenticationDataEntity entity, AuthenticationDataDTO dto) {
        if(dto.getPassword() != null)
            entity.setPassword(dto.getPassword());
        entity.setEmailVerified(dto.isEmailVerified());
        if(dto.getForgotPasswordTokenDTO() != null)
            entity.setForgotPasswordTokenEntity(
                    forgotPasswordTokenMapper.updateEntity(
                            entity.getForgotPasswordTokenEntity(),
                            dto.getForgotPasswordTokenDTO()
                    )
            );
        entity.setVersion(dto.getVersion());
        return entity;
    }

    @Override
    public AuthenticationDataEntity createCopyOf(AuthenticationDataEntity entity, AuthenticationDataDTO dto) {
        AuthenticationDataEntity entityCopy = new AuthenticationDataEntity();
        entityCopy.setId(dto.getId());
        entityCopy.setVeryficationToken(entity.getVeryficationToken());
        entityCopy.setUsername(entity.getUsername());
        entityCopy.setPassword(entity.getPassword());
        entityCopy.setEmailVerified(entity.isEmailVerified());
//        entityCopy.setForgotPasswordTokenEntity(entity.getForgotPasswordTokenEntity());
        if(entity.getForgotPasswordTokenEntity() != null)
            entityCopy.setForgotPasswordTokenEntity(
                    forgotPasswordTokenMapper.createCopyOf(
                            entity.getForgotPasswordTokenEntity(),
                            dto.getForgotPasswordTokenDTO()
                    )
            );
        else
            entityCopy.setForgotPasswordTokenEntity(null);

        entityCopy.setVersion(dto.getVersion());
        return entityCopy;
    }
}
