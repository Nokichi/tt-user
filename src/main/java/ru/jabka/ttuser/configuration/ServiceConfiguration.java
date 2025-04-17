package ru.jabka.ttuser.configuration;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Setter
@Configuration
@ConfigurationProperties("services")
public class ServiceConfiguration {

    private String taskServiceUrl;

    @Bean
    public RestTemplate taskServiceRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(taskServiceUrl));
        return restTemplate;
    }
}