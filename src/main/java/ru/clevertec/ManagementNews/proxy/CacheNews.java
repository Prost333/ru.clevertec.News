package ru.clevertec.ManagementNews.proxy;

import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ru.clevertec.ManagementNews.cache.Cache;
import ru.clevertec.ManagementNews.cache.CacheFactory;
import ru.clevertec.ManagementNews.dao.NewsDao;
import ru.clevertec.ManagementNews.dto.news.NewsMapper;
import ru.clevertec.ManagementNews.dto.news.NewsReq;
import ru.clevertec.ManagementNews.dto.news.NewsResp;
import ru.clevertec.ManagementNews.entity.News;

import java.util.Optional;

@Aspect
@Component
@ConditionalOnBean(CacheFactory.class)
public class CacheNews {
    private final Cache<Object, Object> cache;

    private final NewsDao newsRepository;

    private final NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);;

    public CacheNews(CacheFactory factoryCache, NewsDao newsRepository) {
        this.cache = factoryCache.getCacheAlgorithm();
        this.newsRepository = newsRepository;
    }

    @Around("@annotation(ru.clevertec.ManagementNews.proxy.Cache) && execution(* ru.clevertec.ManagementNews.service.imp.NewsServiceImp.findById(..))")
    public Object cacheGet(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        Long id = (Long) args[0];
        if (cache.get(id) != null) {
            return cache.get(id);
        }
        NewsResp result;
        try {
            result = (NewsResp) joinPoint.proceed();
        } catch (EntityNotFoundException exception) {
            throw exception;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        cache.put(id, result);
        return result;
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.ManagementNews.proxy.Cache) && execution(* ru.clevertec.ManagementNews.service.imp.NewsServiceImp.save(..))", returning = "id")
    public void cacheCreate(Long id) {

        Optional<News> optionalNews = newsRepository.findById(id);
        optionalNews.ifPresent(news -> cache.put(id, newsMapper.toResponse(news)));
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.ManagementNews.proxy.Cache) && execution(* ru.clevertec.ManagementNews.service.imp.NewsServiceImp.update(..)) && args(id, newsRequest)", argNames = "id, newsRequest")
    public void cacheUpdate(Long id, NewsReq newsRequest) throws EntityNotFoundException {

        News news = newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("News not found with id " + id));
        cache.put(id, newsMapper.toResponse(news));
    }

    @AfterReturning(pointcut = "@annotation(ru.clevertec.ManagementNews.proxy.Cache) && execution(* ru.clevertec.ManagementNews.service.imp.NewsServiceImp.delete(..)) && args(id)", argNames = "id")
    public void cacheDelete(Long id) {

        cache.remove(id);
    }
}
