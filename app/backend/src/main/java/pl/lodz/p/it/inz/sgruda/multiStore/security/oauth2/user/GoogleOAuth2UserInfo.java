package pl.lodz.p.it.inz.sgruda.multiStore.security.oauth2.user;

import lombok.extern.java.Log;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.Language;

import java.util.Map;

@Log
public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getFirstName() {
        return (String) attributes.get("given_name");
    }

    @Override
    public String getLastName() {
        return (String) attributes.get("family_name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public Language getLanguage() {
        String languages = attributes.get("locale").toString();
        if(languages.contains("pl"))
            return Language.pl;
        else
            return Language.en;
    }
}
