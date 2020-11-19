package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok.AccountMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.*;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.CheckerAccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.services.MailSenderService;

import javax.mail.MessagingException;
import javax.management.relation.Role;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


@Log
@Validated
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/account")
public class AccountDetailsEndpoint {
    private CreateAccountService createAccountService;
    private MailSenderService mailSenderService;
    private PasswordEncoder passwordEncoder;
    private CheckerAccountDTO checkerAccountDTO;
    private AccountEditService accountEditService;
    private PasswordChangeService passwordChangeService;
    private AccountAccessLevelService accountAccessLevelService;
    private NotEmailVerifiedAccountService notEmailVerifiedAccountService;

    @Autowired
    public AccountDetailsEndpoint(CreateAccountService createAccountService, MailSenderService mailSenderService,
                                  PasswordEncoder passwordEncoder, CheckerAccountDTO checkerAccountDTO,
                                  AccountEditService accountEditService, PasswordChangeService passwordChangeService,
                                AccountAccessLevelService accountAccessLevelService, NotEmailVerifiedAccountService notEmailVerifiedAccountService) {
        this.createAccountService = createAccountService;
        this.mailSenderService = mailSenderService;
        this.passwordEncoder = passwordEncoder;
        this.checkerAccountDTO = checkerAccountDTO;
        this.accountEditService = accountEditService;
        this.passwordChangeService = passwordChangeService;
        this.accountAccessLevelService = accountAccessLevelService;
        this.notEmailVerifiedAccountService = notEmailVerifiedAccountService;
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changePassword(@Valid @RequestBody AccountDTO accountDTO) {
        AccountEntity accountEntity;
        try {
            checkerAccountDTO.checkSignature(accountDTO);
            if(accountDTO.getAuthenticationDataDTO().getPassword() == null)
                throw new AppBaseException("error.password.can.not.be.null");
            accountEntity = passwordChangeService.getAccountByEmail(accountDTO.getEmail());
            checkerAccountDTO.checkVersion(accountEntity, accountDTO);
            passwordChangeService.changePassword(accountEntity, passwordEncoder.encode(accountDTO.getAuthenticationDataDTO().getPassword()));
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.password.change.correctly."));
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> editAccount(@Valid @RequestBody AccountDTO accountDTO) {
        AccountEntity accountEntity;
        try {
            checkerAccountDTO.checkSignature(accountDTO);
            accountEntity = accountEditService.getAccountByEmail(accountDTO.getEmail());
            checkerAccountDTO.checkVersion(accountEntity, accountDTO);
            AccountMapper accountMapper = new AccountMapper();
            accountMapper.updateEntity(accountEntity, accountDTO);
            accountEditService.editAccount(accountEntity);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.edit.correctly."));
    }

    @PutMapping("/add-access-level")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addAccessLevel(@Valid @RequestBody AccountDTO accountDTO) {
        AccountEntity accountEntity;
        try {
            checkerAccountDTO.checkSignature(accountDTO);
            accountEntity = accountAccessLevelService.getAccountByEmail(accountDTO.getEmail());
            checkerAccountDTO.checkVersion(accountEntity, accountDTO);
            accountAccessLevelService.addAccessLevel(accountEntity, accountDTO.getRoles());
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.access.level.added.correctly."));
    }
    @PutMapping("/remove-access-level")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> removeAccessLevel(@Valid @RequestBody AccountDTO accountDTO) {
        AccountEntity accountEntity;
        try {
            checkerAccountDTO.checkSignature(accountDTO);
            accountEntity = accountAccessLevelService.getAccountByEmail(accountDTO.getEmail());
            checkerAccountDTO.checkVersion(accountEntity, accountDTO);
            accountAccessLevelService.removeAccessLevel(accountEntity, accountDTO.getRoles());
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.access.level.removed.correctly."));
    }
    @PostMapping("/send-email-verification")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> sendEmailToAccountVeryfication(@Valid @NotNull(message = "{validation.notnull}")
                                                                    @Email(message = "{validation.email}")
                                                                    @Size(min = 1, max = 32, message = "{validation.size}")
                                                            @RequestParam(value = "email") String email) {
        AccountEntity accountEntity;
        try {
            accountEntity = notEmailVerifiedAccountService.getAccountByEmailIfNotVerified(email);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            mailSenderService.sendRegistrationMail(accountEntity.getEmail(), accountEntity.getVeryficationToken());
        } catch (MessagingException e) {
            log.severe("Problem z mailem " + e);
        }
        return ResponseEntity.ok(new ApiResponse(true, "mail.registration.resend.correctly"));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountRequest accountRequest) {
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