package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.ForgotPasswordTokenDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.ForgotPasswordTokenEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashGenerator;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ForgotPasswordTokenMapper {

    String WITH_SECONDS= "yyyy-MM-dd HH:mm:ss";
    ForgotPasswordTokenMapper INSTANCE = Mappers.getMapper(ForgotPasswordTokenMapper.class);

    @Mapping(target = "accountUsername", source = "accountEntity")
    @Mapping(target = "expireDate", dateFormat = WITH_SECONDS)
    ForgotPasswordTokenDTO toForgotPasswordTokenDTO(ForgotPasswordTokenEntity forgotPasswordTokenEntity);

    default String toAccountUsername(AccountEntity accountEntity) {
        return accountEntity.getUsername();
    }
    default String toIdHash(ForgotPasswordTokenEntity forgotPasswordTokenEntity) {
        return HashGenerator.hash(forgotPasswordTokenEntity.getId());
    }
    default String sign(String idHash, String accountUsername, long version) {
        return HashGenerator.sign(idHash, accountUsername, version);
    }
}
