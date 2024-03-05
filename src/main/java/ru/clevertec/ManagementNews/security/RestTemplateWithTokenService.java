package ru.clevertec.ManagementNews.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateWithTokenService class provides a service to store and retrieve JWT token for making authenticated REST API calls.
 */
@Service
public class RestTemplateWithTokenService {
    private String jwtToken;

    /**
     * Sets the JWT token for making authenticated REST API calls.
     *
     * @param jwtToken The JWT token to set.
     */
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    /**
     * Retrieves the stored JWT token.
     *
     * @return The stored JWT token.
     */
    public String getJwtToken() {
        return jwtToken;
    }

}