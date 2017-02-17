package com.module.integration.common.config;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.dsl.support.GenericHandler;

import com.module.integration.common.api.IntegrationHandler;;

/**
 * @author pparrado
 *
 */
@Configuration
@PropertySource("classpath:integration.properties")
public class IntegrationConfig {

  private static final Logger LOGGER = Logger.getLogger(IntegrationConfig.class.getName());

  @Inject
  private ConnectionFactory connectionFactory;

  @Value("${integration.one-direction.queue}")
  private String queue_1d;

  @Value("${integration.request-reply.queue}")
  private String queue_rr;

  @Value("${integration.poller.rate}")
  private int rate;

  // PRECONFIGURED GATEWAYS - - - - - - - - - - - - - - - - - - - - - -

  @MessagingGateway
  public interface OneDirectionGateway {
    @Gateway(requestChannel = "1d.Channel")
    void send(String message);
  }

  @MessagingGateway
  public interface RequestReplyGateway {
    @Gateway(requestChannel = "rr.Channel")
    String echo(String message);
  }

  // PRECONFIGURED FOWS - - - - - - - - - - - - - - - - - - - - - - - -

  // out

  @Bean
  @Profile("1d")
  @ConditionalOnProperty(prefix = "integration.emitter", name = "enabled", havingValue = "true")
  IntegrationFlow outFlow() {

    return IntegrationFlows.from("1d.Channel")
        .handle(Jms.outboundAdapter(this.connectionFactory).destination(this.queue_1d)).get();

  }

  @Bean
  @Profile("rr")
  @ConditionalOnProperty(prefix = "integration.emitter", name = "enabled", havingValue = "true")
  public IntegrationFlow outAndInFlow() {

    return IntegrationFlows.from("rr.Channel")
        .handle(Jms.outboundGateway(this.connectionFactory).requestDestination(this.queue_rr)).get();
  }

  // in

  @Bean
  @Profile("1d")
  @ConditionalOnProperty(prefix = "integration.listener", name = "enabled", havingValue = "true")
  public IntegrationFlow inFlow(IntegrationHandler handler) throws Exception {

    return IntegrationFlows.from(Jms.inboundAdapter(this.connectionFactory).destination(this.queue_1d),
        c -> c.poller(Pollers.fixedRate(this.rate, TimeUnit.MILLISECONDS))).handle(m -> {
          try {
            handler.handleMessage(m.getPayload());
          } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
          }
        }).get();
  }

  @Bean
  @Profile("rr")
  @ConditionalOnProperty(prefix = "integration.listener", name = "enabled", havingValue = "true")
  public IntegrationFlow inAndOutFlow(IntegrationHandler h) {

    return IntegrationFlows.from(Jms.inboundGateway(this.connectionFactory).destination(this.queue_rr))
        .wireTap(flow -> flow.handle(System.out::println)).handle(new GenericHandler<String>() {

          @Override
          public Object handle(String payload, Map<String, Object> headers) {

            try {
              return h.handleMessage(payload);
            } catch (Exception e) {
              LOGGER.log(Level.SEVERE, e.getMessage());
              return null;
            }
          }
        }

        ).get();
  }

}
