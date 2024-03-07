package ru.clevertec.ManagementNews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EnableFeignClients
@ComponentScan(basePackages = {"ru.clevertec.ManagementNews", "ru.clevertec.ManagementNews.aop.config"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }
}