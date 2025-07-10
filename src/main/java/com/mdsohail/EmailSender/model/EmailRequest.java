
package com.mdsohail.EmailSender.model;

import lombok.Data;

@Data
public class EmailRequest {
    private String to;
    private String subject;
    private String body;
    private String requestId; // for idempotency
}
