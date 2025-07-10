
package com.mdsohail.EmailSender.controller;

import com.mdsohail.EmailSender.model.EmailRequest;
import com.mdsohail.EmailSender.model.EmailStatus;
import com.mdsohail.EmailSender.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired private EmailService emailService;

    @PostMapping("/send")
    public EmailStatus sendEmail(@RequestBody EmailRequest request) {
        return emailService.sendEmail(request);
    }

    @GetMapping("/status/{requestId}")
    public ResponseEntity<?> getStatus(@PathVariable String requestId) {
        EmailStatus status = emailService.getStatus(requestId);
        if (status == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Request ID not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
        return ResponseEntity.ok(status);
    }

}
