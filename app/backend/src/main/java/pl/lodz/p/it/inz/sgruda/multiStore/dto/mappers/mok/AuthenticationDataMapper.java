package pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AuthenticationDataDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AuthenticationDataEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.ForgotPasswordTokenEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashGenerator;

@Mapper(uses = ForgotPasswordTokenMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthenticationDataMapper {
    AuthenticationDataMapper INSTANCE = Mappers.getMapper(AuthenticationDataMapper.class);

    @Mapping(target = "forgotPasswordTokenDTO", source = "forgotPasswordTokenEntity")
    AuthenticationDataDTO toAuthenticationDataDTO(AuthenticationDataEntity authenticationDataEntity);

    default String toIdHash(ForgotPasswordTokenEntity forgotPasswordTokenEntity) {
        return HashGenerator.hash(forgotPasswordTokenEntity.getId());
    }
    default String sign(String idHash, String username, long version) {
        return HashGenerator.sign(idHash, username, version);
    }

    AuthenticationDataEntity createNewAuthenticationData(AuthenticationDataDTO authenticationDataDTO);
    void updateuthenticationDataEntityFromDTO(AuthenticationDataDTO authenticationDataDTO, @MappingTarget AuthenticationDataEntity authenticationDataEntity);
}
