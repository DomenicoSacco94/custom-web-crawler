package com.crawler.domains.topics;

import com.crawler.domains.topics.models.TopicDTO;

public interface TopicService {
    TopicDTO findTopicById(Long topicId);
    TopicDTO createTopic(TopicDTO topicDTO);
}