package ru.clevertec.ManagementNews.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import ru.clevertec.ManagementNews.security.RestTemplateWithTokenService;

@RestController
@RequestMapping("/check")
@AllArgsConstructor
public class CheckController {
    private final RestTemplateWithTokenService restTemplateWithTokenService;

    @Operation(
            method = "GET",
            summary = "Get token",
            description = "Get token",
            responses = @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            )
    )
    @GetMapping("/{token}")
    public void getToken(@PathVariable String token) {
        restTemplateWithTokenService.setJwtToken(token);
    }
}
