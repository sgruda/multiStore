package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.CheckerAccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOVersionException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.AccountNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.AccountActivityService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;

import javax.validation.Valid;

@Log
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/accounts")
public class AccountActivityEndpoint {
    private AccountActivityService accountActivityService;
    private CheckerAccountDTO checkerAccountDTO;

    @Autowired
    public AccountActivityEndpoint(AccountActivityService accountActivityService, CheckerAccountDTO checkerAccountDTO) {
        this.accountActivityService = accountActivityService;
        this.checkerAccountDTO = checkerAccountDTO;
    }

    @PostMapping("/account/block")
    public ResponseEntity<?> blockAcocunt(@Valid @RequestBody AccountDTO accountDTO) {
        AccountEntity accountEntity;
        try {
            accountEntity = accountActivityService.getAccountByEmail(accountDTO.getEmail());
        } catch (AccountNotExistsException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            checkerAccountDTO.checkIntegrity(accountEntity, accountDTO);
            accountActivityService.blockAccount(accountEntity);
        } catch (DTOSignatureException | DTOVersionException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.block.correctly."));
    }

    @PostMapping("/account/unblock")
    public ResponseEntity<?> unblockAcocunt(@Valid @RequestBody AccountDTO accountDTO) {
        AccountEntity accountEntity;
        try {
            accountEntity = accountActivityService.getAccountByEmail(accountDTO.getEmail());
        } catch (AccountNotExistsException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            checkerAccountDTO.checkIntegrity(accountEntity, accountDTO);
            accountActivityService.unblockAccount(accountEntity);
        } catch (DTOSignatureException | DTOVersionException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "account.unblock.correctly."));
    }
}
