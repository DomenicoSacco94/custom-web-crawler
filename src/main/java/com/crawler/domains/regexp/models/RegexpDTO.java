package com.crawler.domains.regexp.models;


import com.crawler.domains.regexp.models.validator.ValidRegexp;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RegexpDTO {
    private Long id;

    @NotNull(message = "Regexp pattern must be valid")
    @ValidRegexp
    private String pattern;

    private String description;

    private Long topicId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}