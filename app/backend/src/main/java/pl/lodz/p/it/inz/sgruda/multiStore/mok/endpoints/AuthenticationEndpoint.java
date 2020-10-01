package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AuthenticationDataEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.EmailAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.UsernameAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.payloads.response.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.payloads.response.JwtAuthenticationResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.AccountService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Log
@RestController
@RequestMapping("/api/auth")
public class AuthenticationEndpoint {

    private @Autowired AccountService accountService;
    private @Autowired PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String tokenJWT = accountService.authenticateUser(
                loginRequest.getUsername(),
                loginRequest.getPassword());
        return ResponseEntity.ok(new JwtAuthenticationResponse(tokenJWT));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        AccountEntity accountEntity = new AccountEntity(
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        AccountEntity resultAccount;
        try {
            resultAccount = accountService.registerAccount(accountEntity);
        } catch (AppBaseException e) {
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(resultAccount.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
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
    }
    @Getter
    private static class LoginRequest {
        @NotNull(message = "{validation.notnull}")
        @Size(min = 1, max = 32, message = "{validation.size}")
        @Pattern(regexp = "[a-zA-Z0-9!@#$%^*]+", message = "{validation.pattern}")
        private String username;

        @NotNull(message = "{validation.notnull}")
        @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "{validation.pattern}")
        private String password;
    }

}
