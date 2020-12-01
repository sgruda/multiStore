package pl.lodz.p.it.inz.sgruda.multiStore.entities.moz;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@ToString
@Getter
@Setter
@Entity
@Table(name = "order", schema = "public")
@TableGenerator(name = "OrderIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
        valueColumnName = "id_range", pkColumnValue = "order")
public class OrderEntity implements Serializable, VersionGetter {
    @Id
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "OrderIdGen")
    private long id;

    @Basic(optional = false)
    @NotNull(message = "validation.notnull")
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @NotNull(message = "validation.notnull")
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private AccountEntity accountEntity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ordered_items_order_mapping",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "ordered_items_id"))
    private Set<OrderedItemsEntity> orderedItemsEntities = new HashSet<>();

    @Digits(integer = 7, fraction = 2, message = "validation.digits")
    @Basic(optional = false)
    @NotNull(message = "validation.notnull")
    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @NotNull(message = "validation.notnull")
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private StatusEntity statusEntity;

    @Version
    @Setter(lombok.AccessLevel.NONE)
    @Basic
    @Column(name = "version", nullable = false)
    private long version;

}
