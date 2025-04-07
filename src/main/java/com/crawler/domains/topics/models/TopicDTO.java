package com.crawler.domains.topics.models;

import com.crawler.domains.topics.regexp.models.RegexpDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TopicDTO {

    private Long id;
    private String name;
    private String description;
    private List<RegexpDTO> regexps;
}