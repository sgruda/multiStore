package pl.lodz.p.it.inz.sgruda.multiStore.entities.moz;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ToString
@Getter
@Setter
@Entity
@Table(name = "ordered_items", schema = "public")
@TableGenerator(name = "OrderedItemsIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
        valueColumnName = "id_range", pkColumnValue = "ordered_items")
public class OrderedItemsEntity implements Serializable, VersionGetter {
    @Id
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "OrderedItemsIdGen")
    private long id;

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
}
