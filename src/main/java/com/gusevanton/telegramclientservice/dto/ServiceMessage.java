package com.gusevanton.telegramclientservice.dto;

/**
 * Created by antongusev on 14.10.17.
 */
public class ServiceMessage {

    private String serviceName;

    private String profile;

    private String action;

    private ServiceTextMessageObject serviceTextMessageObject;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceTextMessageObject getServiceTextMessageObject() {
        return serviceTextMessageObject;
    }

    public void setServiceTextMessageObject(ServiceTextMessageObject serviceTextMessageObject) {
        this.serviceTextMessageObject = serviceTextMessageObject;
    }
}
