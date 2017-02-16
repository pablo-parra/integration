package com.module.integration.common.impl;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.ConfigurableApplicationContext;

import com.module.integration.common.api.Integration;
import com.module.integration.common.api.IntegrationHandler;
import com.module.integration.common.config.IntegrationConfig;
import com.module.integration.common.config.IntegrationConfig.OneDirectionGateway;
import com.module.integration.common.config.IntegrationConfig.RequestReplyGateway;
import com.module.integration.common.utils.XmlManager;

/**
 * @author pparrado
 *
 */
@Named
public class IntegrationImpl implements Integration {

  private static final Logger LOGGER = Logger.getLogger(IntegrationImpl.class.getName());

  @Inject
  private IntegrationConfig integrationConfig;

  @Override
  public void send(ConfigurableApplicationContext ctx, String message) {

    // AnnotationConfigApplicationContext ctxx = new AnnotationConfigApplicationContext(RootConfiguration.class);
    OneDirectionGateway oneDirectionGateway = ctx.getBean(OneDirectionGateway.class);
    oneDirectionGateway.send(message);
  }

  @Override
  public Object send(ConfigurableApplicationContext ctx, Object o) {

    String xml = XmlManager.convertObjectToXml(o);
    RequestReplyGateway rrGateway = ctx.getBean(RequestReplyGateway.class);
    String xmlResponse = rrGateway.echo(xml);
    return XmlManager.convertXmlToObject(xmlResponse);

  }

  @Override
  public void subscribe(IntegrationHandler h) {

    try {
      this.integrationConfig.inFlow(h);
    } catch (Exception e) {
      LOGGER.log(java.util.logging.Level.SEVERE, "Error subscribing to the integration flow: {0} ", e.getMessage());
    }

  }

}
