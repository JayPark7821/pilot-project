package kr.jay.pilotproject.common.config.multidatasource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableJpaRepositories(
	basePackages = "kr.jay.pilotproject",
	transactionManagerRef = "transcationManager",
	entityManagerFactoryRef = "entityManager"
)
@EnableTransactionManagement
@RequiredArgsConstructor
public class EdcDataSourcesRoutingConfig {

	private final EdcDataSourcesConfig edcDataSourcesConfig;

	@Primary
	@Bean
	public DataSource createEdcDataSource() {
		Map<Object, Object> dataSources = new HashMap<>();

		edcDataSourcesConfig.getDatasource()
			.forEach((key, value) ->
				dataSources.put(
					EdcDataSource.valueOf(key),
					value.initializeDataSourceBuilder().type(HikariDataSource.class).build()
				)
			);

		EdcDataSourceRouter routingDataSource = new EdcDataSourceRouter();
		routingDataSource.setTargetDataSources(dataSources);
		routingDataSource.setDefaultTargetDataSource(dataSources.get(EdcDataSource.BUILDER));

		return routingDataSource;
	}

	@Bean(name = "entityManager")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(createEdcDataSource())
			.packages("kr.jay.pilotproject")
			.properties(jpaProperties())
			.build();
	}

	protected Map<String, Object> jpaProperties() {
		Map<String, Object> props = new HashMap<>();
		props.put("hibernate.physical_naming_strategy", CamelCaseToUnderscoresNamingStrategy.class.getName());
		props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
		props.put("hibernate.hbm2ddl.auto", "create");
		return props;
	}


	@Bean(name = "transcationManager")
	public JpaTransactionManager transactionManager(
		@Autowired @Qualifier("entityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
		return new JpaTransactionManager(entityManagerFactoryBean.getObject());
	}
}