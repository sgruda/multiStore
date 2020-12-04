package pl.lodz.p.it.inz.sgruda.multiStore.dto.moz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

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
public @Data class BasketDTO implements SignatureVerifiability  {
    @Size(max = 64, message = "validation.size")
    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "[0-9a-zA-Z]+", message = "validation.pattern")
    private String idHash;

    @NotNull(message = "validation.notnull")
    @Valid
    private Set<OrderedItemDTO> orderedItemDTOS = new HashSet<>();

    @NotNull(message = "validation.notnull")
    @Email(message = "validation.email")
    @Size(min = 1, max = 32, message = "validation.size")
    private String ownerEmail;

    @NotNull(message = "validation.notnull")
    private long version;

    @NotNull(message = "validation.notnull")
    private String signature;

    @Override
    public List<String> specifySigningParams() {
        return Arrays.asList(idHash, ownerEmail, String.valueOf(version));
    }
}
