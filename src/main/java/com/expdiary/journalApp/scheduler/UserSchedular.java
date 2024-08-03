package com.expdiary.journalApp.scheduler;

import java.util.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.expdiary.journalApp.entity.JournalEntry;
import com.expdiary.journalApp.entity.User;
import com.expdiary.journalApp.enums.Sentiment;
import com.expdiary.journalApp.repository.UserRepositoryImpl;
import com.expdiary.journalApp.services.EmailService;
// import com.expdiary.journalApp.services.sentimentanalysis;

@Component
public class UserSchedular {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    // @Autowired
    // private sentimentanalysis sentimentanalysis;

    // @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSemdSaMail() {
        List<User> users = userRepositoryImpl.getUserForSA();
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getSentiment()).collect(Collectors.toList());
            // String entry=String.join(" ", filteredEntries);
            // String sentiment=sentimentanalysis.getSentiment(entry);
            // emailService.sendEmail(user.getEmail(), "Sentiment for 7 DAYS", sentiment);
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment : sentiments) {
                if (sentiment != null)
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null) {
                emailService.sendEmail(user.getEmail(), "Last Seven Days Sentimnets", mostFrequentSentiment.toString());
            }

        }

    }
}
