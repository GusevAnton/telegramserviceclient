package com.gusevanton.telegramclientservice.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by antongusev on 19.10.17.
 */
@Component
public class ConfigurationProperties {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${telegramService.url}")
    private String telegramUrl;

    @Value("${telegramService.enableQueue:true}")
    private boolean enableQueue;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }


    public String getTelegramUrl() {
        return telegramUrl;
    }

    public void setTelegramUrl(String telegramUrl) {
        this.telegramUrl = telegramUrl;
    }

    public boolean isEnableQueue() {
        return enableQueue;
    }

    public void setEnableQueue(boolean enableQueue) {
        this.enableQueue = enableQueue;
    }
}
