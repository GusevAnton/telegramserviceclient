package com.gusevanton.telegramclientservice.utils;

import com.gusevanton.telegramclientservice.dto.ServiceMessage;
import com.gusevanton.telegramclientservice.properties.ConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

/**
 * Created by antongusev on 20.10.17.
 */
@Component
public class RestTemplateWrapper {

    private final static Logger log = Logger.getLogger("RestTemplateWrapper");

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConfigurationProperties configurationProperties;

    @Autowired
    private Environment environment;

    private ConcurrentLinkedQueue<ServiceMessage> serviceMessageQueue = new ConcurrentLinkedQueue<>();

    private final AtomicBoolean registered = new AtomicBoolean(false);


    @PostConstruct
    public void register() {
        try {
            this.send.accept(configurationProperties.getTelegramUrl() + "/register", "startup");
            registered.set(true);
        } catch (Exception e) {
            log.warning("Error on connection initialization with " + configurationProperties.getTelegramUrl() + ".");
        }
        if (!registered.get()) {
            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.schedule(() -> register(), 5, TimeUnit.SECONDS);
        }
    }

    @PreDestroy
    public void unregister() {
        if (registered.get()) {
            try {
                this.send.accept(configurationProperties.getTelegramUrl() + "/unregister", "shutdown");
            } catch (Exception e) {
                log.warning("Error on connection initialization with " + configurationProperties.getTelegramUrl() + ".");
            }
        }
    }

    public void notifyService(ServiceMessage serviceMessage) {
        if (registered.get()) {
            try {
                while(serviceMessageQueue.size() > 0) {
                    restTemplate.postForEntity(URI.create(configurationProperties.getTelegramUrl() + "/notify"), serviceMessageQueue.poll(), HashMap.class);
                }
                restTemplate.postForEntity(URI.create(configurationProperties.getTelegramUrl() + "/notify"), serviceMessage, HashMap.class);
                return;
            } catch (Exception e) {
                log.warning("Error on connection initialization with " + configurationProperties.getTelegramUrl() + ".");
                registered.set(false);
                ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                scheduledExecutorService.schedule(() -> register(), 5, TimeUnit.SECONDS);
                if (serviceMessage != null)
                    serviceMessageQueue.add(serviceMessage);
                return;
            }
        }
        serviceMessageQueue.add(serviceMessage);
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
