package pl.lodz.p.it.inz.sgruda.multiStore.utils;

import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;

@NoArgsConstructor
public class HashGenerator {
    public String hash(String toHash) {
        return  BCrypt.hashpw(toHash, BCrypt.gensalt());
    }
    public String hash(long toHash) {
        return  BCrypt.hashpw(String.valueOf(toHash), BCrypt.gensalt());
    }
    public String sign(String idHash, String param, long version) {
        return BCrypt.hashpw(idHash + param + version, BCrypt.gensalt());
    }


    public boolean checkHash(String text, String hashText) {
        return BCrypt.checkpw(text, hashText);
    }
    public boolean checkSignature(String signature, String idHash, String param, long version) {
        return BCrypt.checkpw(signature, idHash + param + version);
    }
}
