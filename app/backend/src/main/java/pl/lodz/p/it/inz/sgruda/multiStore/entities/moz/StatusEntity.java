package pl.lodz.p.it.inz.sgruda.multiStore.entities.moz;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.StatusName;

import javax.persistence.*;
import java.io.Serializable;

@ToString
@Getter
@Entity
@Table(name = "status", schema = "public", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"status_name"})
})
@TableGenerator(name = "StatusIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
        valueColumnName = "id_range", pkColumnValue = "status")
public class StatusEntity implements Serializable {
    @Id
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "StatusIdGen")
    private long id;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status_name", nullable = false, length = 16)
    private StatusName statusName;

}
