package ru.kpfu.itis.issst.search.service;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * author: Nikita
 * since: 15.05.2014
 */
@Service
public class UIDGenerator {
    private Random random;

    public String getUID() {
        return String.valueOf(random.nextLong()) + String.valueOf(System.currentTimeMillis());
    }
}
