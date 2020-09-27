package pl.lodz.p.it.inz.sgruda.multiStore.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "forgot_password_token", schema = "public")
@TableGenerator(name = "ForgotPasswordTokenIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
                valueColumnName = "id_range", pkColumnValue = "forgot_password_token")
public class ForgotPasswordTokenEntity {
    @Id
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ForgotPasswordTokenIdGen")
    private long id;

    @Basic
    @Column(name = "expire_date", nullable = false)
    private LocalDateTime expireDate;

    @Basic
    @Column(name = "hash", nullable = false, unique = true, updatable = false, length = 64)
    private String hash;


    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, unique = true)
    @OneToOne(optional = false)
    private AccountEntity accountEntity;

    @Getter(lombok.AccessLevel.NONE)
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
                Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expireDate, hash, version);
    }

}
