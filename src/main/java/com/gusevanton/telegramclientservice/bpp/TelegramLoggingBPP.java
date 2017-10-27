package com.gusevanton.telegramclientservice.bpp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gusevanton.telegramclientservice.annotation.LoggingTelegram;
import com.gusevanton.telegramclientservice.annotation.NonTelegramException;
import com.gusevanton.telegramclientservice.dto.ServiceMessage;
import com.gusevanton.telegramclientservice.dto.ServiceTextMessageObject;
import com.gusevanton.telegramclientservice.properties.ConfigurationProperties;
import com.gusevanton.telegramclientservice.utils.RestTemplateWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * Created by antongusev on 18.10.17.
 */
@Primary
@Component
public class TelegramLoggingBPP implements BeanPostProcessor {

	@Autowired
	private RestTemplateWrapper restTemplateWrapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Environment environment;

	@Autowired
	private ConfigurationProperties configurationProperties;

	@Override
	public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
		Class clazz = o.getClass();
		Field field = null;
		try {
			field = clazz.getDeclaredField("log");
		} catch (NoSuchFieldException e) {
			try {
				field = clazz.getDeclaredField("logger");
			} catch (NoSuchFieldException e1) {
			}
		}
		if (field != null) {
			Class loggerClass = field.getType();
			Annotation annotation = clazz.getDeclaredAnnotation(LoggingTelegram.class);
			final Field finalField = field;
			if (annotation != null) {
				try {
					field.setAccessible(true);
					Field modifiersField = Field.class.getDeclaredField("modifiers");
					modifiersField.setAccessible(true);
					modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
					final Object loggerValue = field.get(o);
					Object loggerProxy = Proxy.newProxyInstance(loggerClass.getClassLoader(), new Class[]{loggerClass}, (proxy, method, args) -> {
						if ("error"
								.equals(method.getName())) {
							Arrays.stream(args).forEach(arg -> {
								if (arg instanceof Throwable) {
									Annotation telegramExceptionAnnotation = arg.getClass().getDeclaredAnnotation(NonTelegramException.class);
									if (telegramExceptionAnnotation == null) {
										ServiceTextMessageObject serviceTextMessageObject = new ServiceTextMessageObject();
										serviceTextMessageObject.setException((Exception) arg);
										try {
											serviceTextMessageObject.setHostName(InetAddress.getLocalHost().getHostName());
										} catch (Exception e) {

										}
										ServiceMessage serviceMessage = new ServiceMessage();
										serviceMessage.setServiceName(configurationProperties.getApplicationName());
										serviceMessage.setProfile(environment.getActiveProfiles()[0]);
										serviceMessage.setAction("notify");
										try {
											objectMapper.writerWithDefaultPrettyPrinter();
											serviceMessage.setServiceTextMessageObject(serviceTextMessageObject);
										} catch (Exception e) {

										}
										restTemplateWrapper.notifyService(serviceMessage);
									}
								}
							});
						}
						return method.invoke(loggerValue, args);
					});
					field.set(null, loggerProxy);
				} catch (Exception e) {
					System.out.println("Error" + e.getMessage());
				}
			}
		}
		return o;
	}

	@Override
	public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
		return o;
	}
}
