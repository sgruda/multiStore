package pl.lodz.p.it.inz.sgruda.multiStore.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.HashVerifiability;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class ForgotPasswordTokenDTO implements HashVerifiability {
    @NotNull(message = "validation.notnull")
    private long id;

    @NotNull(message = "validation.notnull")
    private LocalDateTime expireDate;

    @Size(min = 6, max = 6)
    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "[0-9a-zA-Z]+", message = "validation.pattern")
    private String token;

    @NotNull(message = "validation.notnull")
    private String ownerUsername;

    @NotNull(message = "validation.notnull")
    private long version;

    @NotNull(message = "validation.notnull")
    private String hash;

    @Override
    public List<String> specifyHashingParams() {
        return Arrays.asList(String.valueOf(id), ownerUsername, String.valueOf(version));
    }

}
