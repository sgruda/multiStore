package pl.lodz.p.it.inz.sgruda.multiStore;

import lombok.Data;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignatureDTOUtil;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

import java.util.Arrays;
import java.util.List;

@Log
@SpringBootTest
public class SignatureDTOUtilTest {
    private @Autowired
    SignatureDTOUtil signatureDTOUtil;

    @Test
    void signingTest() {
        HashMethod hashMethod = new HashMethod();
        TestDTO dto = new TestDTO(hashMethod.hash(5), "jan.kowalski@gmail.com", 0);
        String badSignature = "ecddd611a9752fa9482670af8ff7483ade932d89feb7af8d5039b4952ff5093f_BAD";
        signatureDTOUtil.signDTO(dto);

        Assertions.assertEquals(true, signatureDTOUtil.checkSignatureDTO(dto));

        String signature = dto.getSignature();
        dto.setSignature(badSignature);
        Assertions.assertEquals(false, signatureDTOUtil.checkSignatureDTO(dto));
        dto.setSignature(signature);

        dto.setSignature(signature + "a");
        Assertions.assertEquals(false, signatureDTOUtil.checkSignatureDTO(dto));
        dto.setSignature(signature);

        String idHash = dto.getIdHash();
        dto.setIdHash(hashMethod.hash(77));
        Assertions.assertEquals(false, signatureDTOUtil.checkSignatureDTO(dto));
        dto.setIdHash(idHash);

        String param = dto.getParam();
        dto.setParam(param + "a");
        Assertions.assertEquals(false, signatureDTOUtil.checkSignatureDTO(dto));
        dto.setParam(param);

        long version = dto.getVersion();
        dto.setVersion(version + 1);
        Assertions.assertEquals(false, signatureDTOUtil.checkSignatureDTO(dto));
        dto.setVersion(version);
    }

    @Data
    private class TestDTO implements SignatureVerifiability {
        String idHash;
        String param;
        long version;
        String signature;

        public TestDTO(String idHash, String param, long version) {
            this.idHash = idHash;
            this.param = param;
            this.version = version;
        }

        @Override
        public List<String> specifySigningParams() {
            return Arrays.asList(idHash, param, String.valueOf(version));
        }

    }
}
