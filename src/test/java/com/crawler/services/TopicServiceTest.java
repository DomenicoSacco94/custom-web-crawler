package com.crawler.services;

import com.crawler.domains.regexp.RegexpRepository;
import com.crawler.domains.regexp.models.Regexp;
import com.crawler.domains.topics.TopicRepository;
import com.crawler.domains.topics.TopicServiceImpl;
import com.crawler.domains.topics.models.Topic;
import com.crawler.domains.topics.models.TopicDTO;
import com.crawler.domains.topics.models.mappers.TopicMapper;
import com.crawler.domains.scanner.exceptions.TopicNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private RegexpRepository regexpRepository;

    @Mock
    private TopicMapper topicMapper;

    @InjectMocks
    private TopicServiceImpl topicService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindTopicById() {
        Long topicId = 1L;
        Topic topic = new Topic();
        topic.setId(topicId);
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(topicId);

        when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));
        when(topicMapper.toDto(topic)).thenReturn(topicDTO);

        TopicDTO result = topicService.findTopicById(topicId);

        assertNotNull(result);
        assertEquals(topicId, result.getId());
        verify(topicRepository, times(1)).findById(topicId);
        verify(topicMapper, times(1)).toDto(topic);
    }

    @Test
    void testFindTopicByIdThrowsException() {
        Long topicId = 1L;

        when(topicRepository.findById(topicId)).thenReturn(Optional.empty());

        assertThrows(TopicNotFoundException.class, () -> topicService.findTopicById(topicId));
        verify(topicRepository, times(1)).findById(topicId);
        verifyNoInteractions(topicMapper);
    }

    @Test
    void testCreateTopic() {
        TopicDTO topicDTO = new TopicDTO();
        Topic topic = new Topic();
        topic.setRegexps(List.of(new Regexp()));

        when(topicMapper.toEntity(topicDTO)).thenReturn(topic);
        when(topicRepository.save(topic)).thenReturn(topic);
        when(topicMapper.toDto(topic)).thenReturn(topicDTO);

        TopicDTO result = topicService.createTopic(topicDTO);

        assertNotNull(result);
        verify(topicMapper, times(1)).toEntity(topicDTO);
        verify(topicRepository, times(2)).save(any(Topic.class));
        verify(regexpRepository, times(1)).save(any(Regexp.class));
        verify(topicMapper, times(1)).toDto(topic);
    }
}