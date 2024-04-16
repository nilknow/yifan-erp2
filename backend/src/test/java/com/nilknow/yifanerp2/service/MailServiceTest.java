package com.nilknow.yifanerp2.service;

import org.junit.jupiter.api.Test;

class MailServiceTest {
    @Test
    public void send_shouldSendEmailToMultipleRecipients() {
        // Setup
        String[] recipientEmails = new String[]{"494939649@qq.com", "maxwangein@gmail.com"};
        String expectedSubject = "Test Subject";
        String expectedBody = "Test Body";

        // Mock Transport.send() to avoid actually sending the email

        // Create MailService instance
        MailService mailService = new MailService();

        // Call send() method
        mailService.send(String.join(",", recipientEmails), expectedSubject, expectedBody);
    }
}