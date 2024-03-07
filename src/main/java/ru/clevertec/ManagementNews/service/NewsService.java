package ru.clevertec.ManagementNews.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ManagementNews.dto.news.NewsReq;
import ru.clevertec.ManagementNews.dto.news.NewsResp;
import ru.clevertec.ManagementNews.entity.News;

public interface NewsService {

    NewsResp save(NewsReq newsReq);

    NewsResp findById(Long id);
    News findByIdEntity(Long id);

    void delete(Long id);

    Page<NewsResp> findAll(int page, int pageSize);

    Page<NewsResp> search(String keyword, Pageable pageable);
    NewsResp findByTitle(String title);

    NewsResp findByText(String text);

    NewsResp findByIdAndComments(Long newsId);
    NewsResp findByIdAndComments(Long newsId,Long commentId);

}
