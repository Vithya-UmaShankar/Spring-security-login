package com.example.login.repository;

/**
 * Interface to send an email
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
public interface EmailSender {

  /**
   * This method is used to send an email to an User
   *
   * @param to    Receiver of the email
   * @param email Body of the email
   */
  void send(String to, String email);
}
