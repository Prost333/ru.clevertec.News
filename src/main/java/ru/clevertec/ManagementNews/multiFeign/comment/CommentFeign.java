package ru.clevertec.ManagementNews.multiFeign.comment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.ManagementNews.dto.comment.CommentResp;
import ru.clevertec.ManagementNews.multiFeign.FeignConfig;

import java.util.List;

@FeignClient(name = "comment-service", url = "http://appb:8082", configuration = FeignConfig.class)
public interface CommentFeign {

    @GetMapping("/comments/news/{newsId}")
    List<CommentResp> getCommentsByNewsId(@PathVariable("newsId") Long newsId);

}