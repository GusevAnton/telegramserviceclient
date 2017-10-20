package com.gusevanton.telegramclientservice.queue;

import com.gusevanton.telegramclientservice.dto.ServiceMessage;
import com.gusevanton.telegramclientservice.properties.ConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by antongusev on 20.10.17.
 */
@Service
@ConditionalOnProperty(value = "telegramService.enableQueue", havingValue = "true")
public class DefaultQueueServiceImpl implements QueueService {

    private final ConcurrentLinkedQueue<ServiceMessage> serviceMessageQueue = new ConcurrentLinkedQueue<>();

    @Autowired
    private ConfigurationProperties configurationProperties;



    public void push(ServiceMessage serviceMessage) {
        if (configurationProperties.isEnableQueue()) {
            serviceMessageQueue.add(serviceMessage);
        }
    }

    public int size() {
        return serviceMessageQueue.size();
    }

    public void clear() {
        serviceMessageQueue.clear();
    }

    public ServiceMessage poll() {
        if (configurationProperties.isEnableQueue()) {
            return serviceMessageQueue.poll();
        }
        return null;
    }

}
