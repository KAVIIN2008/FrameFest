package com.shortfilm.service;

import com.shortfilm.entity.Entry;
import com.shortfilm.repository.EntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EntryService {

    private final EntryRepository entryRepository;

    public EntryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public List<Entry> getAllEntries() {
        return entryRepository.findAll();
    }

    public Entry getEntryById(Long id) {
        return entryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Film entry not found with ID: " + id));
    }

    @Transactional
    public Entry createEntry(Entry entry) {
        return entryRepository.save(entry);
    }

    @Transactional
    public Entry updateEntry(Long id, Entry updatedEntry) {
        Entry existingEntry = getEntryById(id);

        existingEntry.setTitle(updatedEntry.getTitle());
        existingEntry.setDirectorName(updatedEntry.getDirectorName());
        existingEntry.setDescription(updatedEntry.getDescription());
        existingEntry.setFilmUrl(updatedEntry.getFilmUrl());

        return entryRepository.save(existingEntry);
    }

    @Transactional
    public void deleteEntry(Long id) {
        Entry existingEntry = getEntryById(id);
        entryRepository.delete(existingEntry);
    }
}
