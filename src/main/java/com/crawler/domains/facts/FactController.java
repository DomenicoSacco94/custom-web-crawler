package com.crawler.domains.facts;

import com.crawler.domains.facts.models.FactDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class FactController {

    private final FactService factService;

    @GetMapping("/facts")
    public List<FactDTO> getFacts() {
        return factService.getAllFacts();
    }
}