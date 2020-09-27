package pl.lodz.p.it.inz.sgruda.multiStore.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public @Data class ForgotPasswordTokenDTO {
    private String idHash;
    private LocalDateTime expireDate;
    private String hash;
    private String accountUsername;
    private long version;
    private String signature;
}
