package pl.lodz.p.it.inz.sgruda.multiStore.security.oauth2.user;

import lombok.extern.java.Log;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.Language;

import java.util.List;
import java.util.Map;
@Log
public class FacebookOAuth2UserInfo extends OAuth2UserInfo {
    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getFirstName() {
        return (String) attributes.get("first_name");
    }

    @Override
    public String getLastName() {
        return (String) attributes.get("last_name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public Language getLanguage() {
        if(attributes.get("languages") != null) {
            String languages = attributes.get("languages").toString();
            if(languages.contains("polish"))
                return Language.pl;
            else
                return Language.en;
        } else
            return Language.pl;
    }
}
