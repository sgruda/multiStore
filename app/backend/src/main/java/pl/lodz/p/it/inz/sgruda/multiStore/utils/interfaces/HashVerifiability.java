package pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces;

import java.util.List;

public interface HashVerifiability extends VersionGetter {
    List<String> specifyHashingParams();
    String getHash();
    void setHash(String hash);
}
