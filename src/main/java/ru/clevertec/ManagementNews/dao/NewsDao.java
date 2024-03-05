package ru.clevertec.ManagementNews.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.ManagementNews.entity.News;

import java.util.Optional;

public interface NewsDao extends JpaRepository<News, Long> {

    Page<News> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM News n WHERE " +
            "to_tsvector('simple', n.title) @@ plainto_tsquery('simple', :keyword) OR " +
            "to_tsvector('simple', n.text) @@ plainto_tsquery('simple', :keyword)",
            countQuery = "SELECT count(*) FROM News n WHERE " +
                    "to_tsvector('simple', n.title) @@ plainto_tsquery('simple', :keyword) OR " +
                    "to_tsvector('simple', n.text) @@ plainto_tsquery('simple', :keyword)",
            nativeQuery = true)
    Page<News> search(@Param("keyword") String keyword, Pageable pageable);

    Optional<News> findByTitle(String title);

    Optional<News> findByText(String text);

}
