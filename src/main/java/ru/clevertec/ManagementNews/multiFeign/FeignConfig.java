package ru.clevertec.ManagementNews.multiFeign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.ManagementNews.multiFeign.comment.CommentFeign;
import ru.clevertec.ManagementNews.multiFeign.user.UserClient;

@Configuration
public class FeignConfig {
    @Autowired
    private UserClient userClient;


}
