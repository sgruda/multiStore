package pl.lodz.p.it.inz.sgruda.multiStore;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AuthenticationDataDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOSignatureException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mok.CheckerAccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mok.SignAccountDTOUtil;

import java.util.Arrays;;
import java.util.HashSet;

@Log
@SpringBootTest
public class AccountDTOUtilTest {
    private @Autowired CheckerAccountDTO checkerAccountDTO;
    private @Autowired SignAccountDTOUtil signAccountDTOUtil;

    @Test
    void test() {
        AccountDTO accountDTO = new AccountDTO();
        final String EMAIL = "jan.kowalski@gmail.com";
        accountDTO.setIdHash("123");
        accountDTO.setFirstName("Jan");
        accountDTO.setLastName("Kowalski");
        accountDTO.setEmail(EMAIL);
        accountDTO.setRoles(new HashSet<>(Arrays.asList("CLIENT_ROLE")));
        accountDTO.setAuthProvider("google");
        accountDTO.setVersion(0);
        signAccountDTOUtil.signAccountDTO(accountDTO);


        boolean catched = false;
        accountDTO.setVersion(1);
        try {
            checkerAccountDTO.checkAccountDTOSignature(accountDTO);
        } catch (DTOSignatureException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
        accountDTO.setVersion(0);

        catched = false;
        accountDTO.setEmail("kowal.s@gmail.com");
        try {
            checkerAccountDTO.checkAccountDTOSignature(accountDTO);
        } catch (DTOSignatureException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
        accountDTO.setEmail(EMAIL);

        accountDTO.setAuthProvider("system");
        AuthenticationDataDTO authenticationDataDTO = new AuthenticationDataDTO();
        authenticationDataDTO.setIdHash("321");
        authenticationDataDTO.setUsername("kowal");
        authenticationDataDTO.setEmailVerified(true);
        authenticationDataDTO.setVersion(0);
        accountDTO.setAuthenticationDataDTO(authenticationDataDTO);
        signAccountDTOUtil.signAccountDTO(accountDTO);

        accountDTO.getAuthenticationDataDTO().setVersion(1);
        try {
            checkerAccountDTO.checkAccountDTOSignature(accountDTO);
        } catch (DTOSignatureException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
        accountDTO.getAuthenticationDataDTO().setVersion(0);

        catched = false;
        accountDTO.getAuthenticationDataDTO().setIdHash(null);
        try {
            checkerAccountDTO.checkAccountDTOSignature(accountDTO);
        } catch (DTOSignatureException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
    }
}
