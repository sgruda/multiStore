package pl.lodz.p.it.inz.sgruda.multiStore.configuration.persistence;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderedItemEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.StatusEntity;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "pl.lodz.p.it.inz.sgruda.multiStore.moz.repositories",
        entityManagerFactoryRef = "mozManagerFactory",
        transactionManagerRef= "mozTransactionManager")
public class PersistenceMozConfig {
    @Bean
    @ConfigurationProperties("spring.datasource.moz")
    public DataSourceProperties mozDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.moz.configuration")
    public DataSource mozDataSource() {
        return mozDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean(name = "mozManagerFactory")
    public LocalContainerEntityManagerFactoryBean mozManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mozDataSource())
                .packages(BasketEntity.class, OrderEntity.class, OrderedItemEntity.class, StatusEntity.class, ProductEntity.class, AccessLevelEntity.class, AccountEntity.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager mozTransactionManager(
            final @Qualifier("mozManagerFactory") LocalContainerEntityManagerFactoryBean mozManagerFactory) {
        return new JpaTransactionManager(mozManagerFactory.getObject());
    }

}