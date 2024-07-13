package com.expdiary.journalApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Service;

import com.expdiary.journalApp.entity.JournalEntry;

@Service
public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {

}