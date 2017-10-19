package com.gusevanton.telegramclientservice.annotation;

import com.gusevanton.telegramclientservice.TelegramAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by antongusev on 18.10.17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(TelegramAutoConfiguration.class)
public @interface EnableTelegramLogging {
}
