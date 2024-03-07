package ru.clevertec.ManagementNews.dto.news;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ManagementNews.entity.News;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    NewsResp toResponse(News news);

    News toRequest(NewsReq newsReq);
}
