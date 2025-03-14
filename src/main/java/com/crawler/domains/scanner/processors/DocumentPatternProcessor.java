package com.crawler.domains.scanner.processors;

import com.crawler.domains.regexp.RegexpRepository;
import com.crawler.domains.regexp.models.Regexp;
import com.crawler.domains.regexp.models.RegexpProjection;
import com.crawler.domains.occurrences.models.OccurrenceDTO;
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

    private final static int SURROUNDING_CHARS = 2000;

    public List<OccurrenceDTO> detectPatterns(String text) {
        List<RegexpProjection> blacklistedPatterns = regexpRepository.findAllBy();

        return blacklistedPatterns.stream()
                .map(regexpProjection -> findPatternOccurrences(regexpProjection, text))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private OccurrenceDTO findPatternOccurrences(RegexpProjection regexpProjection, String text) {
        Pattern pattern = Pattern.compile(regexpProjection.getPattern(), Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            Regexp regexp = regexpRepository.findByPattern(regexpProjection.getPattern()).stream().findFirst().orElse(null);
            if (regexp != null) {
                int start = Math.max(0, matcher.start() - SURROUNDING_CHARS);
                int end = Math.min(text.length(), matcher.end() + SURROUNDING_CHARS);
                String surroundingText = text.substring(start, end);
                return new OccurrenceDTO(regexp, surroundingText, null);
            }
        }
        return null;
    }
}