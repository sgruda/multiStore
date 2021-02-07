package pl.lodz.p.it.inz.sgruda.multiStore.dto.mop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.HashVerifiability;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class PromotionDTO implements HashVerifiability {
    @NotNull(message = "validation.notnull")
    private long id;

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
    private LocalDateTime expireDate;

    @NotNull(message = "validation.notnull")
    private long version;

    @NotNull(message = "validation.notnull")
    private String hash;

    @Override
    public List<String> specifyHashingParams() {
        return Arrays.asList(String.valueOf(id), name, onCategory, expireDate.toString(), String.valueOf(version));
    }
}
