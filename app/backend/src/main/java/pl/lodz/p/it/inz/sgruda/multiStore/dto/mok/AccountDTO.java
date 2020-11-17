package pl.lodz.p.it.inz.sgruda.multiStore.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class AccountDTO implements SignatureVerifiability {
    private String idHash;

    @NotNull(message = "{validation.notnull}")
    @Size(min = 1, max = 32, message = "{validation.size}")
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+", message = "{validation.pattern}")
    private String firstName;

    @NotNull(message = "{validation.notnull}")
    @Size(min = 1, max = 32, message = "{validation.size}")
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+", message = "{validation.pattern}")
    private String lastName;

    @NotNull(message = "{validation.notnull}")
    @Email(message = "{validation.email}")
    @Size(min = 1, max = 32, message = "{validation.size}")
    private String email;
    private Set<String> roles = new HashSet<>();
    private boolean active;
    private String authProvider;

    @NotNull
    @Valid
    private AuthenticationDataDTO authenticationDataDTO;
    private long version;
    private String signature;

    @Override
    public List<String> specifySigningParams() {
        return Arrays.asList(idHash, email, String.valueOf(version));
    }


}