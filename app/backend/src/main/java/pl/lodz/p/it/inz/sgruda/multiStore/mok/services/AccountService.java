package pl.lodz.p.it.inz.sgruda.multiStore.mok.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.EmailAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mok.UsernameAlreadyExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.payloads.response.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccessLevelRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories.AccountRepository;
import pl.lodz.p.it.inz.sgruda.multiStore.security.TokenJWTService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;

import java.net.URI;
import java.util.Collections;

@Log
@Service
public class AccountService {
    private @Autowired AuthenticationManager authenticationManager;
    private @Autowired TokenJWTService tokenService;
    private @Autowired AccountRepository accountRepository;
    private @Autowired AccessLevelRepository accessLevelRepository;
    private @Autowired PasswordEncoder passwordEncoder;


    public String authenticateUser(String username, String password) {
         Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return  tokenService.generateToken(authentication);
    }
    public AccountEntity registerAccount(AccountEntity account) throws EmailAlreadyExistsException, UsernameAlreadyExistsException {
        if(accountRepository.existsByUsername(account.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        if(accountRepository.existsByEmail(account.getEmail())) {
            throw new EmailAlreadyExistsException();
        }


        AccessLevelEntity clientRole = accessLevelRepository.findByRoleName(RoleName.ROLE_CLIENT)
                .orElseThrow(() -> new AppException("User Role not set."));

        account.setAccessLevelEntities(Collections.singleton(clientRole));
        return accountRepository.save(account);
    }
}
