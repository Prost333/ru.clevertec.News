package ru.clevertec.ManagementNews.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.ManagementNews.aop.LoggNewsAspect;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "aop.logging", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LoggingAutoConfiguration {
    /**
     * Initializes the LoggingAutoConfiguration class and logs the initialization message.
     */

    @Bean
    @ConditionalOnMissingBean
    public LoggNewsAspect loggNewsAspect(){
        return new LoggNewsAspect();
    }
}
