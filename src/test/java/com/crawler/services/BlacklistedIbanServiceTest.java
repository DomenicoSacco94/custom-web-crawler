package com.crawler.services;

import com.crawler.domains.blacklist.BlacklistedIbanRepository;
import com.crawler.domains.blacklist.BlacklistedIbanService;
import com.crawler.domains.blacklist.exceptions.IbanAlreadyExistsException;
import com.crawler.domains.blacklist.exceptions.IbanNotFoundException;
import com.crawler.domains.blacklist.models.BlacklistedIban;
import com.crawler.domains.blacklist.models.BlacklistedIbanDTO;
import com.crawler.domains.blacklist.models.mappers.BlacklistedIbanMapper;
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

class BlacklistedIbanServiceTest {

    @Mock
    private BlacklistedIbanRepository repository;

    @Mock
    private BlacklistedIbanMapper mapper;

    @InjectMocks
    private BlacklistedIbanService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddIban() {
        BlacklistedIbanDTO dto = new BlacklistedIbanDTO();
        BlacklistedIban entity = new BlacklistedIban();
        when(repository.existsByIban(dto.getIban())).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(any(BlacklistedIban.class))).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        BlacklistedIbanDTO result = service.addIban(dto);

        assertEquals(dto, result);
        verify(repository, times(1)).existsByIban(dto.getIban());
        verify(repository, times(1)).save(any(BlacklistedIban.class));
    }

    @Test
    void testAddIbanAlreadyExists() {
        BlacklistedIbanDTO dto = new BlacklistedIbanDTO();
        when(repository.existsByIban(dto.getIban())).thenReturn(true);

        assertThrows(IbanAlreadyExistsException.class, () -> service.addIban(dto));
        verify(repository, times(1)).existsByIban(dto.getIban());
        verify(repository, never()).save(any(BlacklistedIban.class));
    }

    @Test
    void testGetAllIbans() {
        List<BlacklistedIban> entities = Arrays.asList(new BlacklistedIban(), new BlacklistedIban());
        List<BlacklistedIbanDTO> dtos = Arrays.asList(new BlacklistedIbanDTO(), new BlacklistedIbanDTO());
        when(repository.findAll()).thenReturn(entities);
        when(mapper.toDto(any(BlacklistedIban.class))).thenReturn(dtos.get(0), dtos.get(1));

        List<BlacklistedIbanDTO> result = service.getAllIbans();

        assertEquals(dtos, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetIbanById() {
        BlacklistedIban entity = new BlacklistedIban();
        BlacklistedIbanDTO dto = new BlacklistedIbanDTO();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        BlacklistedIbanDTO result = service.getIbanById(1L);

        assertEquals(dto, result);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetIbanByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IbanNotFoundException.class, () -> service.getIbanById(1L));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testUpdateIban() {
        BlacklistedIbanDTO dto = new BlacklistedIbanDTO();
        BlacklistedIban entity = new BlacklistedIban();
        when(repository.findById(dto.getId())).thenReturn(Optional.of(entity));
        when(repository.existsByIban(dto.getIban())).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(any(BlacklistedIban.class))).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        BlacklistedIbanDTO result = service.updateIban(dto);

        assertEquals(dto, result);
        verify(repository, times(1)).findById(dto.getId());
        verify(repository, times(1)).save(any(BlacklistedIban.class));
    }

    @Test
    void testUpdateIbanNotFound() {
        BlacklistedIbanDTO dto = new BlacklistedIbanDTO();
        when(repository.findById(dto.getId())).thenReturn(Optional.empty());

        assertThrows(IbanNotFoundException.class, () -> service.updateIban(dto));
        verify(repository, times(1)).findById(dto.getId());
        verify(repository, never()).save(any(BlacklistedIban.class));
    }

    @Test
    void testUpdateIbanAlreadyExists() {
        BlacklistedIbanDTO dto = new BlacklistedIbanDTO();
        dto.setId(1L);
        dto.setIban("DE89370400440532013000"); // Example IBAN
        BlacklistedIban existingEntity = new BlacklistedIban();
        existingEntity.setId(1L);
        existingEntity.setIban("DE44500105175407324931"); // Different example IBAN

        when(repository.findById(dto.getId())).thenReturn(Optional.of(existingEntity));
        when(repository.existsByIban(dto.getIban())).thenReturn(true);

        assertThrows(IbanAlreadyExistsException.class, () -> service.updateIban(dto));
        verify(repository, times(1)).findById(dto.getId());
        verify(repository, times(1)).existsByIban(dto.getIban());
        verify(repository, never()).save(any(BlacklistedIban.class));
    }

    @Test
    void testDeleteIban() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.deleteIban(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteIbanNotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(IbanNotFoundException.class, () -> service.deleteIban(1L));
        verify(repository, times(1)).existsById(1L);
        verify(repository, never()).deleteById(1L);
    }
}