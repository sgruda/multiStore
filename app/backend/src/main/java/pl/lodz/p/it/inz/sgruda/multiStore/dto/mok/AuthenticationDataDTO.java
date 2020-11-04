package pl.lodz.p.it.inz.sgruda.multiStore.dto.mok;

import lombok.*;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

import java.util.Arrays;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class AuthenticationDataDTO implements SignatureVerifiability {
//    private String veryficationToken;.
    private String idHash;
    private String username;
    private String password;
    private boolean emailVerified;
    private ForgotPasswordTokenDTO forgotPasswordTokenDTO;
    private long version;
    private String signature;

    @Override
    public List<String> specifySigningParams() {
        return Arrays.asList(idHash, username, String.valueOf(version));
    }
}
