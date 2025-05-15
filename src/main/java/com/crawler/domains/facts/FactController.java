package com.crawler.domains.facts;

import com.crawler.domains.facts.models.FactDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1")
@Tag(name = "Facts", description = "Manages the Facts, which are information inferred by the Ollama Model from the Occurences")

public class FactController {

    private final FactService factService;

    @Operation(summary = "Retrieve all the facts")
    @GetMapping("/facts")
    public List<FactDTO> getFacts() {
        return factService.getAllFacts();
    }
}