package pl.lodz.p.it.inz.sgruda.multiStore.mok.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String accessToken;
    private final String tokenType = "Bearer";
}
