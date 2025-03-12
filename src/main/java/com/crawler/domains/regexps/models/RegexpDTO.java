package com.crawler.domains.regexps.models;


import com.crawler.domains.regexps.models.validator.ValidRegexp;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RegexpDTO {
    private Long id;

    @NotNull(message = "Regexp pattern must be valid")
    @ValidRegexp
    private String pattern;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}