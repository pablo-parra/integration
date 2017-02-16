package com.module.integration;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author pparrado
 *
 */
@SpringBootApplication
public class IntegrationApplication {
  public static void main(String[] args) throws IOException {

    ConfigurableApplicationContext ctx = SpringApplication.run(IntegrationApplication.class, args);

    System.out.println("Hit 'Enter' to terminate");
    System.in.read();
    ctx.close();
  }
}
