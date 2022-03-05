package com.example.login.repository;

import com.example.login.model.ConfirmationToken;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository of {@link com.example.login.model.ConfirmationToken ConfirmationToken} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Repository
@Transactional
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {

  /**
   * This method is used to find a ConfirmationToken using token.
   *
   * @param token UUID generated to validate a User.
   * @return Returns the ConfirmationToken.
   */
  ConfirmationToken findByToken(String token);

  /**
   * This method is used to check if a ConfirmationToken exists using token.
   *
   * @param token UUID generated to validate an User.
   */
  boolean existsConfirmationTokenByToken(String token);

  /**
   * This method is used to denote that the User has been validated.
   *
   * @param token       UUID generated to validate an User.
   * @param confirmedAt The current time.
   */
  @Modifying
  @Query("UPDATE ConfirmationToken c SET c.confirmedAt = ?2 WHERE c.token = ?1")
  void updateConfirmedAt(String token, LocalDateTime confirmedAt);

}
