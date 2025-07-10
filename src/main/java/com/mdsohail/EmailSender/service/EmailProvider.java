package com.mdsohail.EmailSender.service;

public interface EmailProvider {
    boolean sendEmail(String to, String subject, String body) throws Exception;
    String getName();
}
