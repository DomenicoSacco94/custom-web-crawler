package com.crawler.domains.topics;

import com.crawler.domains.topics.regexp.RegexpRepository;
import com.crawler.domains.topics.regexp.models.Regexp;
import com.crawler.domains.topics.models.Topic;
import com.crawler.domains.topics.models.TopicDTO;
import com.crawler.domains.topics.models.mappers.TopicMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final RegexpRepository regexpRepository;
    private final TopicMapper topicMapper;

    @Transactional
    public TopicDTO createTopic(TopicDTO topicDTO) {
        final Topic topic = topicMapper.toEntity(topicDTO);

        Topic savedTopic = topicRepository.save(topic);

        if (topic.getRegexps() != null) {
            for (Regexp regexp : topic.getRegexps()) {
                regexp.setTopicId(savedTopic.getId());
                regexpRepository.save(regexp);
            }
        }

        savedTopic.setRegexps(topic.getRegexps());
        topicRepository.save(savedTopic);

        return topicMapper.toDto(savedTopic);
    }
}