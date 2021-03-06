package pl.lodz.p.it.inz.sgruda.multiStore.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.HashVerifiability;

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
public @Data class AccountDTO implements HashVerifiability {
    @NotNull(message = "validation.notnull")
    private long id;

    @NotNull(message = "validation.notnull")
    @Size(min = 1, max = 32, message = "validation.size")
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+", message = "validation.pattern")
    private String firstName;

    @NotNull(message = "validation.notnull")
    @Size(min = 1, max = 32, message = "validation.size")
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+", message = "validation.pattern")
    private String lastName;

    @NotNull(message = "validation.notnull")
    @Email(message = "validation.email")
    @Size(min = 1, max = 32, message = "validation.size")
    private String email;

    @NotNull(message = "validation.notnull")
    private Set<String> roles = new HashSet<>();

    @NotNull(message = "validation.notnull")
    private boolean active;

    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "(system|google|facebook)", message = "validation.pattern")
    private String authProvider;

    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "(pl|en|)", message = "validation.pattern")
    private String language;

    @Valid
    private AuthenticationDataDTO authenticationDataDTO;

    @NotNull(message = "validation.notnull")
    private long version;

    @NotNull(message = "validation.notnull")
    private String hash;

    @Override
    public List<String> specifyHashingParams() {
        return Arrays.asList(String.valueOf(id), email, authProvider, String.valueOf(version));
    }


}