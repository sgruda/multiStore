package pl.lodz.p.it.inz.sgruda.multiStore.dto.moz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public @Data class OrderDTO implements SignatureVerifiability {
    @Size(max = 64, message = "validation.size")
    @NotNull(message = "validation.notnull")
    @Pattern(regexp = "[0-9a-zA-Z]+", message = "validation.pattern")
    private String idHash;

    @NotNull(message = "validation.notnull")
    @Size(min = 36, max = 36, message = "validation.size")
    @Pattern(regexp = "[0-9A-Za-z-]+", message = "validation.pattern")
    private String identifier;

    @NotNull(message = "validation.notnull")
    private LocalDateTime orderDate;

    @NotNull(message = "validation.notnull")
    @Email(message = "validation.email")
    @Size(min = 1, max = 32, message = "validation.size")
    private String buyerEmail;

    @NotNull(message = "validation.notnull")
    @Valid
    private Set<OrderedItemDTO> orderedItemDTOS = new HashSet<>();

    @Digits(integer = 7, fraction = 2, message = "validation.digits")
    @NotNull(message = "validation.notnull")
    private double totalPrice;

    @NotNull(message = "validation.notnull")
    private String status;

    @NotNull(message = "validation.notnull")
    private long version;

    @NotNull(message = "validation.notnull")
    private String signature;

    @Override
    public List<String> specifySigningParams() {
        String items = orderedItemDTOS.stream()
                .map(item -> item.getSignature())
                .collect(Collectors.joining());
        return Arrays.asList(idHash, identifier, orderDate.toString(), buyerEmail, String.valueOf(totalPrice), items, String.valueOf(version));
    }
}
