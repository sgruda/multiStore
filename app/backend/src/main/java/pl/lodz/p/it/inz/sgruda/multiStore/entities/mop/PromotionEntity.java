package pl.lodz.p.it.inz.sgruda.multiStore.entities.mop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Entity
@Table(name = "promotion", schema = "public", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
@TableGenerator(name = "PromotionIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
        valueColumnName = "id_range", pkColumnValue = "category")
public class PromotionEntity implements Serializable, VersionGetter {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PromotionIdGen")
    private long id;

    @Basic(optional = false)
    @NotNull(message = "validation.notnull")
    @Size(min = 1, max = 32, message = "validation.size")
    @Pattern(regexp = "[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ ]+", message = "validation.pattern")
    @Column(name = "name", nullable = false, length = 32, unique = true)
    private String name;

    @Digits(integer = 2, fraction = 2, message = "validation.digits")
    @Basic(optional = false)
    @NotNull(message = "validation.notnull")
    @Column(name = "discount", nullable = false)
    private double discount;

    @NotNull(message = "validation.notnull")
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CategoryEntity categoryEntity;

    @Basic(optional = false)
    @NotNull(message = "validation.notnull")
    @Column(name = "active", nullable = false)
    private boolean active;

    @Basic(optional = false)
    @Future(message = "validation.date.future")
    @NotNull(message = "validation.notnull")
    @Column(name = "expire_date", nullable = false)
    private LocalDateTime expireDate;

    @Version
    @Basic
    @Column(name = "version", nullable = false)
    private long version;

    public PromotionEntity() {
    }

    public PromotionEntity(String name, double discount, boolean active, LocalDateTime expireDate) {
        this.name = name;
        this.discount = discount;
        this.active = active;
        this.expireDate = expireDate;
    }
}
