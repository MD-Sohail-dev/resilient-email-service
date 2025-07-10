package com.mdsohail.EmailSender.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdsohail.EmailSender.model.EmailRequest;
import com.mdsohail.EmailSender.model.EmailStatus;
import com.mdsohail.EmailSender.service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmailController.class)
public class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSendEmailReturnsSuccess() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setTo("test@example.com");
        request.setSubject("Hello");
        request.setBody("Test Body");
        request.setRequestId("req-1");

        Mockito.when(emailService.sendEmail(any(EmailRequest.class)))
                .thenReturn(EmailStatus.SUCCESS);

        mockMvc.perform(post("/api/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("\"SUCCESS\"")); // updated
    }

    @Test
    public void testSendEmailRateLimited() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setTo("limited@example.com");
        request.setSubject("Subject");
        request.setBody("Body");
        request.setRequestId("rl-123");

        Mockito.when(emailService.sendEmail(any(EmailRequest.class)))
                .thenReturn(EmailStatus.RATE_LIMITED);

        mockMvc.perform(post("/api/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("\"RATE_LIMITED\"")); // updated
    }

    @Test
    public void testSendEmailDuplicate() throws Exception {
        EmailRequest request = new EmailRequest();
        request.setTo("user@example.com");
        request.setSubject("Subject");
        request.setBody("Body");
        request.setRequestId("dup-123");

        Mockito.when(emailService.sendEmail(any(EmailRequest.class)))
                .thenReturn(EmailStatus.DUPLICATE);

        mockMvc.perform(post("/api/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("\"DUPLICATE\"")); // updated
    }
}
