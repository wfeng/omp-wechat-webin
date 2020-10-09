package com.efuture.wechat.config;

import com.efuture.wechat.common.RestClientUtils;
import com.efuture.wechat.common.ServiceVersion;
import com.efuture.wechat.controller.ProductReflect;
import com.efuture.wechat.language.MessageSourceHelper;
import com.efuture.wechat.utils.UniqueID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * @title: ApplicationConfig
 * @description: 应用配置
 * @author: wangf
 * @date: 2020/07/16
 */
@Configuration
public class ApplicationConfig {
    @Bean(name = "messageSourceHelper")
    public MessageSourceHelper onMessageSourceHelper() {
        MessageSourceHelper messageSourceHelper = new MessageSourceHelper();
        messageSourceHelper.setMessageSource(this.onResourceBundleMessageSource());
        return messageSourceHelper;
    }

    @Bean(name = "messageSource")
    public ResourceBundleMessageSource onResourceBundleMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean(name = "RestUtils")
    public RestClientUtils getRestUtils(){
        return new RestClientUtils();
    }

    @Bean(name = "UniqueID")
    public UniqueID getUniqueID(){
        UniqueID uniqueID = new UniqueID();
        return uniqueID;
    }

    @Bean(name = "ServiceMethodReflect")
    public ProductReflect onProductReflect() {
        return new ProductReflect();
    }

    @Value("${app.version}")
    String ver;
    @Value("${app.build.time}")
    String build;

    @Value("${app.openlog}")
    String openlog;

    @Bean(name = "ServiceVersion")
    public ServiceVersion getServiceVersion() {
        return new ServiceVersion(build, openlog);
    }
}
