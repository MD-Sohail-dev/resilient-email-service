
package com.mdsohail.EmailSender.service;

import org.springframework.stereotype.Component;

@Component
public class MockEmailProviderA implements EmailProvider {
    public boolean sendEmail(String to, String subject, String body) throws Exception {
        // Simulate 70% success
        if (Math.random() > 0.3) {
            System.out.println("[ProviderA] Email sent to " + to);
            return true;
        }
        throw new Exception("[ProviderA] Failed to send");
    }

    public String getName() {
        return "ProviderA";
    }
}
