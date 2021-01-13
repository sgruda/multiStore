package pl.lodz.p.it.inz.sgruda.multiStore;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.ResourceBundle;

@Log
@SpringBootTest
public class ResourceBundleTest {
    @Test
    void loadingTest() {
       ResourceBundle resourceBundle = new ResourceBundle();
       Assertions.assertEquals("Oto twoj token do resetu hasla w systemie.", resourceBundle.getProperty("pl.reset.password.body"));
        Assertions.assertEquals("Reset hasła.", resourceBundle.getProperty("pl.reset.password.subject"));
        Assertions.assertEquals("Your address e-mail has been used, during registration in system, to confirm that is your e-mail. Click link.", resourceBundle.getProperty("en.confirm.account.body"));
        Assertions.assertEquals("Account registration in system.", resourceBundle.getProperty("en.confirm.account.subject"));
    }
}
