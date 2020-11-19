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

import javax.sql.DataSource;

//@Configuration
//@EnableJpaRepositories(basePackages = "pl.lodz.p.it.inz.sgruda.multiStore.mop.repositories",
//        entityManagerFactoryRef = "mopManagerFactory",
//        transactionManagerRef= "mopTransactionManager")
public class PersistenceMopConfig {
//    @Bean
//    @ConfigurationProperties("spring.datasource.mop")
//    public DataSourceProperties mopDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.mop.configuration")
//    public DataSource mopDataSource() {
//        return mopDataSourceProperties().initializeDataSourceBuilder()
//                .type(HikariDataSource.class).build();
//    }
//
//    @Bean(name = "mopManagerFactory")
//    public LocalContainerEntityManagerFactoryBean mopManagerFactory(
//            EntityManagerFactoryBuilder builder) {
//        return builder
//                .dataSource(mopDataSource())
//                .packages(.class)                   //TODO classes names from package entities
//                .build();
//    }
//
//    @Bean
//    public PlatformTransactionManager mopTransactionManager(
//            final @Qualifier("mopManagerFactory") LocalContainerEntityManagerFactoryBean mopManagerFactory) {
//        return new JpaTransactionManager(mopManagerFactory.getObject());
//    }

}