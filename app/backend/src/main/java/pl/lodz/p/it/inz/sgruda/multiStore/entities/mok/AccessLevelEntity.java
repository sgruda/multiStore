package pl.lodz.p.it.inz.sgruda.multiStore.entities.mok;

import lombok.Getter;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.RoleName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
@ToString
@Getter
@Entity
@Table(name = "access_level", schema = "public", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"role_name"})
})
@TableGenerator(name = "AccessLevelIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
        valueColumnName = "id_range", pkColumnValue = "access_level")
public class AccessLevelEntity implements Serializable  {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AccessLevelIdGen")
    private long id;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, length = 16)
    private RoleName roleName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessLevelEntity that = (AccessLevelEntity) o;
        return id == that.id &&
                Objects.equals(roleName, that.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName);
    }
}
