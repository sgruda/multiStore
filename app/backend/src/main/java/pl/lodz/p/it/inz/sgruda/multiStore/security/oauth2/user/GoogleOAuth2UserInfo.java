package pl.lodz.p.it.inz.sgruda.multiStore.security.oauth2.user;

import lombok.extern.java.Log;

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
    public String getFirstname() {
        log.severe("WTF attributes.get(name ) = " + attributes.get("name").toString().split(" "));
        log.severe("WTF attributes.get(profile ) = " + attributes.get("profile"));
        return (String) attributes.get("firstname");
    }
    @Override
    public String getLastname() {
        return (String) attributes.get("lastname");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}
