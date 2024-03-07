package ru.clevertec.ManagementNews.service.imp;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ManagementNews.dao.NewsDao;
import ru.clevertec.ManagementNews.dto.comment.CommentResp;
import ru.clevertec.ManagementNews.dto.news.NewsMapper;
import ru.clevertec.ManagementNews.dto.news.NewsReq;
import ru.clevertec.ManagementNews.dto.news.NewsResp;
import ru.clevertec.ManagementNews.entity.News;
import ru.clevertec.ManagementNews.exeption.EntityNotFoundException;
import ru.clevertec.ManagementNews.multiFeign.comment.CommentFeign;
import ru.clevertec.ManagementNews.proxy.Cache;
import ru.clevertec.ManagementNews.service.NewsService;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service class for managing news.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NewsServiceImp implements NewsService {
    private final NewsDao newsDao;
    private final NewsMapper newsMapper;
    private final CommentFeign commentFeign;

    /**
     * Saves a news item.
     *
     * @param newsReq the news request object
     * @return the response object after saving the news
     */
    @Override
    @Cache
    public NewsResp save(NewsReq newsReq) {
        News news = newsMapper.toRequest(newsReq);
        news.setTime(LocalDateTime.now());
        newsDao.save(news);
        return newsMapper.toResponse(news);
    }
    /**
     * Finds a news item by its ID.
     *
     * @param id the ID of the news item
     * @return the news response object
     * @throws EntityNotFoundException if no news item is found with the given ID
     */
    @Override
    @Cache
    public NewsResp findById(Long id) {
        Optional<News> news = newsDao.findById(id);
        if (news.isPresent()) {
            NewsResp newsResp = newsMapper.toResponse(news.get());
            return newsResp;
        } else {
            throw new EntityNotFoundException("News not found with id " + id);
        }
    }
    /**
     * Finds a news entity by its ID.
     *
     * @param id the ID of the news item
     * @return the news entity
     * @throws EntityNotFoundException if no news entity is found with the given ID
     */
    @Override
    public News findByIdEntity(Long id) {
        News news = newsDao.findById(id).orElseThrow(() -> new EntityNotFoundException("News not found with id " + id));
        return news;
    }
    /**
     * Deletes a news item by its ID.
     *
     * @param id the ID of the news item
     * @throws EntityNotFoundException if no news item is found with the given ID
     */
    @Override
    @Cache
    public void delete(Long id) {
        Optional<News> news = newsDao.findById(id);
        if (news.isPresent()) {
            newsDao.delete(news.get());
        } else {
            throw new EntityNotFoundException("News not found with id " + id);
        }
    }
    /**
     * Finds all news items.
     *
     * @param page the page number
     * @param pageSize the page size
     * @return a page of news response objects
     * @throws EntityNotFoundException if no news items are found
     */
    @Override
    @Cache
    public Page<NewsResp> findAll(int page, int pageSize) {
        Page<NewsResp> respPage = newsDao.findAll(PageRequest.of(page, pageSize)).map(newsMapper::toResponse);
        if (respPage.isEmpty()) {
            throw new EntityNotFoundException("News not found");
        }
        return respPage;
    }
    /**
     * Searches for news items by a keyword.
     *
     * @param keyword the keyword to search for
     * @param pageable the pagination information
     * @return a page of news response objects
     */
    @Override
    public Page<NewsResp> search(String keyword, Pageable pageable) {
        return newsDao.search(keyword, pageable).map(newsMapper::toResponse);
    }
    /**
     * Finds a news item by its title.
     *
     * @param title the title of the news item
     * @return the news response object
     * @throws EntityNotFoundException if no news item is found with the given title
     */
    @Override
    public NewsResp findByTitle(String title) {
        if (newsDao.findByTitle(title).isPresent()) {
            News news = newsDao.findByTitle(title).get();
            return newsMapper.toResponse(news);
        } else {
            throw new EntityNotFoundException("News not found");
        }
    }
    /**
     * Finds a news item by its text.
     *
     * @param text the text of the news item
     * @return the news response object
     * @throws EntityNotFoundException if no news item is found with the given text
     */
    @Override
    public NewsResp findByText(String text) {
        if (newsDao.findByText(text).isPresent()) {
            News news = newsDao.findByText(text).get();
            return newsMapper.toResponse(news);
        } else {
            throw new EntityNotFoundException("News not found");
        }
    }
    /**
     * Finds a news item and its comments by the news ID.
     *
     * @param newsId the ID of the news item
     * @return the news response object with its comments
     * @throws EntityNotFoundException if no news item is found with the given ID
     */
    @Override
    public NewsResp findByIdAndComments(Long newsId) {
        try {
            NewsResp newsResp = findById(newsId);
            List<CommentResp> respList = commentFeign.getCommentsByNewsId(newsId);
            newsResp.setComments(respList);
            return newsResp;
        } catch (RuntimeException e) {
            throw new EntityNotFoundException("News not found");
        }
    }

    /**
     * Finds a news item and a specific comment by the news ID and the comment ID.
     *
     * @param newsId the ID of the news item
     * @param commentId the ID of the comment
     * @return the news response object with the specific comment
     * @throws EntityNotFoundException if no news item is found with the given ID
     */
    @Override
    public NewsResp findByIdAndComments(Long newsId, Long commentId) {
        try {
            NewsResp newsResp = findById(newsId);
            List<CommentResp> respList = commentFeign.getCommentsByNewsId(newsId);
            newsResp.setComments(respList.stream()
                    .filter(commentResp -> commentResp
                            .getId()
                            .equals(commentId))
                    .collect(Collectors.toList()));
            return newsResp;
        } catch (RuntimeException e) {
            throw new EntityNotFoundException("News not found");
        }
    }
}
