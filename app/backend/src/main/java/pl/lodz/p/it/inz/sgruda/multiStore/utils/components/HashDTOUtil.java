package pl.lodz.p.it.inz.sgruda.multiStore.utils.components;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.HashMethod;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.HashVerifiability;

import java.util.List;
import java.util.stream.Collectors;

@Log
@Component
public class HashDTOUtil {
    @Value("${app.signature.secret}")
    private String hashSecret;

    public void hashDTO(HashVerifiability dto) {
        dto.setHash(this.hash(dto.specifyHashingParams()));
    }
    public boolean checkHashDTO(HashVerifiability dto) {
        return this.checkHash(dto.getHash(), dto.specifyHashingParams());
    }
    private String hash(List<String> stringsToHashing) {
        String stringToHashing = stringsToHashing.stream()
                .collect(Collectors.joining());
        stringToHashing += this.hashSecret;
        HashMethod hashMethod = new HashMethod();
        return hashMethod.hash(stringToHashing);
    }
    private boolean checkHash(String hash, List<String> hashedStrings) {
        String stringToCheck = hashedStrings.stream()
                .collect(Collectors.joining());
        stringToCheck += this.hashSecret;
        HashMethod hashMethod = new HashMethod();
        return hashMethod.hash(stringToCheck).equals(hash) ? true : false;
    }
}
