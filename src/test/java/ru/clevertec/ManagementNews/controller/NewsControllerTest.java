package ru.clevertec.ManagementNews.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.ManagementNews.dto.ResponsePage;
import ru.clevertec.ManagementNews.dto.news.NewsReq;
import ru.clevertec.ManagementNews.dto.news.NewsResp;
import ru.clevertec.ManagementNews.enums.Role;
import ru.clevertec.ManagementNews.multiFeign.user.User;
import ru.clevertec.ManagementNews.service.imp.NewsServiceImp;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
//public class NewsControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private NewsServiceImp newsService;
//
//    private void authenticateAsAdmin() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(Role.ADMIN.name()));
//        User user = User.builder().role(Role.ADMIN).password("admin123").username("admin").build();
//        UserDetails userDetails = user;
//        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//    }
//
//    private void authenticateAsJournalist() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(Role.JOURNALIST.name()));
//        User user = User.builder().role(Role.JOURNALIST).password("journalist1").username("journalist1").build();
//        UserDetails userDetails = user;
//        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//    }
//
//
////    @Test
////    public void testSave() throws Exception {
////        authenticateAsJournalist();
////        NewsResp newsResp = new NewsResp();
////        when(newsService.save(any(NewsReq.class))).thenReturn(newsResp);
////
////        mockMvc.perform(post("/news")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(new ObjectMapper().writeValueAsString(new NewsReq())))
////                .andExpect(status().isOk())
////                .andExpect(content().json(new ObjectMapper().writeValueAsString(newsResp)));
////
////        verify(newsService, times(1)).save(any(NewsReq.class));
////    }
//
//    @Test
//    public void testFindById() throws Exception {
//        authenticateAsAdmin();
//
//        NewsResp newsResp = new NewsResp();
//        when(newsService.findById(anyLong())).thenReturn(newsResp);
//
//        mockMvc.perform(get("/news/{id}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(newsResp)));
//
//        verify(newsService, times(1)).findById(anyLong());
//    }
//
//    @Test
//    public void testFindAll() throws Exception {
//        authenticateAsAdmin();
//
//        Page<NewsResp> pageNews = new PageImpl<>(new ArrayList<>());
//        ResponsePage<NewsResp> responsePage = new ResponsePage<>(pageNews);
//        when(newsService.findAll(anyInt(), anyInt())).thenReturn(new PageImpl<>(new ArrayList<>()));
//
//        mockMvc.perform(get("/news")
//                        .param("page", "0")
//                        .param("pageSize", "10"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(responsePage)));
//
//        verify(newsService, times(1)).findAll(anyInt(), anyInt());
//    }
//
//    @Test
//    public void testSearch() throws Exception {
//        authenticateAsAdmin();
//
//        Page<NewsResp> pageNews = new PageImpl<>(new ArrayList<>());
//        ResponsePage<NewsResp> responsePage = new ResponsePage<>(pageNews);
//        when(newsService.search(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
//
//        mockMvc.perform(get("/news/search")
//                        .param("keyword", "test"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(responsePage)));
//
//        verify(newsService, times(1)).search(anyString(), any(Pageable.class));
//    }
//
//    @Test
//    public void testFindByTitle() throws Exception {
//        authenticateAsAdmin();
//
//        NewsResp newsResp = new NewsResp();
//        when(newsService.findByTitle(anyString())).thenReturn(newsResp);
//
//        mockMvc.perform(get("/news/title/{title}", "test"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(newsResp)));
//
//        verify(newsService, times(1)).findByTitle(anyString());
//    }
//
//    @Test
//    public void testFindByText() throws Exception {
//        authenticateAsAdmin();
//
//        NewsResp newsResp = new NewsResp();
//        when(newsService.findByText(anyString())).thenReturn(newsResp);
//
//        mockMvc.perform(get("/news/text/{text}", "test"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(newsResp)));
//
//        verify(newsService, times(1)).findByText(anyString());
//    }
//
//    @Test
//    public void testFindByIdAndComments() throws Exception {
//        authenticateAsAdmin();
//
//        NewsResp newsResp = new NewsResp();
//        when(newsService.findByIdAndComments(anyLong())).thenReturn(newsResp);
//
//        mockMvc.perform(get("/news/{newsId}/comments", 1L))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(newsResp)));
//
//        verify(newsService, times(1)).findByIdAndComments(anyLong());
//    }
//
//    @Test
//    public void testFindByIdAndCommentId() throws Exception {
//        authenticateAsAdmin();
//
//        NewsResp newsResp = new NewsResp();
//        when(newsService.findByIdAndComments(anyLong(), anyLong())).thenReturn(newsResp);
//
//        mockMvc.perform(get("/news/{newsId}/comments/{commentId}", 1L, 1L))
//                .andExpect(status().isOk())
//                .andExpect(content().json(new ObjectMapper().writeValueAsString(newsResp)));
//
//        verify(newsService, times(1)).findByIdAndComments(anyLong(), anyLong());
//    }
//}
