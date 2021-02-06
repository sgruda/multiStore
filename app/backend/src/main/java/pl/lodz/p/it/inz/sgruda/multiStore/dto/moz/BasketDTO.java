package pl.lodz.p.it.inz.sgruda.multiStore.dto.moz;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class BasketDTO implements HashVerifiability  {
    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "[0-9]+", message = "validation.pattern")
    private long id;

    @NotNull(message = "validation.notnull")
    @Valid
    private List<OrderedItemDTO> orderedItemDTOS = new ArrayList<>();

    @NotNull(message = "validation.notnull")
    @Email(message = "validation.email")
    @Size(min = 1, max = 32, message = "validation.size")
    private String ownerEmail;

    @NotNull(message = "validation.notnull")
    private long version;

    @NotNull(message = "validation.notnull")
    private String hash;

    @Override
    public List<String> specifyHashingParams() {
        return Arrays.asList(String.valueOf(id), ownerEmail, String.valueOf(version));
    }
}
