package pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces;

import java.util.List;

public interface SignatureVerifiability extends VersionGetter {
    List<String> specifySigningParams();
    String getSignature();
    void setSignature(String signature);
}
