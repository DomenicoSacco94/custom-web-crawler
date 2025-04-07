package com.crawler.domains.topics.models.mappers;

import com.crawler.domains.topics.regexp.models.mappers.RegexpMapper;
import com.crawler.domains.topics.models.Topic;
import com.crawler.domains.topics.models.TopicDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RegexpMapper.class})
public interface TopicMapper {
    TopicDTO toDto(Topic topic);
    Topic toEntity(TopicDTO topicDTO);
}