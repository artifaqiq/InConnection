/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

/**
 * @author Lomako
 * @version 1.0
 */
@Configuration
@Profile("!test")
@ImportResource("classpath:dao-context.xml")
@EnableAspectJAutoProxy
public class DaoConfig {

}
