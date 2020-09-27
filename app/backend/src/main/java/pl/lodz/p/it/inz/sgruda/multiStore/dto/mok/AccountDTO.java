package pl.lodz.p.it.inz.sgruda.multiStore.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public @Data class AccountDTO {
    private String idHash;
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles = new HashSet<>();
    private boolean active;
    private String authProvider;
    private AuthenticationDataDTO authenticationDataDTO;
    private long version;
    private String signature;

}
