package pl.lodz.p.it.inz.sgruda.multiStore.entities.mok;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
@ToString
@Getter
@Setter
@Entity
@Table(name = "forgot_password_token", schema = "public")
@TableGenerator(name = "ForgotPasswordTokenIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
                valueColumnName = "id_range", pkColumnValue = "forgot_password_token")
public class ForgotPasswordTokenEntity implements Serializable, VersionGetter {
    @Id
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ForgotPasswordTokenIdGen")
    private long id;

    @Basic(optional = false)
    @Future(message = "validation.date.future")
    @NotNull(message = "validation.notnull")
    @Column(name = "expire_date", nullable = false)
    private LocalDateTime expireDate;

    @Basic(optional = false)
    @NotNull(message = "validation.notnull")
    @Size(min = 6, max = 6, message = "validation.size")
    @Column(name = "token", nullable = false, unique = true, length = 64)
    private String token;


    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, unique = true)
    @OneToOne(optional = false)
    private AccountEntity accountEntity;

    @Setter(lombok.AccessLevel.NONE)
    @Basic(optional = false)
    @Version
    @Column(name = "version", nullable = false)
    private long version;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForgotPasswordTokenEntity that = (ForgotPasswordTokenEntity) o;
        return  Objects.equals(expireDate, that.expireDate) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expireDate, token, version);
    }

}
