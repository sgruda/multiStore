package pl.lodz.p.it.inz.sgruda.multiStore.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class AuthenticationDataDTO {
//    private String veryficationToken;.
    private String idHash;
    private String username;
    private String password;
    private boolean emailVerified;
    private ForgotPasswordTokenDTO forgotPasswordTokenDTO;
    private long version;
    private String signature;
}
