package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok;

import lombok.extern.java.Log;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.Mapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;
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
        AccountEntity entityCopy = new AccountEntity();
        entityCopy.setId(dto.getId());
        entityCopy.setFirstName(dto.getFirstName());
        entityCopy.setLastName(dto.getLastName());
        entityCopy.setEmail(dto.getEmail());
        entityCopy.setAccessLevelEntities(entity.getAccessLevelEntities());
        entityCopy.setActive(dto.isActive());
        entityCopy.setProvider(AuthProvider.valueOf(dto.getAuthProvider()));
        entityCopy.setProviderId(entity.getProviderId());
        entityCopy.setLanguage(Language.valueOf(dto.getLanguage()));
        entityCopy.setAuthenticationDataEntity(authenticationDataMapper.updateEntity(entity.getAuthenticationDataEntity(), dto.getAuthenticationDataDTO()));
        entityCopy.setOrderCollection(entity.getOrderCollection());
        entityCopy.setBasketEntity(entity.getBasketEntity());

        entityCopy.setVersion(dto.getVersion());
        return entityCopy;
    }
}

