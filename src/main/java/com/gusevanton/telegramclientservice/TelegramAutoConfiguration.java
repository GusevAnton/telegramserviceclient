package com.gusevanton.telegramclientservice;

import com.gusevanton.telegramclientservice.bpp.TelegramLoggingBPP;
import com.gusevanton.telegramclientservice.dto.ServiceMessage;
import com.gusevanton.telegramclientservice.properties.ConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;
import java.util.HashMap;
import java.util.function.BiConsumer;
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

    @PostConstruct
    public void register() {
        try {
            this.send.accept(configurationProperties.getTelegramUrl() + "/register", "startup");
        } catch (Exception e) {
            log.warning("Error on connection initialization with " + configurationProperties.getTelegramUrl() + ".");
        }
    }

    @PreDestroy
    public void unregister() {
        try {
            this.send.accept(configurationProperties.getTelegramUrl() + "/unregister", "shutdown");
        } catch (Exception e) {
            log.warning("Error on connection initialization with " + configurationProperties.getTelegramUrl() + ".");
        }
    }

    private BiConsumer<String, String> send = (url, actionName) -> {
        ServiceMessage serviceMessage = new ServiceMessage();
        serviceMessage.setServiceName(configurationProperties.getApplicationName());
        serviceMessage.setAction(actionName);
        String activeProfile = environment.getActiveProfiles()[0];
        serviceMessage.setProfile(activeProfile == null ? "default" : activeProfile);
        restTemplate.postForEntity(URI.create(url), serviceMessage, HashMap.class);
    };

}
