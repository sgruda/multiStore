package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok;

import lombok.extern.java.Log;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.ForgotPasswordTokenDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.ForgotPasswordTokenEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashGenerator;
@Log
public class ForgotPasswordTokenMapper implements Mapper<ForgotPasswordTokenEntity, ForgotPasswordTokenDTO, AccountEntity> {
    private HashGenerator hashGenerator;

    public ForgotPasswordTokenMapper() {
        this.hashGenerator = new HashGenerator();
    }
    @Override
    public ForgotPasswordTokenDTO toDTO(ForgotPasswordTokenEntity entity) {
        ForgotPasswordTokenDTO dto = new ForgotPasswordTokenDTO();
        dto.setIdHash(hashGenerator.hash(entity.getId()));
        dto.setExpireDate(entity.getExpireDate());
        dto.setHash(entity.getHash());
        dto.setOwnerUsername(entity.getAccountEntity().getUsername());
        dto.setVersion(entity.getVersion());
        dto.setSignature(hashGenerator.sign(dto.getIdHash(), dto.getOwnerUsername(), dto.getVersion()));
        return dto;
    }
    @Override
    public ForgotPasswordTokenEntity createFromDto(ForgotPasswordTokenDTO dto, AccountEntity owner) {
        ForgotPasswordTokenEntity entity = new ForgotPasswordTokenEntity();
        entity.setExpireDate(dto.getExpireDate());
        entity.setHash(dto.getHash());
        entity.setAccountEntity(owner);
        return entity;
    }

    @Override
    public ForgotPasswordTokenEntity updateEntity(ForgotPasswordTokenEntity entity, ForgotPasswordTokenDTO dto) throws DTOSignatureException, DTOVersionException {
        checkSignature(dto);
        checkVersion(entity, dto);

        entity.setExpireDate(dto.getExpireDate());
        entity.setHash(dto.getHash());
        return entity;
    }

    private void checkSignature(ForgotPasswordTokenDTO dto) throws DTOSignatureException {
        if(!hashGenerator.checkSignature(dto.getSignature(), dto.getIdHash(), dto.getOwnerUsername(), dto.getVersion())) {
            throw new DTOSignatureException();
        }
    }
    private void checkVersion(ForgotPasswordTokenEntity entity, ForgotPasswordTokenDTO dto) throws DTOVersionException {
        if(entity.getVersion() != dto.getVersion())
            throw new DTOVersionException();
    }
}
