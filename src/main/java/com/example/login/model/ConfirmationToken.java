package com.example.login.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Denotes the model for the activation token that is generated and sent to the user's registered
 * email
 *
 * <p>
 * <b>Note:</b>
 * <p>
 * Repository: {@link com.example.login.repository.ConfirmationTokenRepository
 * ConfirmationTokenRepository}
 * <p>
 * Service: {@link com.example.login.service.ConfirmationTokenService ConfirmationTokenService}
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotBlank
  @Column(name = "token", columnDefinition = "varchar(255)", nullable = false)
  private String token;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "expires_at", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
  private LocalDateTime expiresAt;

  @Column(name = "confirmed_at")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
  private LocalDateTime confirmedAt;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "user_id")
  private User userId;
}
