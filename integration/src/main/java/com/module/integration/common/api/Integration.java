package com.module.integration.common.api;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author pparrado
 *
 */
public interface Integration {

  void send(ConfigurableApplicationContext ctx, String message);

  Object send(ConfigurableApplicationContext ctx, Object object);

  void subscribe(IntegrationHandler handler);

}
