package pl.lodz.p.it.inz.sgruda.multiStore.mok.payloads.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;

@Getter
@Setter
public class SignUpRequest {

    @NotBlank
//    @Size(min = 4, max = 40)
    private String firstname;

    @NotBlank
    private String lastname;

    @NotBlank
//    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
//    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
//    @Size(min = 6, max = 20)
    private String password;

}
