package com.crawler.controllers;

import com.crawler.domains.topics.TopicController;
import com.crawler.domains.topics.TopicService;
import com.crawler.domains.topics.models.TopicDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TopicControllerTest {

    @Mock
    private TopicService topicService;

    @InjectMocks
    private TopicController topicController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTopic() {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setName("Sample Topic");

        TopicDTO createdTopic = new TopicDTO();
        createdTopic.setId(1L);
        createdTopic.setName("Sample Topic");

        when(topicService.createTopic(any(TopicDTO.class))).thenReturn(createdTopic);

        ResponseEntity<TopicDTO> response = topicController.createTopic(topicDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdTopic, response.getBody());
    }
}