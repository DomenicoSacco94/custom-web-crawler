package com.crawler.domains.scanner.validator;

import com.crawler.domains.regexps.RegexpRepository;
import com.crawler.domains.regexps.models.Regexp;
import com.crawler.domains.regexps.models.RegexpProjection;
import com.crawler.domains.scanner.exceptions.BlacklistedPatternDetectedException;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BlacklistedPatternValidator implements Validator {

    private final RegexpRepository regexpRepository;

    private final static int SURROUNDING_CHARS = 500;

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String text = (String) target;

        List<RegexpProjection> blacklistedPatterns = regexpRepository.findAllBy();

        List<DetectedPattern> detectedPatterns = blacklistedPatterns.stream()
                .map(regexpProjection -> {
                    Pattern pattern = Pattern.compile(regexpProjection.getPattern(), Pattern.DOTALL | Pattern.MULTILINE);
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.find()) {
                        Regexp regexp = regexpRepository.findByPattern(regexpProjection.getPattern()).stream().findFirst().orElse(null);
                        if (regexp != null) {
                            int start = Math.max(0, matcher.start() - SURROUNDING_CHARS);
                            int end = Math.min(text.length(), matcher.end() + SURROUNDING_CHARS);
                            String surroundingText = text.substring(start, end);
                            return new DetectedPattern(regexp, surroundingText);
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!detectedPatterns.isEmpty()) {
            errors.reject(BlacklistedPatternDetectedException.class.getSimpleName(), new BlacklistedPatternDetectedException(detectedPatterns).getMessage());
        }
    }
}