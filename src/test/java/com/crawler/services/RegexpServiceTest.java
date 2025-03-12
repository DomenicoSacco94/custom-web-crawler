package com.crawler.services;

import com.crawler.domains.regexps.RegexpRepository;
import com.crawler.domains.regexps.RegexpService;
import com.crawler.domains.regexps.exceptions.PatternAlreadyExistsException;
import com.crawler.domains.regexps.exceptions.PatternNotFoundException;
import com.crawler.domains.regexps.models.Regexp;
import com.crawler.domains.regexps.models.RegexpDTO;
import com.crawler.domains.regexps.models.mappers.RegexpMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegexpServiceTest {

    @Mock
    private RegexpRepository repository;

    @Mock
    private RegexpMapper mapper;

    @InjectMocks
    private RegexpService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPattern() {
        RegexpDTO dto = new RegexpDTO();
        Regexp entity = new Regexp();
        when(repository.existsByPattern(dto.getPattern())).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(any(Regexp.class))).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        RegexpDTO result = service.addPattern(dto);

        assertEquals(dto, result);
        verify(repository, times(1)).existsByPattern(dto.getPattern());
        verify(repository, times(1)).save(any(Regexp.class));
    }

    @Test
    void testAddPatternAlreadyExists() {
        RegexpDTO dto = new RegexpDTO();
        when(repository.existsByPattern(dto.getPattern())).thenReturn(true);

        assertThrows(PatternAlreadyExistsException.class, () -> service.addPattern(dto));
        verify(repository, times(1)).existsByPattern(dto.getPattern());
        verify(repository, never()).save(any(Regexp.class));
    }

    @Test
    void testGetAllPatterns() {
        List<Regexp> entities = Arrays.asList(new Regexp(), new Regexp());
        List<RegexpDTO> dtos = Arrays.asList(new RegexpDTO(), new RegexpDTO());
        when(repository.findAll()).thenReturn(entities);
        when(mapper.toDto(any(Regexp.class))).thenReturn(dtos.get(0), dtos.get(1));

        List<RegexpDTO> result = service.getAllPatterns();

        assertEquals(dtos, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetPatternById() {
        Regexp entity = new Regexp();
        RegexpDTO dto = new RegexpDTO();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        RegexpDTO result = service.getPatternById(1L);

        assertEquals(dto, result);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetPatternByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatternNotFoundException.class, () -> service.getPatternById(1L));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testUpdatePattern() {
        RegexpDTO dto = new RegexpDTO();
        Regexp entity = new Regexp();
        when(repository.findById(dto.getId())).thenReturn(Optional.of(entity));
        when(repository.existsByPattern(dto.getPattern())).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(any(Regexp.class))).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        RegexpDTO result = service.updatePattern(dto);

        assertEquals(dto, result);
        verify(repository, times(1)).findById(dto.getId());
        verify(repository, times(1)).save(any(Regexp.class));
    }

    @Test
    void testUpdatePatternNotFound() {
        RegexpDTO dto = new RegexpDTO();
        when(repository.findById(dto.getId())).thenReturn(Optional.empty());

        assertThrows(PatternNotFoundException.class, () -> service.updatePattern(dto));
        verify(repository, times(1)).findById(dto.getId());
        verify(repository, never()).save(any(Regexp.class));
    }

    @Test
    void testUpdatePatternAlreadyExists() {
        RegexpDTO dto = new RegexpDTO();
        dto.setId(1L);
        dto.setPattern("example-pattern");
        Regexp existingEntity = new Regexp();
        existingEntity.setId(1L);
        existingEntity.setPattern("different-pattern");

        when(repository.findById(dto.getId())).thenReturn(Optional.of(existingEntity));
        when(repository.existsByPattern(dto.getPattern())).thenReturn(true);

        assertThrows(PatternAlreadyExistsException.class, () -> service.updatePattern(dto));
        verify(repository, times(1)).findById(dto.getId());
        verify(repository, times(1)).existsByPattern(dto.getPattern());
        verify(repository, never()).save(any(Regexp.class));
    }

    @Test
    void testDeletePattern() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.deletePattern(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePatternNotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(PatternNotFoundException.class, () -> service.deletePattern(1L));
        verify(repository, times(1)).existsById(1L);
        verify(repository, never()).deleteById(1L);
    }
}