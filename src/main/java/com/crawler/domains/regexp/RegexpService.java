package com.crawler.domains.regexp;

import com.crawler.domains.regexp.models.Regexp;
import com.crawler.domains.regexp.models.RegexpDTO;
import com.crawler.domains.regexp.models.RegexpProjection;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import com.crawler.domains.regexp.models.mappers.RegexpMapper;
import com.crawler.domains.scanner.exceptions.TopicNotFoundException;
import com.crawler.domains.topics.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RegexpService {

    private final RegexpRepository regexpRepository;
    private final RegexpMapper regexpMapper;
    private final TopicService topicService;

    public static final int CHAR_WINDOW_LENGTH = 200;

    public List<OccurrenceDTO> detectPatterns(String text, Long topicId, String url) {
        if (topicId != null) {
            topicService.findTopicById(topicId);
        }

        List<RegexpProjection> listedPatterns = regexpRepository.findAllByTopicId(topicId);

        if (listedPatterns.isEmpty()) {
            throw new TopicNotFoundException("No Regexp found for topic ID: " + topicId);
        }

        return listedPatterns.stream()
                .flatMap(regexpProjection -> findPatternOccurrences(regexpProjection, text, url).stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<OccurrenceDTO> findPatternOccurrences(RegexpProjection regexpProjection, String text, String url) {
        Pattern pattern = Pattern.compile(regexpProjection.getPattern(), Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        List<OccurrenceDTO> occurrences = new ArrayList<>();

        while (matcher.find()) {
            Regexp regexp = regexpRepository.findByPattern(regexpProjection.getPattern())
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (regexp != null) {
                int start = Math.max(0, matcher.start() - CHAR_WINDOW_LENGTH / 2);
                int end = Math.min(text.length(), matcher.end() + CHAR_WINDOW_LENGTH / 2);
                String surroundingText = text.substring(start, end);
                RegexpDTO regexpDTO = regexpMapper.toDto(regexp);
                occurrences.add(new OccurrenceDTO(null, regexpDTO, surroundingText, url));
            }
        }
        return occurrences;
    }
}