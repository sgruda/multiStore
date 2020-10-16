package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok;

import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AuthenticationDataDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.ForgotPasswordTokenDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AuthenticationDataEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.ForgotPasswordTokenEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashGenerator;


public class AuthenticationDataMapper implements Mapper<AuthenticationDataEntity, AuthenticationDataDTO, AccountEntity> {
    private HashGenerator hashGenerator;
    private ForgotPasswordTokenMapper forgotPasswordTokenMapper;

    public AuthenticationDataMapper() {
        this.hashGenerator = new HashGenerator();
        this.forgotPasswordTokenMapper = new ForgotPasswordTokenMapper();
    }
    @Override
    public AuthenticationDataDTO toDTO(AuthenticationDataEntity entity) {
        AuthenticationDataDTO dto = new AuthenticationDataDTO();
        dto.setIdHash(hashGenerator.hash(entity.getId()));
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setEmailVerified(entity.isEmailVerified());
        if(entity.getForgotPasswordTokenEntity() != null)
            dto.setForgotPasswordTokenDTO(forgotPasswordTokenMapper.toDTO(entity.getForgotPasswordTokenEntity()));
        dto.setVersion(entity.getVersion());
        dto.setSignature(hashGenerator.sign(dto.getIdHash(), dto.getUsername(), dto.getVersion()));
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
    public AuthenticationDataEntity updateEntity(AuthenticationDataEntity entity, AuthenticationDataDTO dto) throws DTOSignatureException, DTOVersionException {
        checkSignature(dto);
        checkVersion(entity, dto);

        entity.setPassword(dto.getPassword());
        entity.setEmailVerified(dto.isEmailVerified());
        entity.setForgotPasswordTokenEntity(forgotPasswordTokenMapper.updateEntity(entity.getForgotPasswordTokenEntity(), dto.getForgotPasswordTokenDTO()));
        return entity;
    }
    private void checkSignature(AuthenticationDataDTO dto) throws DTOSignatureException {
        if(!hashGenerator.checkSignature(dto.getSignature(), dto.getIdHash(), dto.getUsername(), dto.getVersion())) {
            throw new DTOSignatureException();
        }
    }
    private void checkVersion(AuthenticationDataEntity entity, AuthenticationDataDTO dto) throws DTOVersionException {
        if(entity.getVersion() != dto.getVersion())
            throw new DTOVersionException();
    }
}
