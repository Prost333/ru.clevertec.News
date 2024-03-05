package ru.clevertec.ManagementNews.service.imp;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import static org.mockito.ArgumentMatchers.any;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.ManagementNews.dao.NewsDao;
import ru.clevertec.ManagementNews.dto.comment.CommentResp;
import ru.clevertec.ManagementNews.dto.news.NewsMapper;
import ru.clevertec.ManagementNews.dto.news.NewsReq;
import ru.clevertec.ManagementNews.dto.news.NewsResp;
import ru.clevertec.ManagementNews.entity.News;
import ru.clevertec.ManagementNews.enums.Role;
import ru.clevertec.ManagementNews.exeption.EntityNotFoundException;
import ru.clevertec.ManagementNews.multiFeign.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NewsServiceTest {
    @InjectMocks
    private NewsServiceImp newsService;

    @Mock
    private NewsDao newsDao;

    @Mock
    private NewsMapper newsMapper;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private WireMockServer wm;

    @Before
    public void setUp() {
        wm = new WireMockServer(WireMockConfiguration.options().port(8888));
        wm.start();
    }

    @After
    public void tearDown() {
        wm.stop();
    }

    private void authenticateAsAdmin() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.ADMIN.name()));
        User user = User.builder().role(Role.ADMIN).password("admin123").username("admin").build();
        UserDetails userDetails = user;
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void testFindByIdandComment() throws Exception {
        authenticateAsAdmin();
        Long id = 1L;
        NewsResp newsResp = new NewsResp();
        newsResp.setId(id);
        newsResp.setTitle("Test title");
        newsResp.setText("Test text");

        wm.stubFor(get(urlEqualTo("/comments/news/" + id))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(newsResp))));

//        mockMvc.perform(MockMvcRequestBuilders.get("/comments/news/" + id)
//                        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(("admin:admin123").getBytes(UTF_8)))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is(401));

    }

//    @Configuration
//    public static class TestConfiguration {
//        @Bean
//        public ServerList<Server> ribbonServerList() {
//            return new StaticServerList<>(new Server("localhost", 8888));
//        }
//        @Bean
//        public ObjectMapper objectMapper() {
//            return new ObjectMapper();
//        }
//    }

    @Test
    public void testSave() {
        NewsReq newsReq = new NewsReq();
        News news = new News();
        NewsResp newsResp = new NewsResp();

        when(newsMapper.toRequest(newsReq)).thenReturn(news);
        when(newsMapper.toResponse(news)).thenReturn(newsResp);

        NewsResp result = newsService.save(newsReq);

        assertEquals(newsResp, result);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        News news = new News();
        NewsResp newsResp = new NewsResp();

        when(newsDao.findById(id)).thenReturn(Optional.of(news));
        when(newsMapper.toResponse(news)).thenReturn(newsResp);

        NewsResp result = newsService.findById(id);

        assertEquals(newsResp, result);
    }

    @Test
    public void testFindByIdNotFound() {
        Long id = 1L;

        when(newsDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> newsService.findById(id));
    }


    @Test
    public void testDeleteNotFound() {
        Long id = 1L;

        when(newsDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> newsService.delete(id));
    }

    @Test
    public void testFindAll() {
        int page = 0;
        int pageSize = 10;
        Page<News> newsPage = new PageImpl<>(Collections.singletonList(new News()));
        Page<NewsResp> newsRespPage = new PageImpl<>(Collections.singletonList(new NewsResp()));

        when(newsDao.findAll(PageRequest.of(page, pageSize))).thenReturn(newsPage);
        when(newsMapper.toResponse(any())).thenReturn(new NewsResp());

        Page<NewsResp> result = newsService.findAll(page, pageSize);

        assertEquals(newsRespPage, result);
    }

    @Test
    public void testSearch() {
        String keyword = "test";
        Pageable pageable = PageRequest.of(0, 10);
        Page<News> newsPage = new PageImpl<>(Collections.singletonList(new News()));
        Page<NewsResp> newsRespPage = new PageImpl<>(Collections.singletonList(new NewsResp()));

        when(newsDao.search(keyword, pageable)).thenReturn(newsPage);
        when(newsMapper.toResponse(any())).thenReturn(new NewsResp());

        Page<NewsResp> result = newsService.search(keyword, pageable);

        assertEquals(newsRespPage, result);
    }

    @Test
    public void testFindByTitle() {
        String title = "test";
        News news = new News();
        NewsResp newsResp = new NewsResp();

        when(newsDao.findByTitle(title)).thenReturn(Optional.of(news));
        when(newsMapper.toResponse(news)).thenReturn(newsResp);

        NewsResp result = newsService.findByTitle(title);

        assertEquals(newsResp, result);
    }

    @Test
    public void testFindByTitleNotFound() {
        String title = "test";

        when(newsDao.findByTitle(title)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> newsService.findByTitle(title));
    }

    @Test
    public void testFindByText() {
        String text = "test";
        News news = new News();
        NewsResp newsResp = new NewsResp();

        when(newsDao.findByText(text)).thenReturn(Optional.of(news));
        when(newsMapper.toResponse(news)).thenReturn(newsResp);

        NewsResp result = newsService.findByText(text);

        assertEquals(newsResp, result);
    }

    @Test
    public void testFindByTextNotFound() {
        String text = "test";

        when(newsDao.findByText(text)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> newsService.findByText(text));
    }

}