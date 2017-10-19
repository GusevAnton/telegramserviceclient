package com.gusevanton.telegramclientservice.dto;

/**
 * Created by antongusev on 19.10.17.
 */
public class ServiceTextMessageObject {

    private String hostName;

    private Exception exception;

    private String profile;

    private String serviceName;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
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
}
