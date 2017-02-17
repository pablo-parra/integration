package com.module.integration.common.impl;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.module.integration.common.api.IntegrationHandler;

/**
 * @author pparrado
 *
 */
@Component
public class IntegrationHandlerImpl implements IntegrationHandler {

  private static final Logger LOGGER = Logger.getLogger(IntegrationHandlerImpl.class.getName());

  @Override
  public Object handleMessage(Object payload) {

    // Default implementation for inFlow IntegrationHandler parameter
    LOGGER.log(java.util.logging.Level.INFO,
        "Default IntegrationHandler implementation launched. Message handled: {0}. Create your own Handler in the receiver application implementing 'IntegrationHandler' interface in order to manage the received messages.",
        payload.toString());
    return null;
  }

}
