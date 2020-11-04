package pl.lodz.p.it.inz.sgruda.multiStore.utils.components;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.SignatureVerifiability;

import java.util.List;
import java.util.stream.Collectors;

@Log
@Component
public class SignatureDTOUtil {
    @Value("${app.signature.secret}")
    private String signatureSecret;

    public void signDTO(SignatureVerifiability dto) {
        dto.setSignature(this.sign(dto.specifySigningParams()));
    }
    public boolean checkSignatureDTO(SignatureVerifiability dto) {
        return this.checkSignature(dto.getSignature(), dto.specifySigningParams());
    }
    private String sign(List<String> stringsToSigning) {
        String stringToSigning = stringsToSigning.stream()
                .collect(Collectors.joining());
        stringToSigning += this.signatureSecret;
        HashMethod hashMethod = new HashMethod();
        return hashMethod.hash(stringToSigning);
    }
    private boolean checkSignature(String signature, List<String> signedStrings) {
        String stringToCheck = signedStrings.stream()
                .collect(Collectors.joining());
        stringToCheck += this.signatureSecret;
        HashMethod hashMethod = new HashMethod();
        return hashMethod.hash(stringToCheck).equals(signature) ? true : false;
    }
}
