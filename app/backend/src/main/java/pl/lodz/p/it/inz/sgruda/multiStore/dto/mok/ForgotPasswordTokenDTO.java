package pl.lodz.p.it.inz.sgruda.multiStore.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class ForgotPasswordTokenDTO implements SignatureVerifiability {
    @Size(min = 64, max = 64)
    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "[0-9a-zA-Z]+", message = "validation.pattern")
    private String idHash;

    @NotNull(message = "validation.notnull")
    private LocalDateTime expireDate;

    @Size(min = 64, max = 64)
    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "[0-9a-zA-Z]+", message = "validation.pattern")
    private String hash;

    @NotNull(message = "validation.notnull")
    private String ownerUsername;

    @NotNull(message = "validation.notnull")
    private long version;

    @NotNull(message = "validation.notnull")
    private String signature;

    @Override
    public List<String> specifySigningParams() {
        return Arrays.asList(idHash, ownerUsername, String.valueOf(version));
    }

}
