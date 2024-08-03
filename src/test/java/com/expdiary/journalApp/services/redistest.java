package com.expdiary.journalApp.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class redistest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    @Disabled
    void test() {
        redisTemplate.opsForValue().set("email", "email@gmial.com");
        Object email = redisTemplate.opsForValue().get("email");
    }
}
