package com.project.onlybuns.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LogInService {

    private static final int MAX_RETRIES = 5;
    private static final long COOLDOWN_MINUTES = 1;

    private final ConcurrentHashMap<String, AttemptData> ipTracker = new ConcurrentHashMap<>();

    public boolean isRateLimited(String ip) {
        AttemptData data = ipTracker.get(ip);
        if (data == null) return false;

        long now = System.currentTimeMillis();
        if (now - data.lastAttemptTime > TimeUnit.MINUTES.toMillis(COOLDOWN_MINUTES)) {
            clearAttempts(ip);
            return false;
        }

        return data.failedAttempts >= MAX_RETRIES;
    }

    public void recordFailure(String ip) {
        AttemptData data = ipTracker.getOrDefault(ip, new AttemptData());
        data.failedAttempts++;
        data.lastAttemptTime = System.currentTimeMillis();
        ipTracker.put(ip, data);
    }

    public void clearAttempts(String ip) {
        ipTracker.remove(ip);
    }

    private static class AttemptData {
        int failedAttempts = 0;
        long lastAttemptTime = System.currentTimeMillis();
    }
}
