package com.expdiary.journalApp.controller;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expdiary.journalApp.entity.JournalEntry;
import com.expdiary.journalApp.entity.User;
import com.expdiary.journalApp.services.JournalEntryService;
import com.expdiary.journalApp.services.UserService;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {
    @Autowired

    // controller--->serice---->repository
    private JournalEntryService journalEntryService;

    // get

    // Create
    // @PostMapping
    // public JournalEntry createEntry(@RequestBody JournalEntry myEntry) {

    // journalEntryService.saveEntry(myEntry);
    // return myEntry;
    // }

    // // find byid
    // @GetMapping("id/{myid}")
    // public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable
    // ObjectId myid) {
    // Optional<JournalEntry> journalEntry = journalEntryService.findById(myid);
    // if (journalEntry.isPresent()) {
    // return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
    // }
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    // // delete
    // @DeleteMapping("id/{myid}")
    // public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myid)
    // {
    // journalEntryService.deleteById(myid);
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    // // update
    // @PutMapping("id/{myid}")
    // public ResponseEntity<?> updateJournalEntryById(@PathVariable ObjectId myid,
    // @RequestBody JournalEntry newEntry) {
    // JournalEntry old = journalEntryService.findById(myid).orElse(null);

    // if (old != null) {
    // old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ?
    // newEntry.getTitle()
    // : old.getTitle());
    // old.setContent(newEntry.getContent() != null &&
    // !newEntry.getContent().equals("") ? newEntry.getContent()
    // : old.getContent());
    // journalEntryService.saveEntry(old);
    // return new ResponseEntity<>(old, HttpStatus.OK);

    // }

    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    // }

    // @ExceptionHandler(Exception.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // public ResponseEntity<String> handleAllExceptions(Exception ex) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error
    // occurred: " + ex.getMessage());
    // }

    //// by username entry of particular user
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Create
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    // find byid
    @GetMapping("id/{myid}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myid))
                .collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myid);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // delete
    @DeleteMapping("id/{myid}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        boolean remove = journalEntryService.deleteById(myid, userName);
        if (remove) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    // update
    @PutMapping("id/{myid}")
    public ResponseEntity<?> updateJournalEntryById(
            @RequestBody JournalEntry newEntry, @PathVariable ObjectId myid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myid))
                .collect(Collectors.toList());

        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myid);
            if (journalEntry.isPresent()) {
                JournalEntry old = journalEntry.get();
                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle()
                        : old.getTitle());
                old.setContent(
                        newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent()
                                : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
