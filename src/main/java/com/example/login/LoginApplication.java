package com.example.login;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application to perform all the logging and signing in operations
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@SpringBootApplication
public class LoginApplication {

  public static void main(String[] args) {
    SpringApplication.run(LoginApplication.class, args);
  }

  /**
   * Used to change the timezone to UTC
   */
  @PostConstruct
  void setUTCTimezone() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

}
