package pl.lodz.p.it.inz.sgruda.multiStore.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;

import java.util.*;
import java.util.stream.Collectors;

//@Getter
//public class UserPrincipal implements OAuth2User, UserDetails {
//    @Getter
//    private Long id;
////    private String name;
//    private String username;
////    private String email;
//    private String password;
//    @Getter @Setter
//    private Collection<? extends GrantedAuthority> authorities;
//    @Setter @Getter
//    private Map<String, Object> attributes;

//    public UserPrincipal(Long id, String name, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
//        this.id = id;
////        this.name = name;
//        this.username = username;
////        this.email = email;
//        this.password = password;
//        this.authorities = authorities;
//    }
//public UserPrincipal(Long id, String username, String password,  Collection<? extends GrantedAuthority> authorities) {
//    this.id = id;
//    this.username = username;
//    this.password = password;
//    this.authorities = authorities;
//    }
//    @Override
//    public String getName() {
//        return String.valueOf(id);
//    }
//
//    public static UserPrincipal create(AccountEntity account) {
//        List<GrantedAuthority> authorities = account.getAccessLevelEntities().stream().map(role ->
//                new SimpleGrantedAuthority(role.getRoleName().name())
//        ).collect(Collectors.toList());
//
////        return new UserPrincipal(
////                account.getId(),
////                account.getName(),
////                account.getUsername(),
////                account.getEmail(),
////                account.getPassword(),
////                authorities
////        );
//        return new UserPrincipal(
//                account.getId(),
//                account.getUsername(),
//                account.getPassword(),
//                authorities
//        );
//    }
//    public static UserPrincipal create(AccountEntity account, Map<String, Object> attributes) {
//        UserPrincipal userPrincipal = UserPrincipal.create(account);
//        userPrincipal.setAttributes(attributes);
//        return userPrincipal;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        UserPrincipal that = (UserPrincipal) o;
//        return Objects.equals(id, that.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//}

public class UserPrincipal implements OAuth2User, UserDetails {
    private Long id;
    private String email;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(Long id, String email, String password,String username, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.authorities = authorities;
    }

    public static UserPrincipal create(AccountEntity account) {
        List<GrantedAuthority> authorities = account.getAccessLevelEntities().stream().map(role ->
                new SimpleGrantedAuthority(role.getRoleName().name())
        ).collect(Collectors.toList());

        return new UserPrincipal(
                account.getId(),
                account.getEmail(),
                account.getPassword(),
                account.getUsername(),
                authorities
        );
    }

    public static UserPrincipal create(AccountEntity user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}