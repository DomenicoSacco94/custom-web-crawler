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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BlacklistedPatternValidator implements Validator {

    private final RegexpRepository regexpRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String text = (String) target;

        List<String> blacklistedPatterns = regexpRepository.findAllBy().stream()
                .map(RegexpProjection::getPattern)
                .toList();

        List<String> detectedPatterns = blacklistedPatterns.stream()
                .filter(pattern -> Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE).matcher(text).find())
                .toList();

        if (!detectedPatterns.isEmpty()) {
            List<Regexp> detectedEntities = regexpRepository.findAllByPatternIn(detectedPatterns);

            errors.reject(BlacklistedPatternDetectedException.class.getSimpleName(), new BlacklistedPatternDetectedException(detectedEntities).getMessage());
        }
    }
}