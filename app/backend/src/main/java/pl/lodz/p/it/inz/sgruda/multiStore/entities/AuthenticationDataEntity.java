package pl.lodz.p.it.inz.sgruda.multiStore.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "authentication_data", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"veryfication_token", "username"})
        })
@TableGenerator(name = "AuthenticationDataTokenIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
        valueColumnName = "id_range", pkColumnValue = "authentication_data")
public class AuthenticationDataEntity implements Serializable {
    @Id
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AuthenticationDataTokenIdGen")
    private long id;

    @Setter(lombok.AccessLevel.NONE)
    @Basic
    @Column(name = "veryfication_token", nullable = false)
    private String veryficationToken;

    @Basic
    @Column(name = "username", nullable = false, unique = true, length = 32)
    private String username;

    @Basic
    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Basic
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "forgot_password_token_id", referencedColumnName = "id")
    private ForgotPasswordTokenEntity forgotPasswordTokenEntity;

    @Getter(lombok.AccessLevel.NONE)
    @Setter(lombok.AccessLevel.NONE)
    @Basic(optional = false)
    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public AuthenticationDataEntity(String username, String password) {
        this.username = username;
        this.password = password;
        this.veryficationToken = UUID.randomUUID().toString();
    }

    public AuthenticationDataEntity() {
        this.veryficationToken = UUID.randomUUID().toString();
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