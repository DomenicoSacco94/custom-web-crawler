package com.crawler.domains.facts;

import com.crawler.domains.facts.models.FactDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facts")
@AllArgsConstructor
public class FactController {

    private final FactService factService;

    @GetMapping
    public List<FactDTO> getFacts() {
        return factService.getAllFacts();
    }
}