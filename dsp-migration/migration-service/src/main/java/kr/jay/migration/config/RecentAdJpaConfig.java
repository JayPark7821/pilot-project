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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * LegacyAdJpaConfig
 *
 * @author jaypark
 * @version 1.0.0
 * @since 5/16/24
 */
@Configuration
@EnableJpaRepositories(
    basePackages = {
        "kr.jay.migration.domain.recent",
        "kr.jay.migration.domain.migration",
    },
    entityManagerFactoryRef = "recentAdEntityManagerFactory",
    transactionManagerRef = "recentAdTransactionManager"
)
@EnableTransactionManagement
public class RecentAdJpaConfig {

    @Primary
    @Bean("recentAdDataSource")
    @ConfigurationProperties(prefix = "spring.jpa.recent-ad.hikari")
    public DataSource dataSource(){
        return DataSourceBuilder.create()
            .type(HikariDataSource.class).build();
    }

    @Primary
    @Bean("recentAdJpaProperties")
    @ConfigurationProperties(prefix = "spring.jpa.recent-ad.properties")
    public Properties recentJpaProperties(){
        return new Properties();
    }

    @Primary
    @Bean("recentAdEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean legacyAdEntityManagerFactory(
        EntityManagerFactoryBuilder builder,
        @Qualifier("recentAdDataSource") DataSource dataSource,
        @Qualifier("recentAdJpaProperties")Properties jpaProperties
    ){
        LocalContainerEntityManagerFactoryBean factoryBean = builder
            .dataSource(dataSource)
            .packages("kr.jay.migration.domain.recent","kr.jay.migration.domain.migration")
            .build();

        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaProperties(jpaProperties);
        return factoryBean;
    }

    @Primary
    @Bean("recentAdTransactionManager")
    public PlatformTransactionManager recentTransactionManager(
        @Qualifier("recentAdEntityManagerFactory") EntityManagerFactory entityManagerFactory
    ){
        return new JpaTransactionManager(entityManagerFactory);
    }
}
