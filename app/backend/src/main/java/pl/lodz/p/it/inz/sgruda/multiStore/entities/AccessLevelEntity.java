package pl.lodz.p.it.inz.sgruda.multiStore.entities;

import lombok.Getter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Getter
@Entity
@Table(name = "access_level", schema = "public", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"role_name"})
})
@TableGenerator(name = "AccessLevelIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
        valueColumnName = "id_range", pkColumnValue = "access_level")
public class AccessLevelEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AccessLevelIdGen")
    private long id;

    @Basic
    @Column(name = "role_name", nullable = false, length = 16)
    private String roleName;

    //    @OneToMany(mappedBy = "accessLevelByAccessLevelId")
//    private Collection<AccountAccessLevelMappingEntity> accountAccessLevelMappingsById;
//
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
