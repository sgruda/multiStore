package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.CreateAccountService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.services.MailSenderService;

import javax.mail.MessagingException;
import javax.management.relation.Role;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


@Log
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/accounts")
public class AccountDetailsEndpoint {
    private CreateAccountService createAccountService;
    private MailSenderService mailSenderService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AccountDetailsEndpoint(CreateAccountService createAccountService, MailSenderService mailSenderService, PasswordEncoder passwordEncoder) {
        this.createAccountService = createAccountService;
        this.mailSenderService = mailSenderService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest accountRequest) {
        AccountEntity accountEntity = new AccountEntity(
                accountRequest.getFirstName(),
                accountRequest.getLastName(),
                accountRequest.getEmail(),
                accountRequest.getUsername(),
                passwordEncoder.encode(accountRequest.getPassword())
        );

        AccountEntity resultAccount;
        try {
            resultAccount = createAccountService.createAccount(accountEntity, accountRequest.getRoles());
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            mailSenderService.sendRegistrationMail(resultAccount.getEmail(), resultAccount.getVeryficationToken());
        } catch (MessagingException e) {
            log.severe("Problem z mailem " + e);
        }


        return ResponseEntity.ok(new ApiResponse(true, "account.correctly.created"));
    }

    @Getter
    private static class CreateAccountRequest {
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
        @Size(min = 1, max = 32, message = "{validation.size}")
        @Pattern(regexp = "[a-zA-Z0-9!@#$%^*]+", message = "{validation.pattern}")
        private String username;

        @NotNull(message = "{validation.notnull}")
        @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "{validation.pattern}")
        private String password;

        @NotNull(message = "{validation.notnull}")
        private Set<String> roles = new HashSet<>();
    }
}