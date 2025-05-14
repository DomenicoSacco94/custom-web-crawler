package com.crawler.domains.topics;

import com.crawler.domains.topics.models.TopicDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @PostMapping("/topics")
    public ResponseEntity<TopicDTO> createTopic(@RequestBody TopicDTO topicDTO) {
        TopicDTO createdTopic = topicService.createTopic(topicDTO);
        return ResponseEntity.ok(createdTopic);
    }
}