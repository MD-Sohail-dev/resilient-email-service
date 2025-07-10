
package com.mdsohail.EmailSender.service;

import org.springframework.stereotype.Component;

@Component
public class MockEmailProviderB implements EmailProvider {
    public boolean sendEmail(String to, String subject, String body) throws Exception {
        // Simulate 80% success
        if (Math.random() > 0.2) {
            System.out.println("[ProviderB] Email sent to " + to);
            return true;
        }
        throw new Exception("[ProviderB] Failed to send");
    }

    public String getName() {
        return "ProviderB";
    }
}
