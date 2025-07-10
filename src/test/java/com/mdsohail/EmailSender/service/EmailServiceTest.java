
package com.mdsohail.EmailSender.service;

import com.mdsohail.EmailSender.model.EmailRequest;
import com.mdsohail.EmailSender.model.EmailStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
        import static org.junit.jupiter.api.Assertions.*;

public class EmailServiceTest {

    @Mock
    private MockEmailProviderA providerA;

    @Mock
    private MockEmailProviderB providerB;

    @Mock
    private RateLimiter rateLimiter;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSuccessfulSendUsingProviderA() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setTo("test@example.com");
        request.setSubject("Hello");
        request.setBody("Body");
        request.setRequestId("req-1");

        when(rateLimiter.allowRequest("test@example.com")).thenReturn(true);
        when(providerA.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        EmailStatus status = emailService.sendEmail(request);
        assertEquals(EmailStatus.SUCCESS, status);
    }

//Test fallback when both providers fail
    @Test
    public void testFallbackFailsBothProviders() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setTo("user@example.com");
        request.setSubject("Subject");
        request.setBody("Body");
        request.setRequestId("fail-1");

        when(rateLimiter.allowRequest("user@example.com")).thenReturn(true);
        when(providerA.sendEmail(anyString(), anyString(), anyString())).thenThrow(new Exception("A failed"));
        when(providerB.sendEmail(anyString(), anyString(), anyString())).thenThrow(new Exception("B failed"));

        EmailStatus status = emailService.sendEmail(request);
        assertEquals(EmailStatus.FAILED, status);
    }

    // Test duplicate request detection
    @Test
    public void testDuplicateRequestIdReturnsDuplicateStatus() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setTo("test@example.com");
        request.setSubject("Hello");
        request.setBody("Body");
        request.setRequestId("duplicate-1");

        when(rateLimiter.allowRequest("test@example.com")).thenReturn(true);
        when(providerA.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        // First call: should succeed
        EmailStatus first = emailService.sendEmail(request);
        assertEquals(EmailStatus.SUCCESS, first);

        // Second call: should detect duplicate
        EmailStatus second = emailService.sendEmail(request);
        assertEquals(EmailStatus.DUPLICATE, second);
    }

    //  Test rate limiter blocks request
    @Test
    public void testRateLimitExceededReturnsRateLimited() {
        EmailRequest request = new EmailRequest();
        request.setTo("limit@example.com");
        request.setSubject("Hello");
        request.setBody("Body");
        request.setRequestId("rate-1");

        when(rateLimiter.allowRequest("limit@example.com")).thenReturn(false);

        EmailStatus status = emailService.sendEmail(request);
        assertEquals(EmailStatus.RATE_LIMITED, status);
    }


}
