package com.gusevanton.telegramclientservice;

import com.gusevanton.telegramclientservice.bpp.TelegramLoggingBPP;
import com.gusevanton.telegramclientservice.properties.ConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

/**
 * Created by antongusev on 18.10.17.
 */
@SpringBootApplication
public class TelegramAutoConfiguration {

    private final static Logger log = Logger.getLogger("TelegramAutoConfiguration");

    public static void main(String[] args) {
        SpringApplication.run(TelegramAutoConfiguration.class, args);
    }

    @Autowired
    private ConfigurationProperties configurationProperties;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private Environment environment;

    @Bean
    public TelegramLoggingBPP telegramLoggingBPP() {
        return new TelegramLoggingBPP();
    }

    @Autowired
    private RestTemplate restTemplate;

}
