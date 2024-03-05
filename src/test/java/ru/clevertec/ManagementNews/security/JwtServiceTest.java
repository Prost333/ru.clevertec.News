package ru.clevertec.ManagementNews.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceTest {
    String key="c92cef2481ba7a58dc63485105e6c0bc289670ba31108ff8cdf6402ae92dc84e";
    @Test
    public void testGenerateToken() {
        JwtService jwtService = new JwtService();
        UserDetails userDetails = mock(UserDetails.class);
        ReflectionTestUtils.setField(jwtService, "secret", key);

        String token = jwtService.generateToken(userDetails);

        assertTrue(token.length() > 0);
    }

    @Test
    public void testExtractUserName() {
        JwtService jwtService = new JwtService();
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        ReflectionTestUtils.setField(jwtService, "secret", key);

        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);

        assertEquals("testUser", username);
    }

    @Test
    public void testIsTokenValid() {
        JwtService jwtService = new JwtService();
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        ReflectionTestUtils.setField(jwtService, "secret", key);


        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }
}