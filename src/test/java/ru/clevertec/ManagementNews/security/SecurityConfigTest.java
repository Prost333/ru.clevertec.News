package ru.clevertec.ManagementNews.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import ru.clevertec.ManagementNews.multiFeign.user.UserClient;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    void jwtSecretKey() {
        SecretKey secretKey = securityConfig.jwtSecretKey();
        assertNotNull(secretKey);
    }

    @Test
    void jwtService() {
        JwtService jwtService = securityConfig.jwtService();
        assertNotNull(jwtService);
    }

    @Test
    void userClient() {
        UserClient userClient = securityConfig.userClient();
        assertNotNull(userClient);
    }

    @Test
    void jwtTokenFilter() {
        JwtTokenFilter jwtTokenFilter = securityConfig.jwtTokenFilter();
        assertNotNull(jwtTokenFilter);
    }


}