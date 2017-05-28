/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Lomako
 * @version 1.0
 */
@Configuration
@Profile("!test")
@PropertySource("classpath:jdbc.properties")
@EnableAspectJAutoProxy
public class DaoConfig {

    private static final String HIBERNATE_PROPERTIES_LOCATION = "classpath:hibernate.properties";

    @Value("${connection.url}")
    private String connectionUrl;

    @Value("${connection.driver}")
    private String connectionDriverClassName;

    @Value("${connection.username}")
    private String connectionUsername;

    @Value("${connection.password}")
    private String connectionPassword;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(connectionUrl);
        dataSource.setDriverClassName(connectionDriverClassName);
        dataSource.setUsername(connectionUsername);
        dataSource.setPassword(connectionPassword);

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean() throws IOException {

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setPackagesToScan("by.nc.lomako.pojos");
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaProperties(hibernateProperties());

        return entityManagerFactory;
    }

    private Properties hibernateProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(applicationContext.getResource(HIBERNATE_PROPERTIES_LOCATION).getInputStream());
        return properties;

    }
}
