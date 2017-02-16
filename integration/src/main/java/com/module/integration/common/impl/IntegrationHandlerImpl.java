package com.module.integration.common.impl;

import org.springframework.stereotype.Component;

import com.module.integration.common.api.IntegrationHandler;

/**
 * @author pparrado
 *
 */
@Component
public class IntegrationHandlerImpl implements IntegrationHandler {

  @Override
  public Object handleMessage(Object payload) {

    // Default implementation for inFlow IntegrationHandler parameter
    return null;
  }

}
