package pl.lodz.p.it.inz.sgruda.multiStore.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class HashGenerator {
    public static String hash(String toHash) {
        return  BCrypt.hashpw(toHash, BCrypt.gensalt());
    }
    public static String hash(long toHash) {
        return  BCrypt.hashpw(String.valueOf(toHash), BCrypt.gensalt());
    }
    public static String sign(String param1, String param2, long version) {
        return BCrypt.hashpw(param1 + param2 + version, BCrypt.gensalt());
    }


    public static boolean checkPasswords(String password, String passwordHash) {
        return BCrypt.checkpw(password, passwordHash);
    }
    public boolean checkSignature(String signature, String param1, String param2, long version) {
        return BCrypt.checkpw(signature, param1 + param2 + version);
    }
}
