package pl.lodz.p.it.inz.sgruda.multiStore;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AuthenticationDataDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOHashException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mok.CheckerAccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.mok.HashAccountDTOUtil;

import java.util.Arrays;;
import java.util.HashSet;

@Log
@SpringBootTest
public class AccountDTOUtilTest {
    private @Autowired CheckerAccountDTO checkerAccountDTO;
    private @Autowired HashAccountDTOUtil hashAccountDTOUtil;

    @Test
    void test() {
        AccountDTO accountDTO = new AccountDTO();
        final String EMAIL = "jan.kowalski@gmail.com";
        accountDTO.setId(123);
        accountDTO.setFirstName("Jan");
        accountDTO.setLastName("Kowalski");
        accountDTO.setEmail(EMAIL);
        accountDTO.setRoles(new HashSet<>(Arrays.asList("CLIENT_ROLE")));
        accountDTO.setAuthProvider("google");
        accountDTO.setVersion(0);
        hashAccountDTOUtil.signAccountDTO(accountDTO);


        boolean catched = false;
        accountDTO.setVersion(1);
        try {
            checkerAccountDTO.checkAccountDTOHash(accountDTO);
        } catch (DTOHashException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
        accountDTO.setVersion(0);

        catched = false;
        accountDTO.setEmail("kowal.s@gmail.com");
        try {
            checkerAccountDTO.checkAccountDTOHash(accountDTO);
        } catch (DTOHashException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
        accountDTO.setEmail(EMAIL);

        accountDTO.setAuthProvider("system");
        AuthenticationDataDTO authenticationDataDTO = new AuthenticationDataDTO();
        authenticationDataDTO.setId(321);
        authenticationDataDTO.setUsername("kowal");
        authenticationDataDTO.setEmailVerified(true);
        authenticationDataDTO.setVersion(0);
        accountDTO.setAuthenticationDataDTO(authenticationDataDTO);
        hashAccountDTOUtil.signAccountDTO(accountDTO);

        accountDTO.getAuthenticationDataDTO().setVersion(1);
        try {
            checkerAccountDTO.checkAccountDTOHash(accountDTO);
        } catch (DTOHashException e) {
            catched = true;
        }
        Assertions.assertEquals(true, catched);
        accountDTO.getAuthenticationDataDTO().setVersion(0);
    }
}
