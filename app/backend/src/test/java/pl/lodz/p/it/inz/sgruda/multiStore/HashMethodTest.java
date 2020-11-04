package pl.lodz.p.it.inz.sgruda.multiStore;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashGenerator;

import java.util.Arrays;


@Log
@SpringBootTest
public class HashGeneratorTest {
    @Test
    void signingTest() {
        HashGenerator hashGenerator = new HashGenerator();
        long toHash = 5;
        String idHash = hashGenerator.hash(toHash);
        String param = "jan.kowalski@gmail.com";
        long version = 0;
        String signature = hashGenerator.sign(Arrays.asList(idHash, param, String.valueOf(version)));
        long toHash2 = 7;
        String idHash2 = hashGenerator.hash(toHash2);
        String param2 = "kazimierz.wielki@gmail.com";
        long version2 = 2;
        String signature2 = hashGenerator.sign(Arrays.asList(idHash2, param2, String.valueOf(version2)));


        Assertions.assertEquals(true, hashGenerator.checkHash(String.valueOf(toHash), idHash));
        Assertions.assertEquals(false, hashGenerator.checkHash(String.valueOf(toHash2), idHash));
        Assertions.assertEquals(false, hashGenerator.checkHash(String.valueOf(toHash), idHash2));
        Assertions.assertEquals(false, hashGenerator.checkHash(String.valueOf(toHash), idHash + "a"));
        Assertions.assertEquals(false, hashGenerator.checkHash(String.valueOf(toHash + 1), idHash));

        Assertions.assertEquals(true, hashGenerator.checkSignature(signature, Arrays.asList(idHash, param, String.valueOf(version))));
        Assertions.assertEquals(false, hashGenerator.checkSignature(signature2, Arrays.asList(idHash, param, String.valueOf(version))));
        Assertions.assertEquals(false, hashGenerator.checkSignature(signature, Arrays.asList(idHash2, param, String.valueOf(version))));
        Assertions.assertEquals(false, hashGenerator.checkSignature(signature, Arrays.asList(idHash, param2, String.valueOf(version))));
        Assertions.assertEquals(false, hashGenerator.checkSignature(signature, Arrays.asList(idHash, param, String.valueOf(version2))));
        Assertions.assertEquals(false, hashGenerator.checkSignature(signature + "a", Arrays.asList(idHash, param, String.valueOf(version))));
        Assertions.assertEquals(false, hashGenerator.checkSignature(signature, Arrays.asList(idHash + "a", param, String.valueOf(version))));
        Assertions.assertEquals(false, hashGenerator.checkSignature(signature, Arrays.asList(idHash, param + "a", String.valueOf(version))));
        Assertions.assertEquals(false, hashGenerator.checkSignature(signature, Arrays.asList(idHash, param, String.valueOf(version + 1))));
    }
}
