package com.crawler.domains.scanner.processors;

import com.crawler.domains.occurrences.regexp.RegexpRepository;
import com.crawler.domains.occurrences.regexp.models.Regexp;
import com.crawler.domains.occurrences.regexp.models.RegexpDTO;
import com.crawler.domains.occurrences.regexp.models.RegexpProjection;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
import com.crawler.domains.occurrences.regexp.models.mappers.RegexpMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DocumentPatternProcessor {

    private final RegexpRepository regexpRepository;
    private final RegexpMapper regexpMapper;

    public final static int CHAR_WINDOW_LENGTH = 2000;

    public List<OccurrenceDTO> detectPatterns(String text, Long topicId, String url) {
        List<RegexpProjection> listedPatterns = topicId == null ? regexpRepository.findAllBy() : regexpRepository.findAllByTopicId(topicId);

        return listedPatterns.stream()
                .map(regexpProjection -> findPatternOccurrences(regexpProjection, text, url))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private OccurrenceDTO findPatternOccurrences(RegexpProjection regexpProjection, String text, String url) {
        Pattern pattern = Pattern.compile(regexpProjection.getPattern(), Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            Regexp regexp = regexpRepository.findByPattern(regexpProjection.getPattern()).stream().findFirst().orElse(null);
            if (regexp != null) {
                int start = Math.max(0, matcher.start() - CHAR_WINDOW_LENGTH /2);
                int end = Math.min(text.length(), matcher.end() + CHAR_WINDOW_LENGTH /2);
                String surroundingText = text.substring(start, end);
                RegexpDTO regexpDTO = regexpMapper.toDto(regexp);
                return new OccurrenceDTO(null, regexpDTO, surroundingText, url);
            }
        }
        return null;
    }
}