package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok;

import lombok.extern.java.Log;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.Language;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;

import java.util.stream.Collectors;
@Log
public class AccountMapper implements Mapper<AccountEntity, AccountDTO> {
    private AuthenticationDataMapper authenticationDataMapper;

    public AccountMapper() {
        this.authenticationDataMapper = new AuthenticationDataMapper();
    }
    @Override
    public AccountDTO toDTO(AccountEntity entity) {
        AccountDTO dto = new AccountDTO();

        dto.setId(entity.getId());
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
        dto.setLanguage(entity.getLanguage().name());
        if(entity.getAuthenticationDataEntity() != null)
            dto.setAuthenticationDataDTO(authenticationDataMapper.toDTO(entity.getAuthenticationDataEntity()));
        dto.setVersion(entity.getVersion());
        return dto;
    }

    @Override
    public AccountEntity updateEntity(AccountEntity entity, AccountDTO dto)  {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAccessLevelEntities(entity.getAccessLevelEntities());
        entity.setActive(dto.isActive());
        entity.setLanguage(Language.valueOf(dto.getLanguage()));
        if(dto.getAuthenticationDataDTO() != null)
            entity.setAuthenticationDataEntity(
                authenticationDataMapper.updateEntity(
                        entity.getAuthenticationDataEntity(),
                        dto.getAuthenticationDataDTO()
                )
            );

        entity.setVersion(dto.getVersion());
        return entity;
    }
    public AccountEntity createCopyOf(AccountEntity entity, AccountDTO accountDTO) {
        AccountEntity entityCopy = new AccountEntity();
        entityCopy.setId(accountDTO.getId());
        entityCopy.setFirstName(entity.getFirstName());
        entityCopy.setLastName(entity.getLastName());
        entityCopy.setEmail(entity.getEmail());
        entityCopy.setAccessLevelEntities(entity.getAccessLevelEntities());
        entityCopy.setActive(entity.isActive());
        entityCopy.setProvider(entity.getProvider());
        entityCopy.setProviderId(entity.getProviderId());
        entityCopy.setLanguage(entity.getLanguage());
//        entityCopy.setAuthenticationDataEntity(entity.getAuthenticationDataEntity());
        if(entity.getAuthenticationDataEntity() != null)
            entityCopy.setAuthenticationDataEntity(
                    authenticationDataMapper.createCopyOf(
                            entity.getAuthenticationDataEntity(),
                            accountDTO.getAuthenticationDataDTO()
                    )
            );
        else
            entityCopy.setAuthenticationDataEntity(null);
//        entityCopy.setAuthenticationDataEntity(authenticationDataMapper.updateEntity(entity.getAuthenticationDataEntity(), dto.getAuthenticationDataDTO()));
        entityCopy.setOrderCollection(entity.getOrderCollection());
        entityCopy.setBasketEntity(entity.getBasketEntity());

        entityCopy.setVersion(accountDTO.getVersion());
        return entityCopy;
    }
}

