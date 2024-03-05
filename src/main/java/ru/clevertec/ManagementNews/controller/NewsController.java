package ru.clevertec.ManagementNews.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ManagementNews.dto.ResponsePage;
import ru.clevertec.ManagementNews.dto.news.NewsReq;
import ru.clevertec.ManagementNews.dto.news.NewsResp;
import ru.clevertec.ManagementNews.service.imp.NewsServiceImp;

@RestController
@RequestMapping("/news")
@AllArgsConstructor
public class NewsController {
    private final NewsServiceImp newsService;

    @Operation(
            method = "POST",
            summary = "Save news",
            description = "Save news",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewsReq.class)
                    )
            ),
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewsResp.class)
                    )
            )
    )
    @PostMapping
    public ResponseEntity<NewsResp> save(@RequestBody NewsReq newsReq) {
        return ResponseEntity.ok(newsService.save(newsReq));
    }

    @Operation(
            method = "GET",
            summary = "Find news by ID",
            description = "Find news by ID",
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewsResp.class)
                    )
            )
    )
    @GetMapping("/{id}")
    public ResponseEntity<NewsResp> findById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.findById(id));
    }

    @Operation(
            method = "DELETE",
            summary = "Delete news by ID",
            description = "Delete news by ID",
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "204"
            )
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        newsService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            method = "GET",
            summary = "Find all news",
            description = "Find all news",
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponsePage.class)
                    )
            )
    )
    @GetMapping
    public ResponsePage<NewsResp> findAll(@RequestParam(defaultValue = "0", name = "page") int page,
                                          @RequestParam (defaultValue = "0", name = "pageSize")int pageSize) {
        Page<NewsResp> pageNews=newsService.findAll(page, pageSize);
        return new ResponsePage<>(pageNews);
    }

    @Operation(
            method = "GET",
            summary = "Search news by keyword",
            description = "Search news by keyword",
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponsePage.class)
                    )
            )
    )
    @GetMapping("/search")
    public ResponsePage<NewsResp> search(@RequestParam String keyword, Pageable pageable) {
        Page<NewsResp> pageNews=newsService.search(keyword, pageable);
        return new ResponsePage<>(pageNews);
    }

    @Operation(
            method = "GET",
            summary = "Find news by title",
            description = "Find news by title",
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewsResp.class)
                    )
            )
    )
    @GetMapping("/title/{title}")
    public ResponseEntity<NewsResp> findByTitle(@PathVariable String title) {
        return ResponseEntity.ok(newsService.findByTitle(title));
    }

    @Operation(
            method = "GET",
            summary = "Find news by text",
            description = "Find news by text",
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewsResp.class)
                    )
            )
    )
    @GetMapping("/text/{text}")
    public ResponseEntity<NewsResp> findByText(@PathVariable String text) {
        return ResponseEntity.ok(newsService.findByText(text));
    }

    @Operation(
            method = "GET",
            summary = "Find news by ID and comments",
            description = "Find news by ID and comments",
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewsResp.class)
                    )
            )
    )
    @GetMapping("/{newsId}/comments")
    public ResponseEntity<NewsResp>findByIdAndComments(@PathVariable Long newsId) {
        NewsResp news = newsService.findByIdAndComments(newsId);
        return ResponseEntity.ok(news);
    }

    @Operation(
            method = "GET",
            summary = "Find news by ID and comment ID",
            description = "Find news by ID and comment ID",
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewsResp.class)
                    )
            )
    )
    @GetMapping("/{newsId}/comments/{commentId}")
    public ResponseEntity<NewsResp>findByIdAndCommentId(@PathVariable Long newsId,@PathVariable Long commentId) {
        NewsResp news = newsService.findByIdAndComments(newsId, commentId);
        return ResponseEntity.ok(news);
    }

}
