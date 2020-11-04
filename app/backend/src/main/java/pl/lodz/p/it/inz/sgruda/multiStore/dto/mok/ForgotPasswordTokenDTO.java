package pl.lodz.p.it.inz.sgruda.multiStore.dto.mok;

import lombok.*;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class ForgotPasswordTokenDTO implements SignatureVerifiability {
    private String idHash;
    private LocalDateTime expireDate;
    private String hash;
    private String ownerUsername;
    private long version;
    private String signature;

    @Override
    public List<String> specifySigningParams() {
        return Arrays.asList(idHash, ownerUsername, String.valueOf(version));
    }

}
