package com.expdiary.journalApp.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.expdiary.journalApp.entity.User;

public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUserForSA() {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}"));
        query.addCriteria(Criteria.where("sentimentanalysis").is(true));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;

    }

}
