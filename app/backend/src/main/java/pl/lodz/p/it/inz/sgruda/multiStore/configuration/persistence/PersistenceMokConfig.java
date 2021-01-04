package pl.lodz.p.it.inz.sgruda.multiStore.configuration.persistence;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AuthenticationDataEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.ForgotPasswordTokenEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.OrderEntity;

import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories",
        entityManagerFactoryRef = "mokManagerFactory",
        transactionManagerRef= "mokTransactionManager"
)
public class PersistenceMokConfig {
    @Value("${spring.datasource.mok.url}")
    private String url;
    @Value("${spring.datasource.mok.username}")
    private String username;
    @Value("${spring.datasource.mok.password}")
    private String password;
    @Value("${spring.datasource.mok.driverClassName}")
    private String driver;

    @Bean
    @Primary
//    @ConfigurationProperties("spring.datasource.mok")
    public DataSourceProperties mokDataSourceProperties() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setUrl(url);
        dataSourceProperties.setUsername(username);
        dataSourceProperties.setPassword(password);
        dataSourceProperties.setDriverClassName(driver);
        return dataSourceProperties;
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.mok.configuration")
    public DataSource mokDataSource() {
        return mokDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }
    @Primary
    @Bean(name = "mokManagerFactory")
    public LocalContainerEntityManagerFactoryBean mokManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mokDataSource())
                .packages(AccountEntity.class, AccessLevelEntity.class, AuthenticationDataEntity.class,
                        ForgotPasswordTokenEntity.class, BasketEntity.class, OrderEntity.class, ProductEntity.class)
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager mokTransactionManager(
            final @Qualifier("mokManagerFactory") LocalContainerEntityManagerFactoryBean mokManagerFactory) {
        return new JpaTransactionManager(mokManagerFactory.getObject());
    }
}
