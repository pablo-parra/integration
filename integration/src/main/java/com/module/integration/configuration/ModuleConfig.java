package com.module.integration.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;

/**
 * @author pparrado
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.module.integration" })
@EnableIntegration
public class ModuleConfig {

}
