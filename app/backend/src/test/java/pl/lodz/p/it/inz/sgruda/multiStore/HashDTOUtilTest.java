package pl.lodz.p.it.inz.sgruda.multiStore;

import lombok.Data;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.HashDTOUtil;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.HashVerifiability;

import java.util.Arrays;
import java.util.List;

@Log
@SpringBootTest
public class HashDTOUtilTest {
    private @Autowired
    HashDTOUtil hashDTOUtil;

    @Test
    void signingTest() {
        HashMethod hashMethod = new HashMethod();
        TestDTO dto = new TestDTO(5, "jan.kowalski@gmail.com", 0, AuthProvider.system.name());
        String badSignature = "ecddd611a9752fa9482670af8ff7483ade932d89feb7af8d5039b4952ff5093f_BAD";
        hashDTOUtil.hashDTO(dto);

        Assertions.assertEquals(true, hashDTOUtil.checkHashDTO(dto));

        String signature = dto.getHash();
        dto.setHash(badSignature);
        Assertions.assertEquals(false, hashDTOUtil.checkHashDTO(dto));
        dto.setHash(signature);

        dto.setHash(signature + "a");
        Assertions.assertEquals(false, hashDTOUtil.checkHashDTO(dto));
        dto.setHash(signature);

        String idHash = dto.getHash();
        dto.setHash(hashMethod.hash(77));
        Assertions.assertEquals(false, hashDTOUtil.checkHashDTO(dto));
        dto.setHash(idHash);

        String param = dto.getParam();
        dto.setParam(param + "a");
        Assertions.assertEquals(false, hashDTOUtil.checkHashDTO(dto));
        dto.setParam(param);

        long version = dto.getVersion();
        dto.setVersion(version + 1);
        Assertions.assertEquals(false, hashDTOUtil.checkHashDTO(dto));
        dto.setVersion(version);

        String provider = dto.getAuthProvider();
        dto.setAuthProvider(AuthProvider.facebook.name());
        Assertions.assertEquals(false, hashDTOUtil.checkHashDTO(dto));
        dto.setAuthProvider(provider);
    }

    @Data
    private class TestDTO implements HashVerifiability {
        long id;
        String param;
        long version;
        String authProvider;
        String hash;

        public TestDTO(long id, String param, long version, String authProvider) {
            this.id = id;
            this.param = param;
            this.version = version;
            this.authProvider = authProvider;
        }

        @Override
        public List<String> specifyHashingParams() {
            return Arrays.asList(String.valueOf(id), param, authProvider, String.valueOf(version));
        }

    }
}
