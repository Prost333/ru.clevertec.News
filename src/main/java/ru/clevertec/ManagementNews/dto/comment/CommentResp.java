package ru.clevertec.ManagementNews.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.ManagementNews.entity.News;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResp implements Serializable {
    private Long id;
    private LocalDateTime time;
    private String text;
    private String username;
    private Long newsId;
}