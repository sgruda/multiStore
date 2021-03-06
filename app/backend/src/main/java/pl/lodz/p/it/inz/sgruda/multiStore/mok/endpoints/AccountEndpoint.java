package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok.AccountMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.AccountDetailsService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.security.CurrentUser;
import pl.lodz.p.it.inz.sgruda.multiStore.security.UserPrincipal;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mok.HashAccountDTOUtil;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Log
@Validated
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/account")
public class AccountEndpoint {
    private AccountDetailsService accountDetailsService;
    private HashAccountDTOUtil hashAccountDTOUtil;

    @Autowired
    public AccountEndpoint(AccountDetailsService accountDetailsService, HashAccountDTOUtil hashAccountDTOUtil) {
        this.accountDetailsService = accountDetailsService;
        this.hashAccountDTOUtil = hashAccountDTOUtil;
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAccountByEmail(@Valid   @NotNull(message = "validation.notnull")
                                                        @Email(message = "validation.email")
                                                        @Size(min = 1, max = 32, message = "validation.size")
                                               @RequestParam(value = "email") String email) {
        AccountEntity accountEntity;
        try {
            accountEntity = accountDetailsService.getAccountByEmail(email);
        } catch (AccountNotExistsException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        AccountMapper accountMapper = new AccountMapper();
        AccountDTO accountDTO = accountMapper.toDTO(accountEntity);
        hashAccountDTOUtil.signAccountDTO(accountDTO);
        return ResponseEntity.ok(accountDTO);
    }
    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_EMPLOYEE') or  hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAccountByCurrentUser(@CurrentUser UserPrincipal currentUser) {
        AccountEntity accountEntity;
        try {
            accountEntity = accountDetailsService.getAccountByEmail(currentUser.getEmail());
        } catch (AccountNotExistsException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        AccountMapper accountMapper = new AccountMapper();
        AccountDTO accountDTO = accountMapper.toDTO(accountEntity);
        hashAccountDTOUtil.signAccountDTO(accountDTO);
        return ResponseEntity.ok(accountDTO);
    }
}
