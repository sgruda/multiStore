package pl.lodz.p.it.inz.sgruda.multiStore.entities.moz;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

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
    @Size(min = 36, max = 36, message = "validation.size")
    @Pattern(regexp = "[0-9A-Za-z-]+", message = "validation.pattern")
    @Column(name = "identifier", nullable = false, length = 32)
    private String identifier;

    @Basic(optional = false)
    @NotNull(message = "validation.notnull")
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @NotNull(message = "validation.notnull")
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private AccountEntity accountEntity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ordered_item_order_mapping",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "ordered_item_id"))
    private List<OrderedItemEntity> orderedItemEntities = new ArrayList<>();

    @Digits(integer = 7, fraction = 2, message = "validation.digits")
    @Basic(optional = false)
    @NotNull(message = "validation.notnull")
    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @NotNull(message = "validation.notnull")
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private StatusEntity statusEntity;

    @NotNull(message = "validation.notnull")
    @Size(max = 64, message = "validation.size")
    @Pattern(regexp = "[0-9A-Za-z-/]+", message = "validation.pattern")
    @Column(name = "address", nullable = false, length = 64)
    private String address;

    @Version
    @Setter(lombok.AccessLevel.NONE)
    @Basic
    @Column(name = "version", nullable = false)
    private long version;

    public OrderEntity() {
        this.identifier = UUID.randomUUID().toString();
    }
}
