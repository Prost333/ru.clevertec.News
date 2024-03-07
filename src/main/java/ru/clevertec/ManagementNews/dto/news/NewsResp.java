package ru.clevertec.ManagementNews.dto.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.ManagementNews.dto.comment.CommentResp;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResp implements Serializable {
    private Long id;
    private LocalDateTime time;
    private String title;
    private String text;
    private List<CommentResp> comments;
}
