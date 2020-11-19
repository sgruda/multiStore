package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok.AccountMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.OperationDisabledForAccountException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.OwnAccountEditService;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.OwnPasswordChangeService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.CheckerAccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;

import javax.validation.Valid;

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
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AccountOwnerOperationsEndpoint(OwnPasswordChangeService ownPasswordChangeService, CheckerAccountDTO checkerAccountDTO, PasswordEncoder passwordEncoder, OwnAccountEditService ownAccountEditService) {
        this.ownPasswordChangeService = ownPasswordChangeService;
        this.checkerAccountDTO = checkerAccountDTO;
        this.passwordEncoder = passwordEncoder;
        this.ownAccountEditService = ownAccountEditService;
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_EMPLOYEE') or  hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changePassword(@Valid @RequestBody AccountDTO accountDTO) {
        AccountEntity accountEntity;
        try {
            checkerAccountDTO.checkSignature(accountDTO);
            if(accountDTO.getAuthenticationDataDTO().getPassword() == null)
                throw new AppBaseException("error.password.can.not.be.null");
            accountEntity = ownPasswordChangeService.getAccountByEmail(accountDTO.getEmail());
            checkerAccountDTO.checkVersion(accountEntity, accountDTO);
            ownPasswordChangeService.changePassword(accountEntity, passwordEncoder.encode(accountDTO.getAuthenticationDataDTO().getPassword()));
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.password.change.correctly."));
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_EMPLOYEE') or  hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> editAccount(@Valid @RequestBody AccountDTO accountDTO) {
        AccountEntity accountEntity;
        try {
            checkerAccountDTO.checkSignature(accountDTO);
            accountEntity = ownAccountEditService.getAccountByEmail(accountDTO.getEmail());
            checkerAccountDTO.checkVersion(accountEntity, accountDTO);
            AccountMapper accountMapper = new AccountMapper();
            accountMapper.updateEntity(accountEntity, accountDTO);
            ownAccountEditService.editAccount(accountEntity);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.edit.correctly."));
    }
}
