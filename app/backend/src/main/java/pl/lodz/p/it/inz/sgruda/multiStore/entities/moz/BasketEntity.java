package pl.lodz.p.it.inz.sgruda.multiStore.entities.moz;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@Getter
@Setter
@Entity
@Table(name = "basket", schema = "public")
@TableGenerator(name = "BasketIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
        valueColumnName = "id_range", pkColumnValue = "basket")
public class BasketEntity implements Serializable, VersionGetter {
    @Id
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "BasketIdGen")
    private long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ordered_item_basket_mapping",
            joinColumns = @JoinColumn(name = "basket_id"),
            inverseJoinColumns = @JoinColumn(name = "ordered_item_id"))
    private List<OrderedItemEntity> orderedItemEntities = new ArrayList<>();

    @OneToOne(mappedBy = "basketEntity")
    private AccountEntity accountEntity;

    @Version
    @Setter(lombok.AccessLevel.NONE)
    @Basic
    @Column(name = "version", nullable = false)
    private long version;

    public BasketEntity() {
    }

    public BasketEntity(AccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }
}
