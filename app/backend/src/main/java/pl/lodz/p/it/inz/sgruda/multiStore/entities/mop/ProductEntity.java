package pl.lodz.p.it.inz.sgruda.multiStore.entities.mop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.AuthProvider;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.ProductType;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ToString
@Getter
@Setter
@Entity
@Table(name = "product", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"title"})
        })
@TableGenerator(name = "ProductIdGen", table = "id_generator", schema = "public", pkColumnName = "class_name",
        valueColumnName = "id_range", pkColumnValue = "product")
public class ProductEntity implements Serializable, VersionGetter {
        @Id
        @Setter(lombok.AccessLevel.NONE)
        @Column(name = "id", nullable = false)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "ProductIdGen")
        private long id;

        @Basic(optional = false)
        @NotNull(message = "validation.notnull")
        @Size(min = 1, max = 32, message = "validation.size")
        @Pattern(regexp = "[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ ]+", message = "validation.pattern")
        @Column(name = "title", nullable = false, unique = true, length = 32)
        private String title;

        @Basic(optional = false)
        @NotNull(message = "validation.notnull")
        @Size(min = 1, max = 512, message = "validation.size")
        @Column(name = "description", nullable = false, length = 512)
        private String description;

        @Digits(integer = 7, fraction = 0, message = "validation.digits")
        @Basic(optional = false)
        @NotNull(message = "validation.notnull")
        @Column(name = "in_store", nullable = false)
        private int inStore;

        @Digits(integer = 7, fraction = 2, message = "validation.digits")
        @Basic(optional = false)
        @NotNull(message = "validation.notnull")
        @Column(name = "price", nullable = false)
        private double price;

        @Basic(optional = false)
        @NotNull(message = "validation.notnull")
        @Enumerated(EnumType.STRING)
        private ProductType type;

        @NotNull(message = "validation.notnull")
        @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
        @ManyToOne(optional = false)
        private CategoryEntity categoryEntity;

        @Basic(optional = false)
        @NotNull(message = "validation.notnull")
        @Column(name = "active", nullable = false)
        private boolean active;

        @Version
        @Setter(lombok.AccessLevel.NONE)
        @Basic
        @Column(name = "version", nullable = false)
        private long version;

        public ProductEntity() {
        }

        public ProductEntity(@NotNull(message = "validation.notnull") @Size(min = 1, max = 32, message = "validation.size")
                             @Pattern(regexp = "[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+", message = "validation.pattern")
                                     String title,
                             @NotNull(message = "validation.notnull") @Size(min = 1, max = 512, message = "validation.size")
                                     String description,
                             @Digits(integer = 7, fraction = 0, message = "validation.digits") @NotNull(message = "validation.notnull")
                                     int inStore,
                             @Digits(integer = 7, fraction = 2, message = "validation.digits") @NotNull(message = "validation.notnull")
                                     double price,
                             @NotNull(message = "validation.notnull")
                                     ProductType type) {
                this.title = title;
                this.description = description;
                this.inStore = inStore;
                this.price = price;
                this.type = type;
                this.active = true;
        }
}
