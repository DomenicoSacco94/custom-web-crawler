package com.crawler.domains.blacklist;

import com.crawler.domains.blacklist.exceptions.IbanAlreadyExistsException;
import com.crawler.domains.blacklist.exceptions.IbanNotFoundException;
import com.crawler.domains.blacklist.models.BlacklistedIban;
import com.crawler.domains.blacklist.models.BlacklistedIbanDTO;
import com.crawler.domains.blacklist.models.mappers.BlacklistedIbanMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BlacklistedIbanService {

    private final BlacklistedIbanRepository repository;
    private final BlacklistedIbanMapper mapper = BlacklistedIbanMapper.INSTANCE;

    public BlacklistedIbanDTO addIban(BlacklistedIbanDTO dto) {
        log.info("Adding IBAN: {}", dto.getIban());
        if (repository.existsByIban(dto.getIban())) {
            throw new IbanAlreadyExistsException(dto.getIban());
        }
        BlacklistedIban blacklistedIban = mapper.toEntity(dto);
        BlacklistedIban savedIban = repository.save(blacklistedIban);
        log.info("IBAN added successfully: {}", savedIban.getIban());
        return mapper.toDto(savedIban);
    }

    public List<BlacklistedIbanDTO> getAllIbans() {
        log.info("Fetching all IBANs");
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public BlacklistedIbanDTO getIbanById(Long id) {
        log.info("Fetching IBAN by ID: {}", id);
        BlacklistedIban blacklistedIban = repository.findById(id)
                .orElseThrow(() -> new IbanNotFoundException(id));
        return mapper.toDto(blacklistedIban);
    }

    public BlacklistedIbanDTO updateIban(BlacklistedIbanDTO dto) {
        log.info("Updating IBAN: {}", dto.getIban());
        BlacklistedIban existingIban = repository.findById(dto.getId())
                .orElseThrow(() -> new IbanNotFoundException(dto.getId()));

        if (repository.existsByIban(dto.getIban()) && !existingIban.getIban().equals(dto.getIban())) {
            throw new IbanAlreadyExistsException(dto.getIban());
        }

        BlacklistedIban blacklistedIban = mapper.toEntity(dto);

        // we need to save the timestamp at which the record was originally created
        blacklistedIban.setCreatedAt(existingIban.getCreatedAt());
        BlacklistedIban updatedIban = repository.save(blacklistedIban);
        log.info("IBAN updated successfully: {}", updatedIban.getIban());
        return mapper.toDto(updatedIban);
    }

    public void deleteIban(Long id) {
        log.info("Deleting IBAN with ID: {}", id);
        if (!repository.existsById(id)) {
            throw new IbanNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("IBAN deleted successfully with ID: {}", id);
    }
}