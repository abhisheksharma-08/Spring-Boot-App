package com.expdiary.journalApp.entity;

import java.time.LocalDateTime;
import java.util.PrimitiveIterator;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.expdiary.journalApp.enums.Sentiment;
import com.mongodb.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "journalEntry")
// @Getter
// @Setter
@Data
@NoArgsConstructor
public class JournalEntry {
    @Id
    private ObjectId id;
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;
    private Sentiment sentiment;

}
