package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;

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
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok.AccountMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.*;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mok.CheckerAccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.Language;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.services.MailSenderService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
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
            checkerAccountDTO.checkAccountDTOSignature(accountDTO);
            if(accountDTO.getAuthenticationDataDTO().getPassword() == null)
                throw new AppBaseException("error.password.can.not.be.null");
            accountEntity = passwordChangeService.getAccountByEmail(accountDTO.getEmail());
            AccountMapper accountMapper = new AccountMapper();
            AccountEntity entityCopy = accountMapper.createCopyOf(accountEntity, accountDTO);
            passwordChangeService.changePassword(entityCopy, passwordEncoder.encode(accountDTO.getAuthenticationDataDTO().getPassword()));
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
            checkerAccountDTO.checkAccountDTOSignature(accountDTO);
            accountEntity = accountEditService.getAccountByEmail(accountDTO.getEmail());
            AccountMapper accountMapper = new AccountMapper();
            AccountEntity entityCopy = accountMapper.createCopyOf(accountEntity, accountDTO);
            accountMapper.updateEntity(entityCopy, accountDTO);
            accountEditService.editAccount(entityCopy);
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
            checkerAccountDTO.checkAccountDTOSignature(accountDTO);
            accountEntity = accountAccessLevelService.getAccountByEmail(accountDTO.getEmail());
            AccountMapper accountMapper = new AccountMapper();
            AccountEntity entityCopy = accountMapper.createCopyOf(accountEntity, accountDTO);

            accountAccessLevelService.addAccessLevel(entityCopy, accountDTO.getRoles());
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
            checkerAccountDTO.checkAccountDTOSignature(accountDTO);
            accountEntity = accountAccessLevelService.getAccountByEmail(accountDTO.getEmail());
            AccountMapper accountMapper = new AccountMapper();
            AccountEntity entityCopy = accountMapper.createCopyOf(accountEntity, accountDTO);
            accountAccessLevelService.removeAccessLevel(entityCopy, accountDTO.getRoles());
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.access.level.removed.correctly."));
    }
    @PutMapping("/send-email-verification")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> sendEmailToAccountVeryfication(@Valid @NotNull(message = "validation.notnull")
                                                                    @Email(message = "validation.email")
                                                                    @Size(min = 1, max = 32, message = "validation.size")
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
            mailSenderService.sendRegistrationMail(accountEntity.getEmail(), accountEntity.getVeryficationToken(), accountEntity.getLanguage());
        } catch (MessagingException e) {
            log.severe("Problem with mail " + e);
        }
        return ResponseEntity.ok(new ApiResponse(true, "mail.registration.resend.correctly"));
    }

    @PostMapping("/remove")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> removeAccountWithNotVerifiedEmail(@Valid @RequestBody AccountDTO accountDTO) {
        AccountEntity accountEntity;
        try {
            checkerAccountDTO.checkAccountDTOSignature(accountDTO);
            accountEntity = notEmailVerifiedAccountService.getAccountByEmailIfNotVerified(accountDTO.getEmail());
            AccountMapper accountMapper = new AccountMapper();
            AccountEntity entityCopy = accountMapper.createCopyOf(accountEntity, accountDTO);
            notEmailVerifiedAccountService.removeAccountWithNotVerifiedEmail(entityCopy);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.removed.correctly."));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountRequest accountRequest) {
        AccountEntity accountEntity = new AccountEntity(
                accountRequest.getFirstName(),
                accountRequest.getLastName(),
                accountRequest.getEmail(),
                accountRequest.getUsername(),
                passwordEncoder.encode(accountRequest.getPassword()),
                Language.valueOf(accountRequest.getLanguage())
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
            mailSenderService.sendRegistrationMail(resultAccount.getEmail(), resultAccount.getVeryficationToken(), Language.valueOf(accountRequest.getLanguage()));
        } catch (MessagingException e) {
            log.severe("Problem z mailem " + e);
        }

        return ResponseEntity.ok(new ApiResponse(true, "account.correctly.created"));
    }

    @Getter
    private static class CreateAccountRequest {
        @NotNull(message = "validation.notnull")
        @Size(min = 1, max = 32, message = "validation.size")
        @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+", message = "validation.pattern")
        private String firstName;

        @NotNull(message = "validation.notnull")
        @Size(min = 1, max = 32, message = "validation.size")
        @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ]{1}[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+", message = "validation.pattern")
        private String lastName;

        @NotNull(message = "validation.notnull")
        @Email(message = "validation.email")
        @Size(min = 1, max = 32, message = "validation.size")
        private String email;

        @NotNull(message = "validation.notnull")
        @Size(min = 1, max = 32, message = "validation.size")
        @Pattern(regexp = "[a-zA-Z0-9!@#$%^*]+", message = "validation.pattern")
        private String username;

        @NotNull(message = "validation.notnull")
        @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "validation.pattern")
        private String password;

        @NotNull(message = "validation.notnull")
        private Set<String> roles = new HashSet<>();

        @NotNull(message = "validation.notnull")
        @Pattern(regexp = "(pl|en)", message = "validation.pattern")
        private String language;
    }
}