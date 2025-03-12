package com.crawler.domains.scanner.validator;

import com.crawler.domains.blacklist.BlacklistedIbanRepository;
import com.crawler.domains.blacklist.models.IbanProjection;
import com.crawler.domains.scanner.exceptions.BlacklistedIbanDetectedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BlacklistedIbanValidator implements Validator {

    private final BlacklistedIbanRepository ibanRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String text = (String) target;

        List<String> blacklistedIbans = ibanRepository.findAllBy().stream()
                .map(IbanProjection::getIban)
                .toList();

        //checking for the iban both in spaced and not spaced formats
        List<String> detectedIbans = blacklistedIbans.stream()
                .filter(iban -> text.contains(iban) || text.contains(iban.replaceAll("\\s+", "")))
                .collect(Collectors.toList());

        if (!detectedIbans.isEmpty()) {
            errors.reject(BlacklistedIbanDetectedException.class.getSimpleName(), new BlacklistedIbanDetectedException(detectedIbans).getMessage());
        }
    }
}