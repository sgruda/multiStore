package pl.lodz.p.it.inz.sgruda.multiStore.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.*;


@Getter
@Setter
@Entity
@Table(name = "account_data", schema = "public",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "veryfication_token"})
})
@TableGenerator(name = "AccountIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
               valueColumnName = "id_range", pkColumnValue = "account_login_data")
@SecondaryTables({
        @SecondaryTable(name = "account_login_data", schema = "public", uniqueConstraints = {
                @UniqueConstraint(columnNames = {"username"})
                })
})
public class AccountEntity {

    @Id
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AccountIdGen")
    private long id;

    @Basic
    @Column(name = "firstname", nullable = false, length = 32)
    private String firstname;

    @Basic
    @Column(name = "lastname", nullable = false, length = 32)
    private String lastname;

    @Basic
    @Email
    @Column(name = "email", nullable = false, length = 32)
    private String email;

    @Setter(lombok.AccessLevel.NONE)
    @Basic
    @Column(name = "veryfication_token", nullable = true, length = 32)
    private String veryficationToken;


    @Basic
    @Column(table = "account_login_data", name = "username", nullable = false, length = 32)
    private String username;

    @JsonIgnore
    @Basic
    @Column(table = "account_login_data", name = "password", nullable = false, length = 64)
    private String password;

    @Basic
    @Column(table = "account_login_data", name = "active", nullable = false)
    private boolean active;

    @Basic
    @Column(table = "account_login_data", name = "confirmed", nullable = false)
    private boolean confirmed;

    @Getter(lombok.AccessLevel.NONE)
    @Setter(lombok.AccessLevel.NONE)
    @Basic
    @Column(table = "account_login_data", name = "version", nullable = false)
    private long version;


//    @OneToMany(mappedBy = "accountEntity")
//    private Collection<AccessLevelEntity> accessLevelEntityCollectionccessLevelEntity = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "account_access_level_mapping",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "access_level_id"))
    private Set<AccessLevelEntity> accessLevelEntities = new HashSet<>();


    @OneToOne(mappedBy = "accountEntity")
    private ForgotPasswordTokenEntity forgotPasswordTokenEntity;


    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return id == that.id &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(email, that.email) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(active, that.active) &&
                Objects.equals(confirmed, that.confirmed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, veryficationToken, username, password, active, confirmed);
    }
}
