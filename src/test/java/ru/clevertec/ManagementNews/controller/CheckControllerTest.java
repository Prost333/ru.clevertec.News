package ru.clevertec.ManagementNews.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ManagementNews.security.RestTemplateWithTokenService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
//public class CheckControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private RestTemplateWithTokenService restTemplateWithTokenService;
//
//    @Test
//    public void testGetToken() throws Exception {
//        String token = "testToken";
//
//        mockMvc.perform(get("/check/" + token))
//                .andExpect(status().isOk());
//
//        verify(restTemplateWithTokenService).setJwtToken(token);
//    }
//}