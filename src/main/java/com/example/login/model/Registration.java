package com.example.login.model;

import com.example.login.validator.CustomEmail;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the user information passed via the sign-in form
 *
 * <p>
 * <b>Note:</b>
 * <p>
 * Service: {@link com.example.login.service.RegistrationService RegistrationService}
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Registration {

  @NotBlank(message = "Name cannot be empty")
  private String screenName;

  @NotBlank(message = "Email cannot be empty")
  @CustomEmail(message = "Invalid Email")
  private String contactEmail;

  @NotBlank(message = "Password cannot be empty")
  @Size(min = 8, max = 16, message = "Password must be between 8 and 16")
  private String password;

  @NotBlank(message = "User must belong to an Account")
  private String accountName;

  @NotBlank(message = "Choose any one option")
  private String isAdmin;
}
