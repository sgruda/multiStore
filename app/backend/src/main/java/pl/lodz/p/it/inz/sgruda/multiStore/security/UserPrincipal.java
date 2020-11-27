package pl.lodz.p.it.inz.sgruda.multiStore.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class UserPrincipal implements OAuth2User, UserDetails {
    @Getter
    private Long id;
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String email;
    private String username;
    private String password;
    @Getter
    private boolean active;
    @Getter
    private boolean emailVerified;
    @Setter @Getter
    private Collection<? extends GrantedAuthority> authorities;
    @Setter
    private Map<String, Object> attributes;
    @Getter
    private AuthProvider authProvider;
    @Getter
    private String providerId;


    public UserPrincipal(Long id, String firstName, String lastName, String email, String username, String password, boolean active,
                         boolean emailVerified, AuthProvider authProvider, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.active = active;
        this.emailVerified = emailVerified;
        this.authProvider = authProvider;
        this.authorities = authorities;
    }

    public UserPrincipal(Long id, String firstName, String lastName, String email, boolean active, AuthProvider authProvider,
                         String providerId, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
        this.emailVerified = true;
        this.authProvider = authProvider;
        this.providerId = providerId;
        this.authorities = authorities;
    }

    public static UserPrincipal create(AccountEntity account) {
        List<GrantedAuthority> authorities = account.getAccessLevelEntities().stream().map(role ->
                new SimpleGrantedAuthority(role.getRoleName().name())
        ).collect(Collectors.toList());

        if(account.getProvider().equals(AuthProvider.system))
            return new UserPrincipal(
                    account.getId(),
                    account.getFirstName(),
                    account.getLastName(),
                    account.getEmail(),
                    account.getUsername(),
                    account.getPassword(),
                    account.isActive(),
                    account.isEmailVerified(),
                    account.getProvider(),
                    authorities
            );
        else
            return new UserPrincipal(
                    account.getId(),
                    account.getFirstName(),
                    account.getLastName(),
                    account.getEmail(),
                    account.isActive(),
                    account.getProvider(),
                    account.getProviderId(),
                    authorities

            );
    }

    public static UserPrincipal create(AccountEntity user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        if(this.authProvider.equals(AuthProvider.system))
            return username;
        else
            return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return emailVerified;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}