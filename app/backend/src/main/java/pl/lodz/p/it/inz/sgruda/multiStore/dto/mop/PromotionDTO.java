package pl.lodz.p.it.inz.sgruda.multiStore.dto.mop;

import lombok.*;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.CategoryEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class PromotionDTO implements SignatureVerifiability {
    @Size(max = 64, message = "validation.size")
    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "[0-9a-zA-Z]+", message = "validation.pattern")
    private String idHash;

    @NotNull(message = "validation.notnull")
    @Size(min = 1, max = 32, message = "validation.size")
    @Pattern(regexp = "[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ ]+", message = "validation.pattern")
    private String name;

    @Digits(integer = 2, fraction = 2, message = "validation.digits")
    @NotNull(message = "validation.notnull")
    private double discount;

    @NotNull(message = "validation.notnull")
    private String onCategory;

    @NotNull(message = "validation.notnull")
    private boolean active;

    @NotNull(message = "validation.notnull")
    private long version;

    @NotNull(message = "validation.notnull")
    private String signature;

    @Override
    public List<String> specifySigningParams() {
        return Arrays.asList(idHash, name, onCategory, String.valueOf(version));
    }
}
