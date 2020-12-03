package pl.lodz.p.it.inz.sgruda.multiStore.entities.moz;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@ToString
@Getter
@Setter
@Entity
@Table(name = "ordered_item", schema = "public")
@TableGenerator(name = "OrderedItemIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
        valueColumnName = "id_range", pkColumnValue = "ordered_item")
public class OrderedItemEntity implements Serializable, VersionGetter {
    @Id
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "OrderedItemIdGen")
    private long id;

    @Basic(optional = false)
    @NotNull(message = "validation.notnull")
    @Size(min = 36, max = 36, message = "validation.size")
    @Pattern(regexp = "[0-9A-Za-z-]+", message = "validation.pattern")
    @Column(name = "identifier", nullable = false, length = 32)
    private String identifier;

    @Digits(integer = 7, fraction = 0, message = "validation.digits")
    @Basic(optional = false)
    @NotNull(message = "validation.notnull")
    @Column(name = "ordered_number", nullable = false)
    private int orderedNumber;

    @NotNull(message = "validation.notnull")
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProductEntity productEntity;

    @Version
    @Setter(lombok.AccessLevel.NONE)
    @Basic
    @Column(name = "version", nullable = false)
    private long version;

    public OrderedItemEntity() {
        this.identifier = UUID.randomUUID().toString();
    }
}
