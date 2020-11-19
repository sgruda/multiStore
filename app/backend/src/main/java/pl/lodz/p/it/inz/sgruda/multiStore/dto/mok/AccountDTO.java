package pl.lodz.p.it.inz.sgruda.multiStore.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

import javax.persistence.Basic;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class AccountDTO implements SignatureVerifiability {
    @Size(min = 64, max = 64)
    @NotNull(message = "{validation.notnull}")
    @Pattern(regexp = "[0-9a-zA-Z]+", message = "{validation.pattern}")
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

    @NotNull(message = "{validation.notnull}")
    private Set<String> roles = new HashSet<>();
    private boolean active;

    @Basic(optional = false)
    @NotNull(message = "{validation.notnull}")
    private String authProvider;

    @NotNull(message = "{validation.notnull}")
    @Valid
    private AuthenticationDataDTO authenticationDataDTO;

    @NotNull(message = "{validation.notnull}")
    private long version;
    @NotNull(message = "{validation.notnull}")
    private String signature;

    @Override
    public List<String> specifySigningParams() {
        return Arrays.asList(idHash, email, authProvider, String.valueOf(version));
    }


}