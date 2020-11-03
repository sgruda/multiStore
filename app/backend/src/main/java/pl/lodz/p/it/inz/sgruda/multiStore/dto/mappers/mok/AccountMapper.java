package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok;

import lombok.extern.java.Log;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashGenerator;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;

import java.util.Set;
import java.util.stream.Collectors;
@Log
public class AccountMapper implements Mapper<AccountEntity, AccountDTO, Set<AccessLevelEntity>> {
    private HashGenerator hashGenerator;
    private AuthenticationDataMapper authenticationDataMapper;

    public AccountMapper() {
        this.hashGenerator = new HashGenerator();
        this.authenticationDataMapper = new AuthenticationDataMapper();
    }

    @Override
    public AccountDTO toDTO(AccountEntity entity) {
        AccountDTO dto = new AccountDTO();

        dto.setIdHash(hashGenerator.hash(entity.getId()));
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setRoles(
                entity.getAccessLevelEntities()
                .stream()
                .map(AccessLevelEntity::getRoleName)
                .map(RoleName::name)
                .collect(Collectors.toSet())
        );
        dto.setActive(entity.isActive());
        dto.setAuthProvider(entity.getProvider().name());
        if(entity.getAuthenticationDataEntity() != null)
            dto.setAuthenticationDataDTO(authenticationDataMapper.toDTO(entity.getAuthenticationDataEntity()));
        dto.setVersion(entity.getVersion());
        dto.setSignature(hashGenerator.sign(dto.getIdHash(), dto.getEmail(), dto.getVersion()));
        return dto;
    }
    @Override
    public AccountEntity createFromDto(AccountDTO dto, Set<AccessLevelEntity> accessLevelEntitySet) {
        AccountEntity entity = new AccountEntity();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setAccessLevelEntities(accessLevelEntitySet);
        entity.setActive(dto.isActive());
        entity.setProvider(AuthProvider.valueOf(dto.getAuthProvider()));
        entity.setAuthenticationDataEntity(authenticationDataMapper.createFromDto(dto.getAuthenticationDataDTO(), entity));
        return entity;
    }
    @Override
//    public AccountEntity updateEntity(AccountEntity entity, AccountDTO dto, Set<AccessLevelEntity> accessLevelEntitySet) throws DTOSignatureException, DTOVersionException {
    public AccountEntity updateEntity(AccountEntity entity, AccountDTO dto) throws DTOSignatureException, DTOVersionException {
//        checkSignature(dto);
//        checkVersion(entity, dto);

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
//        entity.setAccessLevelEntities(accessLevelEntitySet);
        entity.setActive(dto.isActive());
        entity.setProvider(AuthProvider.valueOf(dto.getAuthProvider()));
        entity.setAuthenticationDataEntity(authenticationDataMapper.createFromDto(dto.getAuthenticationDataDTO(), entity));
        return entity;
    }
    public void checkCorrectness(AccountEntity entity, AccountDTO dto) throws DTOSignatureException, DTOVersionException {
        if(!hashGenerator.checkSignature(dto.getSignature(), dto.getIdHash(), dto.getEmail(), dto.getVersion())) {
            throw new DTOSignatureException();
        }
        if(entity.getVersion() != dto.getVersion())
            throw new DTOVersionException();
    }
}

