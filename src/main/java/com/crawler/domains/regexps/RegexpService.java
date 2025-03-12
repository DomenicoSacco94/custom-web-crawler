package com.crawler.domains.regexps;

import com.crawler.domains.regexps.exceptions.PatternAlreadyExistsException;
import com.crawler.domains.regexps.exceptions.PatternNotFoundException;
import com.crawler.domains.regexps.models.Regexp;
import com.crawler.domains.regexps.models.RegexpDTO;
import com.crawler.domains.regexps.models.mappers.RegexpMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class RegexpService {

    private final RegexpRepository repository;
    private final RegexpMapper mapper = RegexpMapper.INSTANCE;

    public RegexpDTO addPattern(RegexpDTO dto) {
        log.info("Adding Pattern: {}", dto.getPattern());
        if (repository.existsByPattern(dto.getPattern())) {
            throw new PatternAlreadyExistsException(dto.getPattern());
        }
        Regexp regexp = mapper.toEntity(dto);
        Regexp savedPattern = repository.save(regexp);
        log.info("Pattern added successfully: {}", savedPattern.getPattern());
        return mapper.toDto(savedPattern);
    }

    public List<RegexpDTO> getAllPatterns() {
        log.info("Fetching all Patterns");
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public RegexpDTO getPatternById(Long id) {
        log.info("Fetching Pattern by ID: {}", id);
        Regexp regexp = repository.findById(id)
                .orElseThrow(() -> new PatternNotFoundException(id));
        return mapper.toDto(regexp);
    }

    public RegexpDTO updatePattern(RegexpDTO dto) {
        log.info("Updating Pattern: {}", dto.getPattern());
        Regexp existingPattern = repository.findById(dto.getId())
                .orElseThrow(() -> new PatternNotFoundException(dto.getId()));

        if (repository.existsByPattern(dto.getPattern()) && !existingPattern.getPattern().equals(dto.getPattern())) {
            throw new PatternAlreadyExistsException(dto.getPattern());
        }

        Regexp regexp = mapper.toEntity(dto);

        // we need to save the timestamp at which the record was originally created
        regexp.setCreatedAt(existingPattern.getCreatedAt());
        Regexp updatedPattern = repository.save(regexp);
        log.info("Pattern updated successfully: {}", updatedPattern.getPattern());
        return mapper.toDto(updatedPattern);
    }

    public void deletePattern(Long id) {
        log.info("Deleting Pattern with ID: {}", id);
        if (!repository.existsById(id)) {
            throw new PatternNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("Pattern deleted successfully with ID: {}", id);
    }
}