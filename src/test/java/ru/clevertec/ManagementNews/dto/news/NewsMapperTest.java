package ru.clevertec.ManagementNews.dto.news;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.ManagementNews.entity.News;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NewsMapperTest {


    private final NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);

    @Test
    public void testToResponse() {
        News news = new News();
        news.setTitle("Test Title");
        news.setText("Test Content");

        NewsResp newsResp = newsMapper.toResponse(news);

        assertEquals(news.getTitle(), newsResp.getTitle());
        assertEquals(news.getText(), newsResp.getText());
    }

    @Test
    public void testToRequest() {
        NewsReq newsReq = new NewsReq();
        newsReq.setTitle("Test Title");
        newsReq.setText("Test Content");

        News news = newsMapper.toRequest(newsReq);

        assertEquals(newsReq.getTitle(), news.getTitle());
        assertEquals(newsReq.getText(), news.getText());
    }
}