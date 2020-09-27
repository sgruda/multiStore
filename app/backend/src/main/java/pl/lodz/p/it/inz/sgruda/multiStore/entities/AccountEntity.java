package pl.lodz.p.it.inz.sgruda.multiStore.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "account_data", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email"})
        })
@TableGenerator(name = "AccountIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
        valueColumnName = "id_range", pkColumnValue = "account_data")
public class AccountEntity implements Serializable {
    @Id
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AccountIdGen")
    private long id;

    @Basic
    @Column(name = "first_name", nullable = false, length = 32)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false, length = 32)
    private String lastName;

    @Basic
    @Email
    @Column(name = "email", nullable = false, unique = true, length = 32)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_access_level_mapping",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "access_level_id"))
    private Set<AccessLevelEntity> accessLevelEntities = new HashSet<>();


    @Basic
    @Column(name = "active", nullable = false)
    private boolean active;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Basic
    @Column(name = "provider_id", nullable = true)
    private String providerId;

    @Setter(lombok.AccessLevel.NONE)
    @Basic
    @Column(name = "version", nullable = false)
    private long version;

    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "authentication_data_id", referencedColumnName = "id")
    private AuthenticationDataEntity authenticationDataEntity;


    public AccountEntity(String firstName, String lastName, @Email String email, AuthProvider provider, String providerId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = true;
        this.provider = provider;
        this.providerId = providerId;
    }

    public AccountEntity(String firstName, String lastName, @Email String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = true;
        this.authenticationDataEntity = new AuthenticationDataEntity(username, password);
        this.provider = AuthProvider.system;
    }

    public AccountEntity() {
        this.authenticationDataEntity = new AuthenticationDataEntity();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return  firstName.equals(that.firstName) &&
                lastName.equals(that.lastName) &&
                email.equals(that.email) &&
                accessLevelEntities.equals(that.accessLevelEntities) &&
                provider == that.provider &&
                active == that.active &&
                authenticationDataEntity.equals(that.authenticationDataEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, accessLevelEntities, provider, active, authenticationDataEntity);
    }

    public String getUsername() {
        if(this.authenticationDataEntity != null)
            return this.authenticationDataEntity.getUsername();
        return null;
    }
    public void setUsername(String username) {
        if(this.authenticationDataEntity != null)
            this.authenticationDataEntity.setUsername(username);
    }
    public String getPassword() {
        if(this.authenticationDataEntity != null)
            return this.authenticationDataEntity.getPassword();
        return null;
    }
    public void setPassword(String password) {
        if(this.authenticationDataEntity != null)
            this.authenticationDataEntity.setPassword(password);
    }
    public String getVeryficationToken() {
        if(this.authenticationDataEntity != null)
            return this.authenticationDataEntity.getVeryficationToken();
        return null;
    }
    public boolean isEmailVerified() {
        if(this.authenticationDataEntity != null)
            return this.authenticationDataEntity.isEmailVerified();
        return false;
    }
    public void setEmailVerified(boolean emailVerified) {
        if(this.authenticationDataEntity != null)
            this.authenticationDataEntity.setEmailVerified(emailVerified);
    }
    public ForgotPasswordTokenEntity getForgotPasswordTokenEntity() {
        if(this.authenticationDataEntity != null)
            return this.authenticationDataEntity.getForgotPasswordTokenEntity();
        return null;
    }
}
