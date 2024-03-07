package ru.clevertec.ManagementNews.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ManagementNews.enums.Role;
import ru.clevertec.ManagementNews.multiFeign.user.User;
import ru.clevertec.ManagementNews.multiFeign.user.UserClient;
import ru.clevertec.ManagementNews.security.JwtService;
import ru.clevertec.ManagementNews.security.RestTemplateWithTokenService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplateWithTokenService restTemplateWithTokenService;

    @MockBean
    private UserClient userClient;
    @Autowired
    private JwtService jwtService;
    private void authenticateAsAdmin() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.ADMIN.name()));
        User user = User.builder().role(Role.ADMIN).password("admin123").username("admin").build();
        UserDetails userDetails = user;
        String token = jwtService.generateToken(userDetails);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(userClient.getDto()).thenReturn(token);
    }

    @Test
    public void testGetToken() throws Exception {
        authenticateAsAdmin();
        String token = "testToken";

        mockMvc.perform(get("/check/" + token))
                .andExpect(status().isOk());

        verify(restTemplateWithTokenService).setJwtToken(token);
    }
}