package kr.jay.migration.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * LegacyAdJpaConfig
 *
 * @author jaypark
 * @version 1.0.0
 * @since 5/16/24
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "kr.jay.migration.domain.legacy",
    entityManagerFactoryRef = "legacyAdEntityManagerFactory",
    transactionManagerRef = "legacyAdTransactionManager"
)
public class LegacyAdJpaConfig {

    @Bean("legacyAdDataSource")
    @ConfigurationProperties(prefix = "spring.jpa.legacy-ad.hikari")
    public DataSource dataSource(){
        return DataSourceBuilder.create()
            .type(HikariDataSource.class).build();
    }

    @Bean("legacyAdJpaProperties")
    @ConfigurationProperties(prefix = "spring.jpa.legacy-ad.properties")
    public Properties legacyJpaProperties(){
        return new Properties();
    }

    @Bean("legacyAdEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean legacyAdEntityManagerFactory(
        EntityManagerFactoryBuilder builder,
        @Qualifier("legacyAdDataSource") DataSource dataSource,
        @Qualifier("legacyAdJpaProperties")Properties jpaProperties
    ){
        LocalContainerEntityManagerFactoryBean factoryBean = builder
            .dataSource(dataSource)
            .packages("kr.jay.migration.domain.legacy")
            .build();

        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaProperties(jpaProperties);
        return factoryBean;
    }

    @Bean("legacyAdTransactionManager")
    public PlatformTransactionManager legacyTransactionManager(
        @Qualifier("legacyAdEntityManagerFactory") EntityManagerFactory entityManagerFactory
    ){
        return new JpaTransactionManager(entityManagerFactory);
    }
}
