package pl.lodz.p.it.inz.sgruda.multiStore;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;

import java.util.Arrays;
import java.util.HashMap;


@Log
@SpringBootTest
public class HashMethodTest {
    @Test
    void hashingTest() {
        HashMethod hashMethod = new HashMethod();
        long toHash = 5;
        String idHash = hashMethod.hash(toHash);
        String email = "jan.kowalski@gmail.com";
        String hashEmail = hashMethod.hash(email);
        long toHash2 = 7;
        String idHash2 = hashMethod.hash(toHash2);
        String email2 = "kazimierz.wielki@gmail.com";
        String hashEmail2 = hashMethod.hash(email2);

        class Checker {
            public boolean checkHash(long plain, String hash) {
                return hashMethod.hash(plain).equals(hash) ? true : false;
            }
            public boolean checkHash(String plain, String hash) {
                return hashMethod.hash(plain).equals(hash) ? true : false;
            }
        }
        Checker checker = new Checker();

        Assertions.assertEquals(true, checker.checkHash(toHash, idHash));
        Assertions.assertEquals(false, checker.checkHash(toHash2, idHash));
        Assertions.assertEquals(false, checker.checkHash(toHash, idHash2));
        Assertions.assertEquals(false, checker.checkHash(toHash, idHash + "a"));
        Assertions.assertEquals(false, checker.checkHash(toHash + 1, idHash));
        Assertions.assertEquals(true, checker.checkHash(email, hashEmail));
        Assertions.assertEquals(false, checker.checkHash(email2, hashEmail));
        Assertions.assertEquals(false, checker.checkHash(email, hashEmail2));
        Assertions.assertEquals(false, checker.checkHash(email, hashEmail + "a"));
        Assertions.assertEquals(false, checker.checkHash(email + "a", hashEmail));
    }
}
