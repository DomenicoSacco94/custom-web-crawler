package com.crawler.domains.blacklist;

import com.crawler.domains.blacklist.models.BlacklistedIbanDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/blacklist")
@AllArgsConstructor
@Tag(name = "Blacklisted IBANs", description = "Operations related to blacklisted IBANs")
public class BlacklistedIbanController {

    private final BlacklistedIbanService blacklistedIbanService;

    @Operation(summary = "Add a new blacklisted IBAN")
    @PostMapping
    public ResponseEntity<BlacklistedIbanDTO> addIban(@RequestBody @Valid BlacklistedIbanDTO dto) {
        BlacklistedIbanDTO blacklistedIban = blacklistedIbanService.addIban(dto);
        return new ResponseEntity<>(blacklistedIban, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all blacklisted IBANs")
    @GetMapping
    public ResponseEntity<List<BlacklistedIbanDTO>> getAllIbans() {
        List<BlacklistedIbanDTO> ibans = blacklistedIbanService.getAllIbans();
        return new ResponseEntity<>(ibans, HttpStatus.OK);
    }

    @Operation(summary = "Get a blacklisted IBAN by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BlacklistedIbanDTO> getIbanById(@PathVariable Long id) {
        BlacklistedIbanDTO blacklistedIban = blacklistedIbanService.getIbanById(id);
        return new ResponseEntity<>(blacklistedIban, HttpStatus.OK);
    }

    @Operation(summary = "Update a blacklisted IBAN")
    @PutMapping
    public ResponseEntity<BlacklistedIbanDTO> updateIban(@RequestBody @Valid BlacklistedIbanDTO dto) {
        BlacklistedIbanDTO updatedIban = blacklistedIbanService.updateIban(dto);
        return new ResponseEntity<>(updatedIban, HttpStatus.OK);
    }

    @Operation(summary = "Delete a blacklisted IBAN by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIban(@PathVariable Long id) {
        blacklistedIbanService.deleteIban(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}