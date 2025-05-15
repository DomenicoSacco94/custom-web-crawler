package com.crawler.domains.topics;

import com.crawler.domains.topics.models.TopicDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/v1")
@Tag(name = "Topics", description = "Manages the Topics to search with the scanner (and the Regexps they are composed of)")
public class TopicController {

    private final TopicService topicService;

    @Operation(summary = "Create a new Topic")
    @PostMapping("/topics")
    public ResponseEntity<TopicDTO> createTopic(@RequestBody TopicDTO topicDTO) {
        TopicDTO createdTopic = topicService.createTopic(topicDTO);
        return ResponseEntity.ok(createdTopic);
    }
}