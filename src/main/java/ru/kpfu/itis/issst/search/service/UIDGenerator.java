package ru.kpfu.itis.issst.search.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * author: Nikita
 * since: 15.05.2014
 */
@Service
public class UIDGenerator {
    private Random random;

    @PostConstruct
    private void initialize() {
        random = new Random();
    }

    public String getUID() {
        return String.valueOf(Math.abs(random.nextLong())) + String.valueOf(System.currentTimeMillis());
    }
}
