package pl.lodz.p.it.inz.sgruda.multiStore.dto.mok;

import lombok.*;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class AuthenticationDataDTO implements SignatureVerifiability {
//    private String veryficationToken;.
    private String idHash;

    @NotNull(message = "{validation.notnull}")
    @Size(min = 1, max = 32, message = "{validation.size}")
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^*]+", message = "{validation.pattern}")
    private String username;

    @NotNull(message = "{validation.notnull}")
    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "{validation.pattern}")
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
