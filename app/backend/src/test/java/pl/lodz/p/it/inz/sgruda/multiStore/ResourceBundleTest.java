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
       Assertions.assertEquals("Oto twoj token do resetu hasla w systemie.", resourceBundle.getProperty("pl.mail.account.reset.password.body"));
        Assertions.assertEquals("Reset hasla.", resourceBundle.getProperty("pl.mail.account.reset.password.subject"));
        Assertions.assertEquals("Your address e-mail has been used, during registration in system, to confirm that is your e-mail. Click link.", resourceBundle.getProperty("en.mail.account.confirm.body"));
        Assertions.assertEquals("Account registration in system.", resourceBundle.getProperty("en.mail.account.confirm.subject"));
    }
}
