package pl.lodz.p.it.inz.sgruda.multiStore.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.HashVerifiability;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class AuthenticationDataDTO implements HashVerifiability {
    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "[0-9]+", message = "validation.pattern")
    private long id;

    @NotNull(message = "validation.notnull")
    @Size(min = 1, max = 32, message = "validation.size")
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^*]+", message = "validation.pattern")
    private String username;

    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "validation.pattern")
    private String password;

    @NotNull(message = "validation.notnull")
    private boolean emailVerified;

    @Valid
    private ForgotPasswordTokenDTO forgotPasswordTokenDTO;

    @NotNull(message = "{validation.notnull}")
    private long version;
    @NotNull(message = "{validation.notnull}")
    private String hash;

    @Override
    public List<String> specifyHashingParams() {
        return Arrays.asList(String.valueOf(id), username, String.valueOf(version));
    }
}

