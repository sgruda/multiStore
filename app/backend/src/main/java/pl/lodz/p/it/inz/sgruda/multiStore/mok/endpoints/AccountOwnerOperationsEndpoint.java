package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;

import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok.AccountMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.OperationDisabledForAccountException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.OwnAccountEditService;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.OwnPasswordChangeService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.security.CurrentUser;
import pl.lodz.p.it.inz.sgruda.multiStore.security.UserPrincipal;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mok.CheckerAccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.Language;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Log
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/account/me")
public class AccountOwnerOperationsEndpoint {
    private OwnPasswordChangeService ownPasswordChangeService;
    private OwnAccountEditService ownAccountEditService;
    private CheckerAccountDTO checkerAccountDTO;

    @Autowired
    public AccountOwnerOperationsEndpoint(OwnPasswordChangeService ownPasswordChangeService, CheckerAccountDTO checkerAccountDTO, OwnAccountEditService ownAccountEditService) {
        this.ownPasswordChangeService = ownPasswordChangeService;
        this.checkerAccountDTO = checkerAccountDTO;
        this.ownAccountEditService = ownAccountEditService;
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_EMPLOYEE') or  hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request, @CurrentUser UserPrincipal currentUser) {
        AccountEntity accountEntity;
        try {
            checkerAccountDTO.checkAccountDTOHash(request.getAccountDTO());
            if(!currentUser.getEmail().equals(request.getAccountDTO().getEmail())) {
                throw new OperationDisabledForAccountException();
            }
            if(request.getAccountDTO().getAuthenticationDataDTO().getPassword() == null)
                throw new AppBaseException("error.password.can.not.be.null");
            accountEntity = ownPasswordChangeService.getAccountByEmail(request.getAccountDTO().getEmail());
            AccountMapper accountMapper = new AccountMapper();
            AccountEntity entityCopy = accountMapper.createCopyOf(accountEntity, request.getAccountDTO());
            ownPasswordChangeService.changePassword(entityCopy, request.getNewPassword(), request.getAccountDTO().getAuthenticationDataDTO().getPassword());
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.password.change.correctly."));
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_EMPLOYEE') or  hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> editAccount(@Valid @RequestBody AccountDTO accountDTO, @CurrentUser UserPrincipal currentUser) {
        AccountEntity accountEntity;
        try {
            checkerAccountDTO.checkAccountDTOHash(accountDTO);
            if(!currentUser.getEmail().equals(accountDTO.getEmail())) {
                throw new OperationDisabledForAccountException();
            }
            accountEntity = ownAccountEditService.getAccountByEmail(accountDTO.getEmail());
            AccountMapper accountMapper = new AccountMapper();
            AccountEntity entityCopy = accountMapper.createCopyOf(accountEntity, accountDTO);
            accountMapper.updateEntity(entityCopy, accountDTO);
            ownAccountEditService.editAccount(entityCopy);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.edit.correctly."));
    }
    @PutMapping("/change-language")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_EMPLOYEE') or  hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> editLanguageAccount(@Valid @RequestBody AccountDTO accountDTO, @CurrentUser UserPrincipal currentUser) {
        AccountEntity accountEntity;
        try {
            checkerAccountDTO.checkAccountDTOHash(accountDTO);
            if(!currentUser.getEmail().equals(accountDTO.getEmail())) {
                throw new OperationDisabledForAccountException();
            }
            accountEntity = ownAccountEditService.getAccountByEmail(accountDTO.getEmail());
            AccountMapper accountMapper = new AccountMapper();
            AccountEntity entityCopy = accountMapper.createCopyOf(accountEntity, accountDTO);
            entityCopy.setLanguage(Language.valueOf(accountDTO.getLanguage()));
            ownAccountEditService.changeAccountLanguage(entityCopy);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.language.edit.correctly."));
    }
    @Getter
    private static class ChangePasswordRequest {
        @NotNull(message = "{validation.notnull}")
        @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "{validation.pattern}")
        private String newPassword;

        @NotNull(message = "{validation.notnull}")
        @Valid
        private AccountDTO accountDTO;
    }
}
