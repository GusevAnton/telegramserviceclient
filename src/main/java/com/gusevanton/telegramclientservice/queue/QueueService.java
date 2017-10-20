package com.gusevanton.telegramclientservice.queue;

import com.gusevanton.telegramclientservice.dto.ServiceMessage;

/**
 * Created by antongusev on 20.10.17.
 */
public interface QueueService {

    void push(ServiceMessage serviceMessage);

    ServiceMessage poll();

    int size();

    void clear();

}

