package ru.clevertec.ManagementNews.dto.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsReq implements Serializable {
    private LocalDateTime time;
    private String title;
    private String text;
}
