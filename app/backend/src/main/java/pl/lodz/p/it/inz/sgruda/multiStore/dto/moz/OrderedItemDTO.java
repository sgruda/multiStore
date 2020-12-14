package pl.lodz.p.it.inz.sgruda.multiStore.dto.moz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.ProductDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class OrderedItemDTO implements SignatureVerifiability {
    @Size(max = 64, message = "validation.size")
    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "[0-9a-zA-Z]+", message = "validation.pattern")
    private String idHash;

    @NotNull(message = "validation.notnull")
    @Size(max = 36, message = "validation.size")
    @Pattern(regexp = "[0-9A-Za-z-]+", message = "validation.pattern")
    private String identifier;

    @Digits(integer = 7, fraction = 0, message = "validation.digits")
    @NotNull(message = "validation.notnull")
    private int orderedNumber;

    @NotNull(message = "validation.notnull")
    @Valid
    private ProductDTO orderedProduct;

    @NotNull(message = "validation.notnull")
    private long version;

    @NotNull(message = "validation.notnull")
    private String signature;

    @Override
    public List<String> specifySigningParams() {
        return Arrays.asList(idHash, identifier, String.valueOf(version));
    }
}
