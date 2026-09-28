package com.shortfilm;

import com.shortfilm.entity.Entry;
import com.shortfilm.repository.EntryRepository;
import com.shortfilm.service.EntryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntryServiceTest {

    @Mock
    private EntryRepository entryRepository;

    @InjectMocks
    private EntryService entryService;

    private Entry entry;

    @BeforeEach
    void setUp() {
        entry = new Entry();
        entry.setTitle("The Last Frame");
        entry.setDirectorName("Kavin");
        entry.setDescription("A short film");
        entry.setFilmUrl("https://example.com/film");
    }

    @Test
    void shouldCreateEntry() {
        when(entryRepository.save(entry)).thenReturn(entry);

        Entry result = entryService.createEntry(entry);

        assertNotNull(result);
        assertEquals("The Last Frame", result.getTitle());
        verify(entryRepository).save(entry);
    }

    @Test
    void shouldGetEntryById() {
        when(entryRepository.findById(1L))
                .thenReturn(Optional.of(entry));

        Entry result = entryService.getEntryById(1L);

        assertNotNull(result);
        assertEquals("The Last Frame", result.getTitle());
        verify(entryRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenEntryNotFound() {
        when(entryRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> entryService.getEntryById(99L)
        );

        assertEquals(
                "Film entry not found with ID: 99",
                exception.getMessage()
        );
    }

    @Test
    void shouldUpdateEntry() {
        when(entryRepository.findById(1L))
                .thenReturn(Optional.of(entry));

        Entry updatedEntry = new Entry();
        updatedEntry.setTitle("Updated Film");
        updatedEntry.setDirectorName("Kavin S");
        updatedEntry.setDescription("Updated description");
        updatedEntry.setFilmUrl("https://example.com/updated");

        when(entryRepository.save(entry)).thenReturn(entry);

        Entry result = entryService.updateEntry(1L, updatedEntry);

        assertEquals("Updated Film", result.getTitle());
        assertEquals("Kavin S", result.getDirectorName());
        assertEquals("Updated description", result.getDescription());
        assertEquals(
                "https://example.com/updated",
                result.getFilmUrl()
        );

        verify(entryRepository).save(entry);
    }

    @Test
    void shouldDeleteEntry() {
        when(entryRepository.findById(1L))
                .thenReturn(Optional.of(entry));

        entryService.deleteEntry(1L);

        verify(entryRepository).delete(entry);
    }
}