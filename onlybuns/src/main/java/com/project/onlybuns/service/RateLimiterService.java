package com.project.onlybuns.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class RateLimiterService {
    private final Map<String, List<LocalDateTime>> userRequestTimes = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS_PER_MINUTE = 5;

    public boolean isAllowed(String username) {
        LocalDateTime now = LocalDateTime.now();
        userRequestTimes.putIfAbsent(username, new ArrayList<>());

        List<LocalDateTime> timestamps = userRequestTimes.get(username);
        // Ukloni stare zahteve
        timestamps.removeIf(time -> time.isBefore(now.minusMinutes(1)));

        if (timestamps.size() >= MAX_REQUESTS_PER_MINUTE) {
            return false;
        }

        timestamps.add(now);
        return true;
    }
}
