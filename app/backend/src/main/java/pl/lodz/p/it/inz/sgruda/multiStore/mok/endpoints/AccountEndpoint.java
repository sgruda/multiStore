package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok.AccountMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.AccountDetailsService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.security.CurrentUser;
import pl.lodz.p.it.inz.sgruda.multiStore.security.UserPrincipal;

@Log
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/accounts")
public class AccountEndpoint {
    private AccountDetailsService accountDetailsService;

    @Autowired
    public AccountEndpoint(AccountDetailsService accountDetailsService) {
        this.accountDetailsService = accountDetailsService;
    }


    @GetMapping("/account")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAccountByUsername(@RequestParam(value = "username") String username) {
        AccountEntity accountEntity;
        try {
            accountEntity = accountDetailsService.getAccount(username);
        } catch (AccountNotExistsException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        AccountMapper accountMapper = new AccountMapper();
        AccountDTO accountDTO = accountMapper.toDTO(accountEntity);
        return ResponseEntity.ok(accountDTO);
    }
    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_EMPLOYEE') or  hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAccount(@CurrentUser UserPrincipal currentUser) {
        AccountEntity accountEntity;
        try {
            accountEntity = accountDetailsService.getAccount(currentUser.getUsername());
        } catch (AccountNotExistsException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        AccountMapper accountMapper = new AccountMapper();
        AccountDTO accountDTO = accountMapper.toDTO(accountEntity);
        return ResponseEntity.ok(accountDTO);
    }
}
