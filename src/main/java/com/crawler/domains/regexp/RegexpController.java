package com.crawler.domains.regexp;

import com.crawler.domains.regexp.models.RegexpDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/regexp")
@AllArgsConstructor
@Tag(name = "Regexp Patterns", description = "Operations related to Regexp Patterns")
public class RegexpController {

    private final RegexpService regexpService;

    @Operation(summary = "Add a new Regexp Pattern")
    @PostMapping
    public ResponseEntity<RegexpDTO> addPattern(@RequestBody @Valid RegexpDTO dto) {
        RegexpDTO blacklistedPattern = regexpService.addPattern(dto);
        return new ResponseEntity<>(blacklistedPattern, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Regexp Patterns")
    @GetMapping
    public ResponseEntity<List<RegexpDTO>> getAllPatterns() {
        List<RegexpDTO> regexps = regexpService.getAllPatterns();
        return new ResponseEntity<>(regexps, HttpStatus.OK);
    }

    @Operation(summary = "Get a Regexp Pattern by ID")
    @GetMapping("/{id}")
    public ResponseEntity<RegexpDTO> getPatternById(@PathVariable Long id) {
        RegexpDTO blacklistedPattern = regexpService.getPatternById(id);
        return new ResponseEntity<>(blacklistedPattern, HttpStatus.OK);
    }

    @Operation(summary = "Update a Regexp Pattern")
    @PutMapping
    public ResponseEntity<RegexpDTO> updatePattern(@RequestBody @Valid RegexpDTO dto) {
        RegexpDTO updatedPattern = regexpService.updatePattern(dto);
        return new ResponseEntity<>(updatedPattern, HttpStatus.OK);
    }

    @Operation(summary = "Delete a Regexp Pattern by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePattern(@PathVariable Long id) {
        regexpService.deletePattern(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}