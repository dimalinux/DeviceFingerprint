/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import static org.hibernate.cfg.Environment.*;
import static java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.springframework.context.annotation.AdviceMode;

@Configuration
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableJpaRepositories("to.noc.devicefp.server.domain.repository")
public class JpaConfig {

    private static final int THIRTY_MINUTES_MS = 1000 * 60 * 30;

    @Value("${database.url}")       private String databaseUrl;
    @Value("${database.username}")  private String username;
    @Value("${database.password}")  private String password;
    @Value("${database.debugSql}")  private String debugSql;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {

        BasicDataSource source = new BasicDataSource();
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl(databaseUrl);
        source.setUsername(username);
        source.setPassword(password);
        source.setTestOnBorrow(true);
        source.setTestOnReturn(true);
        source.setTestWhileIdle(true);
        source.setTimeBetweenEvictionRunsMillis(THIRTY_MINUTES_MS);
        source.setNumTestsPerEvictionRun(3);
        source.setMinEvictableIdleTimeMillis(THIRTY_MINUTES_MS);
        source.setValidationQuery("SELECT 1");

        return source;
    }

    // Test code can override the dataSource and this method
    protected Class getHibernateDialect() {
        return MySQL5InnoDBDialect.class;
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaProperties(getJpaProperties());
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPackagesToScan("to.noc.devicefp.server.domain");
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    // not a bean
    private Properties getJpaProperties() {
        Properties properties = new Properties();
        properties.put(DIALECT, getHibernateDialect().getName());
        properties.setProperty(HBM2DDL_AUTO, "update"); // create, update, create-drop or validate
        // We don't delete devices nor do we have expected rollback scenarios,
        // so "read uncommited" should be safe.
        properties.setProperty(ISOLATION, String.valueOf(TRANSACTION_READ_UNCOMMITTED));
        properties.setProperty(SHOW_SQL, debugSql);
        properties.setProperty(FORMAT_SQL, debugSql);
        properties.setProperty(USE_SQL_COMMENTS, debugSql);
        properties.setProperty(USE_NEW_ID_GENERATOR_MAPPINGS, "true");

        // This definitely changes the naming strategy.  I'm not clear why there
        // is no variable for it.
        properties.setProperty("hibernate.ejb.naming_strategy", ImprovedNamingStrategy.class.getName());
        properties.setProperty("hibernate.connection.charSet", "UTF-8");

        return properties;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        txManager.setDataSource(dataSource());
        return txManager;
    }

}