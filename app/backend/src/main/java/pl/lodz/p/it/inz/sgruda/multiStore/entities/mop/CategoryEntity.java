package pl.lodz.p.it.inz.sgruda.multiStore.entities.mop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.CategoryName;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

import javax.persistence.*;
import java.io.Serializable;

@ToString
@Getter
@Entity
@Table(name = "category", schema = "public", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"category_name"})
})
@TableGenerator(name = "CategoryIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
        valueColumnName = "id_range", pkColumnValue = "category")
public class CategoryEntity implements Serializable, VersionGetter {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CategoryIdGen")
    private long id;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "category_name", nullable = false, length = 16)
    private CategoryName categoryName;

    @Version
    @Setter(lombok.AccessLevel.NONE)
    @Basic
    @Column(name = "version", nullable = false)
    private long version;
}
