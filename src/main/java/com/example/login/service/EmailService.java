package com.example.login.service;

import com.example.login.repository.EmailSender;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service to configure an email
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

  @Autowired
  JavaMailSender mailSender;

  /**
   * This method is used to send an email withh the ConfirmationTokenn to the userList.
   *
   * @param to    Receiver of the email
   * @param email Body of the email
   */
  @Override
  @Async
  public void send(String to, String email) {
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setText(email, true);
      helper.setTo(to);
      helper.setSubject("Confirm your email");
      helper.setFrom("hello@innectotoken.com");
      mailSender.send(mimeMessage);
    } catch (MessagingException e) {
      throw new IllegalStateException("Failed to send email");
    }
  }
}
