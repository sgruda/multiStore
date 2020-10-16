package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok.AccountMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.payloads.response.UserIdentityAvailability;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.payloads.response.UserSummary;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.security.CurrentUser;
import pl.lodz.p.it.inz.sgruda.multiStore.security.UserPrincipal;

import javax.validation.Valid;

@Log
@RestController
public class UserController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/account")
    public ResponseEntity<?> getAccount(@RequestParam(value = "username") String username) {
        AccountEntity accountEntity = accountRepository.findByUsername(username).get();
        AccountMapper accountMapper = new AccountMapper();
        AccountDTO accountDTO = accountMapper.toDTO(accountEntity);
        return ResponseEntity.ok(accountDTO);
    }

//    @GetMapping("/user/me")
////    @PreAuthorize("hasRole('ROLE_CLIENT')")
//    public AccountDTO getCurrentUser(@CurrentUser UserPrincipal currentUser) {
//        AccountDTO accountDTO = new AccountDTO(currentUser.getFirstName(),
//                );
//        return accountDTO;
//    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !accountRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !accountRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

}