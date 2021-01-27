package pl.lodz.p.it.inz.sgruda.multiStore.entities.mok;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
@ToString
@Getter
@Setter
@Entity
@Table(name = "authentication_data", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"veryfication_token", "username"})
        })
@TableGenerator(name = "AuthenticationDataTokenIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
        valueColumnName = "id_range", pkColumnValue = "authentication_data")
public class AuthenticationDataEntity implements Serializable, VersionGetter {
    @Id
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AuthenticationDataTokenIdGen")
    private long id;

    @Setter(lombok.AccessLevel.NONE)
    @Basic(optional = false)
    @Column(name = "veryfication_token", nullable = false)
    private String veryficationToken;


    @Basic(optional = false)
    @NotNull(message = "validation.notnull")
    @Size(min = 1, max = 32, message = "validation.size")
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^*]+", message = "validation.pattern")
    @Column(name = "username", nullable = false, unique = true, length = 32, updatable = false)
    private String username;

    @Basic(optional = false)
    @NotNull(message = "validation.notnull")
    @Size(min = 60, max = 60, message = "validation.size")
    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Basic(optional = false)
    @NotNull(message = "validation.notnull")
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "forgot_password_token_id", referencedColumnName = "id")
    private ForgotPasswordTokenEntity forgotPasswordTokenEntity;

    @Setter(lombok.AccessLevel.NONE)
    @Basic(optional = false)
    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public AuthenticationDataEntity(String username, String password) {
        this.username = username;
        this.password = password;
        this.veryficationToken = UUID.randomUUID().toString();
        this.emailVerified = false;
        this.forgotPasswordTokenEntity = null;
    }

    public AuthenticationDataEntity() {
        this.veryficationToken = UUID.randomUUID().toString();
        this.emailVerified = false;
        this.forgotPasswordTokenEntity = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationDataEntity that = (AuthenticationDataEntity) o;
        return emailVerified == that.emailVerified &&
                veryficationToken.equals(that.veryficationToken) &&
                username.equals(that.username) &&
                password.equals(that.password) &&
                Objects.equals(forgotPasswordTokenEntity, that.forgotPasswordTokenEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(veryficationToken, username, password, emailVerified, forgotPasswordTokenEntity);
    }

}