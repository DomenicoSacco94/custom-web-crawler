package com.crawler.domains.scanner.processors;

import com.crawler.domains.regexps.RegexpRepository;
import com.crawler.domains.regexps.models.Regexp;
import com.crawler.domains.regexps.models.RegexpProjection;
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

    private final static int SURROUNDING_CHARS = 500;

    public List<RegexpOccurrence> detectPatterns(String text) {
        List<RegexpProjection> blacklistedPatterns = regexpRepository.findAllBy();

        return blacklistedPatterns.stream()
                .map(regexpProjection -> {
                    Pattern pattern = Pattern.compile(regexpProjection.getPattern(), Pattern.DOTALL | Pattern.MULTILINE);
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.find()) {
                        Regexp regexp = regexpRepository.findByPattern(regexpProjection.getPattern()).stream().findFirst().orElse(null);
                        if (regexp != null) {
                            int start = Math.max(0, matcher.start() - SURROUNDING_CHARS);
                            int end = Math.min(text.length(), matcher.end() + SURROUNDING_CHARS);
                            String surroundingText = text.substring(start, end);
                            return new RegexpOccurrence(regexp, surroundingText);
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}