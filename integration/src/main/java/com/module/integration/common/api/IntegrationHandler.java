package com.module.integration.common.api;

/**
 * @author pparrado
 *
 */
public interface IntegrationHandler {
  Object handleMessage(Object payload);
}
