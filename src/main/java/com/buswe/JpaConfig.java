package com.buswe;

import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.buswe.core.dao.jpa.BaseRepositoryImpl;
import org.hibernate.jpa.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = CoreApplication.class, entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "jpaTransaction"
		,repositoryBaseClass=BaseRepositoryImpl.class,
		excludeFilters = {
		@org.springframework.context.annotation.ComponentScan.Filter({
				org.springframework.stereotype.Controller.class }) })
@EnableSpringDataWebSupport
/**
 * DomainClassConverter - to allow usage of domain types managed by Spring Data repositories as controller method arguments bound with PathVariable or RequestParam.
 PageableHandlerMethodArgumentResolver - to allow injection of Pageable instances into controller methods automatically created from request parameters.
 SortHandlerMethodArgumentResolver - to allow injection of Sort instances into controller methods automatically created from request parameters.
 */
@EnableJpaAuditing
class JpaConfig implements TransactionManagementConfigurer {

	@Value("${spring.dataSource.driverClassName}")
	private String driver;
	@Value("${spring.dataSource.url}")
	private String url;
	@Value("${spring.dataSource.username}")
	private String username;
	@Value("${spring.dataSource.password}")
	private String password;
	@Value("${spring.hibernate.dialect}")
	private String dialect;
	@Value("${spring.hibernate.hbm2ddl.auto}")
	private String hbm2ddlAuto;
	@Value("${spring.hibernate.show_sql}")
	private Boolean showSql;

	@Bean(name = "stat-filter")
	public StatFilter statFilter() {
		StatFilter statFilter = new StatFilter();
		statFilter.setLogSlowSql(true);
		statFilter.setSlowSqlMillis(10000);
		return statFilter;
	}

	@Bean(name = "log4j-filter")
	public Log4jFilter log4jFilter() {
		Log4jFilter log4jFilter = new Log4jFilter();
		log4jFilter.setStatementExecutableSqlLogEnable(true);
		return log4jFilter;
	}

	@Bean(name = { "dataSource" }, initMethod = "init", destroyMethod = "close")
	public DataSource dataSource() {
		DruidDataSource ds = new DruidDataSource();
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setUrl(url);
		ds.setInitialSize(1);
		ds.setMaxActive(20);
		try {
			ds.setFilters("stat,log4j");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}

	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setPackagesToScan("com.buswe");
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		Properties jpaProperties = new Properties();
		jpaProperties.put(org.hibernate.cfg.Environment.DIALECT, dialect);
		jpaProperties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, hbm2ddlAuto);
		jpaProperties.put(org.hibernate.cfg.Environment.SHOW_SQL, showSql);
		//jpaProperties.put(org.hibernate.cfg.Environment.STORAGE_ENGINE,"innodb");
		  jpaProperties.put(org.hibernate.cfg.AvailableSettings.CACHE_REGION_FACTORY,"org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");

		// hibernate.cache.region.factory_class
		entityManagerFactoryBean.setJpaProperties(jpaProperties);
		entityManagerFactoryBean.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return entityManagerFactoryBean;
	}

	@Bean(name = "jpaTransaction")
	public JpaTransactionManager jpaTransaction(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		JpaTransactionManager manager = new JpaTransactionManager();
		manager.setEntityManagerFactory(entityManagerFactory.getObject());
		return manager;
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new JpaTransactionManager();
	}
}
