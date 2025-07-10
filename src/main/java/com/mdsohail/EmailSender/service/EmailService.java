
package com.mdsohail.EmailSender.service;

import com.mdsohail.EmailSender.model.EmailRequest;
import com.mdsohail.EmailSender.model.EmailStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailService {

    @Autowired private MockEmailProviderA providerA;
    @Autowired private MockEmailProviderB providerB;
    @Autowired private RateLimiter rateLimiter;

    private final Map<String, EmailStatus> requestMap = new ConcurrentHashMap<>();

    public EmailStatus sendEmail(EmailRequest request) {
        // Idempotency
        if (requestMap.containsKey(request.getRequestId())) {
            return EmailStatus.DUPLICATE;
        }

        // Rate limiting
        if (!rateLimiter.allowRequest(request.getTo())) {
            requestMap.put(request.getRequestId(), EmailStatus.RATE_LIMITED);
            return EmailStatus.RATE_LIMITED;
        }

        EmailProvider[] providers = { providerA, providerB };

        for (EmailProvider provider : providers) {
            int retries = 3;
            int delay = 500; // ms
            for (int i = 0; i < retries; i++) {
                try {
                    boolean sent = provider.sendEmail(
                            request.getTo(), request.getSubject(), request.getBody());
                    if (sent) {
                        requestMap.put(request.getRequestId(), EmailStatus.SUCCESS);
                        return EmailStatus.SUCCESS;
                    }
                } catch (Exception e) {
                    try { Thread.sleep((long) Math.pow(2, i) * delay); } catch (InterruptedException ignored) {}
                }
            }
        }

        requestMap.put(request.getRequestId(), EmailStatus.FAILED);
        return EmailStatus.FAILED;
    }

    public EmailStatus getStatus(String requestId) {
        return requestMap.getOrDefault(requestId, null);
    }
}
