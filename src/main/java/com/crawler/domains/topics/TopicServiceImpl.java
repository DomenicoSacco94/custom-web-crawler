package com.crawler.domains.topics;

import com.crawler.domains.regexp.RegexpRepository;
import com.crawler.domains.regexp.models.Regexp;
import com.crawler.domains.scanner.exceptions.TopicNotFoundException;
import com.crawler.domains.topics.models.Topic;
import com.crawler.domains.topics.models.TopicDTO;
import com.crawler.domains.topics.models.mappers.TopicMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final RegexpRepository regexpRepository;
    private final TopicMapper topicMapper;

    @Transactional(readOnly = true)
    public TopicDTO findTopicById(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Topic not found for ID: " + topicId));
        return topicMapper.toDto(topic);
    }

    @Transactional
    public TopicDTO createTopic(TopicDTO topicDTO) {
        final Topic topic = topicMapper.toEntity(topicDTO);

        // create the topic first
        Topic savedTopic = topicRepository.save(topic);

        // create the regexps referencing to it
        for (Regexp regexp : topic.getRegexps()) {
            regexp.setTopicId(savedTopic.getId());
            regexpRepository.save(regexp);
        }

        savedTopic.setRegexps(topic.getRegexps());

        return topicMapper.toDto(savedTopic);
    }
}