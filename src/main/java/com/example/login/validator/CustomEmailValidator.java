package com.example.login.validator;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * CustomValidation to verify if an email is valid
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
public class CustomEmailValidator implements ConstraintValidator<CustomEmail, String> {

  @Override
  public void initialize(CustomEmail email) {
  }

  @Override
  public boolean isValid(String email, ConstraintValidatorContext cxt) {
    String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
        + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";
    if (email == null) {
      return false;
    }

    return Pattern.matches(regexPattern, email);
  }

}