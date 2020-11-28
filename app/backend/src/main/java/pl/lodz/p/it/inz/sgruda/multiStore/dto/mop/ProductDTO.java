package pl.lodz.p.it.inz.sgruda.multiStore.dto.mop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class ProductDTO implements SignatureVerifiability {
    @Size(min = 0, max = 64)
    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "[0-9a-zA-Z]+", message = "validation.pattern")
    private String idHash;

    @NotNull(message = "validation.notnull")
    @Size(min = 1, max = 32, message = "validation.size")
    @Pattern(regexp = "[0-9a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ ]+", message = "validation.pattern")
    private String title;

    @NotNull(message = "validation.notnull")
    @Size(min = 1, max = 512, message = "validation.size")
    private String description;

    @Digits(integer = 7, fraction = 0, message = "validation.digits")
    @NotNull(message = "validation.notnull")
    private int inStore;

    @Digits(integer = 7, fraction = 2, message = "validation.digits")
    @NotNull(message = "validation.notnull")
    private double price;

    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "(book|movie)", message = "validation.pattern")
    private String type;

    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "(fantasy|action|adventure|history|science|fiction|detective|document|novel)", message = "validation.pattern")
    private String category;

    @NotNull(message = "validation.notnull")
    private boolean active;

    @NotNull(message = "validation.notnull")
    private long version;

    @NotNull(message = "validation.notnull")
    private String signature;

    @Override
    public List<String> specifySigningParams() {
        return Arrays.asList(idHash, title, type, category, String.valueOf(version));
    }
}
