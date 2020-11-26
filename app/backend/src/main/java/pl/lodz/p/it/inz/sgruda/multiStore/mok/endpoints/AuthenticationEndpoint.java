package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;

import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.AuthService;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.MailVerifierService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.JwtAuthenticationResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.Language;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.services.MailSenderService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.services.MailSenderServiceImpl;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.net.URI;

@Log
@Validated
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/auth")
public class AuthenticationEndpoint {

    private AuthService authService;
    private MailVerifierService mailVerifierService;
    private PasswordEncoder passwordEncoder;
    private MailSenderService mailSenderServiceImpl;

    @Autowired
    public AuthenticationEndpoint(AuthService authService, MailVerifierService mailVerifierService, PasswordEncoder passwordEncoder, MailSenderServiceImpl mailSenderServiceImpl) {
        this.authService = authService;
        this.mailVerifierService = mailVerifierService;
        this.passwordEncoder = passwordEncoder;
        this.mailSenderServiceImpl = mailSenderServiceImpl;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateAccount(@Valid @RequestBody SignInRequest signInRequest) {
        String tokenJWT = authService.authenticateAccount(
                signInRequest.getUsername(),
                signInRequest.getPassword());
        return ResponseEntity.ok(new JwtAuthenticationResponse(tokenJWT));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerAccount(@Valid @RequestBody SignUpRequest signUpRequest) {
        AccountEntity accountEntity = new AccountEntity(
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        AccountEntity resultAccount;
        try {
            resultAccount = authService.registerAccount(accountEntity);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            mailSenderServiceImpl.sendRegistrationMail(resultAccount.getEmail(), resultAccount.getVeryficationToken(), Language.valueOf(signUpRequest.getLanguage()));
        } catch (MessagingException e) {
            log.severe("Problem z mailem " + e);
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(resultAccount.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "response.registration.success"));
    }
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid  @NotNull(message = "{validation.notnull}")
                                                 @Pattern(regexp = "[0-9a-zA-Z-]+", message = "{validation.pattern}")
                                                 @Size(min = 36, max = 36, message = "{validation.size}")
                                             @RequestParam("token") String veryficationToken) {
        try {
            mailVerifierService.verifyEmail(veryficationToken);
            return ResponseEntity.ok(new ApiResponse(true, "account.email.correctly.verified"));
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Getter
    private static class SignUpRequest {
        @NotNull(message = "{validation.notnull}")
        @Size(min = 1, max = 32, message = "{validation.size}")
        @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+", message = "{validation.pattern}")
        private String firstName;

        @NotNull(message = "{validation.notnull}")
        @Size(min = 1, max = 32, message = "{validation.size}")
        @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+", message = "{validation.pattern}")
        private String lastName;

        @NotNull(message = "{validation.notnull}")
        @Email(message = "{validation.email}")
        @Size(min = 1, max = 32, message = "{validation.size}")
        private String email;

        @NotNull(message = "{validation.notnull}")
        @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "{validation.pattern}")
        private String password;

        @NotNull(message = "{validation.notnull}")
        @Size(min = 1, max = 32, message = "{validation.size}")
        @Pattern(regexp = "[a-zA-Z0-9!@#$%^*]+", message = "{validation.pattern}")
        private String username;

        @NotNull(message = "{validation.notnull}")
        @Pattern(regexp = "(pl|en)", message = "{validation.pattern}")
        private String language;
    }
    @Getter
    private static class SignInRequest {
        @NotNull(message = "{validation.notnull}")
        @Size(min = 1, max = 32, message = "{validation.size}")
        @Pattern(regexp = "[a-zA-Z0-9!@#$%^*]+", message = "{validation.pattern}")
        private String username;

        @NotNull(message = "{validation.notnull}")
        @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "{validation.pattern}")
        private String password;
    }

}
