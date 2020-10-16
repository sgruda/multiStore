package pl.lodz.p.it.inz.sgruda.multiStore.configuration.persistence;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
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
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccessLevelEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AuthenticationDataEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.ForgotPasswordTokenEntity;

import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "pl.lodz.p.it.inz.sgruda.multiStore.mok.repositories",
        entityManagerFactoryRef = "mokManagerFactory",
        transactionManagerRef= "mokTransactionManager"
)
public class PersistenceMokConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.mok")
    public DataSourceProperties mokDataSourceProperties() {
        return new DataSourceProperties();
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
                .packages(AccountEntity.class, AccessLevelEntity.class, AuthenticationDataEntity.class, ForgotPasswordTokenEntity.class)
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager mokTransactionManager(
            final @Qualifier("mokManagerFactory") LocalContainerEntityManagerFactoryBean mokManagerFactory) {
        return new JpaTransactionManager(mokManagerFactory.getObject());
    }
}
