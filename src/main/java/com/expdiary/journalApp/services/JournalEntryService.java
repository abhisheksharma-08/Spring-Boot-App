package com.expdiary.journalApp.services;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expdiary.journalApp.entity.JournalEntry;
import com.expdiary.journalApp.entity.User;
import com.expdiary.journalApp.repository.JournalEntryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    UserService userService;

    // create
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        } catch (Exception e) {
            log.error("Exception", e);
        }

    }

    public void saveEntry(JournalEntry journalEntry) {
        try {
            journalEntryRepository.save(journalEntry);
        } catch (Exception e) {
            log.error("Exception", e);
        }

    }

    // get
    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    // getbyid
    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    // delete
    @Transactional

    public boolean deleteById(ObjectId id, String userName) {
        boolean remove = false;
        try {

            User user = userService.findByUserName(userName);
            remove = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (remove) {
                userService.saveEntry(user);
                journalEntryRepository.deleteById(id);
            }
            return remove;

        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An Error occurred while deleting the entry", e);
        }

    }

}
