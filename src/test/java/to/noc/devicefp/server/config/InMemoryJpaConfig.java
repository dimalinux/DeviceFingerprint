/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.config;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.dialect.HSQLDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 *  Same as the non-test JPA config, but using an in-memory database.  Comment
 *  out the body if you want the integration test cases to run against MySQL.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("to.noc.devicefp.server.domain.repository")
public class InMemoryJpaConfig extends JpaConfig {

    @Override
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        BasicDataSource source = new BasicDataSource();
        source.setDriverClassName("org.hsqldb.jdbcDriver");
        source.setUrl("jdbc:hsqldb:mem:TestDb");
        source.setUsername("sa" );
        source.setPassword("");

        return source;
    }

    @Override
    protected Class getHibernateDialect() {
        return HSQLDialect.class;
    }

}
