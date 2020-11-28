package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.AccountActivityService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mok.CheckerAccountDTO;

import javax.validation.Valid;

@Log
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/account")
public class AccountActivityEndpoint {
    private AccountActivityService accountActivityService;
    private CheckerAccountDTO checkerAccountDTO;

    @Autowired
    public AccountActivityEndpoint(AccountActivityService accountActivityService, CheckerAccountDTO checkerAccountDTO) {
        this.accountActivityService = accountActivityService;
        this.checkerAccountDTO = checkerAccountDTO;
    }

    @PutMapping("/block")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> blockAcocunt(@Valid @RequestBody AccountDTO accountDTO) {
        AccountEntity accountEntity;
        try {
            checkerAccountDTO.checkAccountDTOSignature(accountDTO);
            accountEntity = accountActivityService.getAccountByEmail(accountDTO.getEmail());
            checkerAccountDTO.checkAccountDTOVersion(accountEntity, accountDTO);
            accountActivityService.blockAccount(accountEntity);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.block.correctly."));
    }

    @PutMapping("/unblock")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> unblockAcocunt(@Valid @RequestBody AccountDTO accountDTO) {
        AccountEntity accountEntity;
        try {
            checkerAccountDTO.checkAccountDTOSignature(accountDTO);
            accountEntity = accountActivityService.getAccountByEmail(accountDTO.getEmail());
            checkerAccountDTO.checkAccountDTOVersion(accountEntity, accountDTO);
            accountActivityService.unblockAccount(accountEntity);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.unblock.correctly."));
    }
}
