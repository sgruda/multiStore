package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashGenerator;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = ForgotPasswordTokenMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDTO toAccountDTO(AccountEntity accountEntity);
    AccountEntity createNewAccountEntity(AccountDTO accountDTO);
    void updateAccountEntityFromDTO(AccountDTO accountDTO, @MappingTarget AccountEntity accountEntity);
    Collection<AccountDTO> toAccountDTOCollection(Collection<AccountEntity> accountEntityCollection);

    default Set<String> toRolesSet(Set<AccessLevelEntity> accessLevelEntitySet) {
        return accessLevelEntitySet.stream()
                .map(AccessLevelEntity::getRoleName)
                .map(RoleName::name)
                .collect(Collectors.toSet());
    }
    default Set<AccessLevelEntity> toAccessLevelEntitySet(Set<String> rolesSet) {
        return new HashSet<>();
    }

    default String sign(String idHash, String email, long version) {
        return HashGenerator.sign(idHash, email, version);
    }

}
