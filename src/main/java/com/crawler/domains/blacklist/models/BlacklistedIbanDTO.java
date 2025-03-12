package com.crawler.domains.blacklist.models;


import com.crawler.domains.blacklist.models.validator.ValidIban;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BlacklistedIbanDTO {
    private Long id;

    @NotNull(message = "IBAN cannot be null")
    @ValidIban
    private String iban;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}