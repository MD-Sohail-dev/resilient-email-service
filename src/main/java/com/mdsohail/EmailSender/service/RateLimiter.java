

package com.mdsohail.EmailSender.service;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RateLimiter {
    private final Map<String, List<Long>> requestTimes = new HashMap<>();
    private final long TIME_WINDOW_MS = 60000; // 1 minute
    private final int MAX_REQUESTS = 5;

    public boolean allowRequest(String to) {
        long now = System.currentTimeMillis();
        requestTimes.putIfAbsent(to, new ArrayList<>());

        List<Long> timestamps = requestTimes.get(to);
        timestamps.removeIf(t -> now - t > TIME_WINDOW_MS);

        if (timestamps.size() >= MAX_REQUESTS) {
            return false;
        }
        timestamps.add(now);
        return true;
    }
}
