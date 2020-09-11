package pl.lodz.p.it.inz.sgruda.multiStore.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


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
                @UniqueConstraint(columnNames = {"login"})
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
    @Column(name = "email", nullable = false, length = 32)
    private String email;

    @Setter(lombok.AccessLevel.NONE)
    @Basic
    @Column(name = "veryfication_token", nullable = true, length = 32)
    private String veryficationToken;


    @Basic
    @Column(table = "account_login_data", name = "login", nullable = false, length = 32)
    private String login;

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


    @OneToMany(mappedBy = "accountEntity")
    private Collection<AccessLevelEntity> accessLevelEntityCollectionccessLevelEntity = new ArrayList<>();

    @OneToOne(mappedBy = "accountEntity")
    private ForgotPasswordTokenEntity forgotPasswordTokenEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return id == that.id &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(email, that.email) &&
                Objects.equals(login, that.login) &&
                Objects.equals(password, that.password) &&
                Objects.equals(active, that.active) &&
                Objects.equals(confirmed, that.confirmed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, veryficationToken, login, password, active, confirmed);
    }
}
