package ru.clevertec.ManagementNews.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.ManagementNews.entity.News;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
class NewsDaoTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    @Autowired
    private NewsDao newsDao;

    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    void testFindAll() {
        Page<News> newsPage = newsDao.findAll(PageRequest.of(0, 10));
        assertNotNull(newsPage);
    }

    @Test
    void testSearch() {
        Page<News> newsPage = newsDao.search("keyword", PageRequest.of(0, 10));
        assertNotNull(newsPage);
    }

    @Test
    void testFindByTitle() {
        Optional<News> news = newsDao.findByTitle("title");
        assertNotNull(news);
    }

    @Test
    void testFindByText() {
        Optional<News> news = newsDao.findByText("text");
        assertNotNull(news);
    }
}