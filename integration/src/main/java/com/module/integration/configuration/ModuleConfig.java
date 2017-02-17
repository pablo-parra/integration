package com.module.integration.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

/**
 * @author pparrado
 *
 */
@Configuration
@EnableIntegration
@ComponentScan(basePackages = { "com.module.integration" })
@IntegrationComponentScan(basePackages = { "com.module.integration.common" })
public class ModuleConfig {

}
