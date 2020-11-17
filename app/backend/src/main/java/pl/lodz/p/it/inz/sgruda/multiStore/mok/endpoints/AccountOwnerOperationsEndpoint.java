package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.OwnPasswordChangeService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.CheckerAccountDTO;

import javax.validation.Valid;

@Log
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/accounts/me")
public class AccountOwnerOperationsEndpoint {
    private OwnPasswordChangeService ownPasswordChangeService;
    private CheckerAccountDTO checkerAccountDTO;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AccountOwnerOperationsEndpoint(OwnPasswordChangeService ownPasswordChangeService, CheckerAccountDTO checkerAccountDTO, PasswordEncoder passwordEncoder) {
        this.ownPasswordChangeService = ownPasswordChangeService;
        this.checkerAccountDTO = checkerAccountDTO;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody AccountDTO accountDTO) {
        AccountEntity accountEntity;
        try {
            log.severe("Hello");
            accountEntity = ownPasswordChangeService.getAccountByEmail(accountDTO.getEmail());
            log.severe("wersja entity " + accountEntity.getVersion());
        } catch (AccountNotExistsException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
        try {
            checkerAccountDTO.checkIntegrity(accountEntity, accountDTO);
            ownPasswordChangeService.changePassword(accountEntity, passwordEncoder.encode(accountDTO.getAuthenticationDataDTO().getPassword()));
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.password.change.correctly."));
    }
}
