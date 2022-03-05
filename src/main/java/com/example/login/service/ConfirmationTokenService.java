package com.example.login.service;

import com.example.login.exception.ResourceNotFoundException;
import com.example.login.model.ConfirmationToken;
import com.example.login.repository.ConfirmationTokenRepository;
import java.time.LocalDateTime;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service of {@link com.example.login.model.ConfirmationToken ConfirmationToken} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Service
public class ConfirmationTokenService {

  @Autowired
  ConfirmationTokenRepository confirmationTokenRepository;

  /**
   * This method is used to save a new ConfirmationToken
   *
   * @param token UUID that is generated to activate a userList.
   */
  public ConfirmationToken saveConfirmationToken(@Valid ConfirmationToken token) {
    return confirmationTokenRepository.save(token);
  }

  /**
   * This method is used to find a ConfirmationToken using token
   *
   * @param token UUID that is generated to activate a user.
   * @return Returns a ConfirmationToken.
   */
  public ConfirmationToken getToken(String token) {
    if (confirmationTokenRepository.existsConfirmationTokenByToken(token)) {
      return confirmationTokenRepository.findByToken(token);
    } else {
      throw new ResourceNotFoundException("ConfirmationToken [" + token + "] not found");
    }
  }

  /**
   * This method is used to activate a User.
   *
   * @param token UUID generated to validate an User.
   */
  public void updateTokenConfirmedAt(String token, LocalDateTime time) {
    if (confirmationTokenRepository.existsConfirmationTokenByToken(token)) {
      confirmationTokenRepository.updateConfirmedAt(token, time);
    } else {
      throw new ResourceNotFoundException("ConfirmationToken [" + token + "] not found");
    }
  }

}
