package com.example.login.service;

import com.example.login.exception.ResourceNotFoundException;
import com.example.login.model.Account;
import com.example.login.model.ConfirmationToken;
import com.example.login.model.Provider;
import com.example.login.model.Registration;
import com.example.login.model.User;
import com.example.login.model.UserRole;
import com.example.login.repository.AccountRepository;
import com.example.login.repository.ConfirmationTokenRepository;
import com.example.login.repository.EmailSender;
import com.example.login.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service of {@link com.example.login.model.Registration Registration} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class RegistrationService {

  @Autowired
  UserService userService;

  @Autowired
  ConfirmationTokenService confirmationTokenService;

  @Autowired
  ConfirmationTokenRepository confirmationTokenRepository;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  EmailSender emailSender;

  /**
   * This method is used to gather the details from the sign-up form and use it to create a User
   *
   * @param registration The object that holds the values passed by the sign-up form.
   * @return Returns either "success" or "error" based on whether a User with the same email id
   * exists.
   */
  public String register(Registration registration) {
    User user = new User();

    user.setScreenName(registration.getScreenName());
    user.setContactEmail(registration.getContactEmail());
    user.setPassword(registration.getPassword());
    user.setIsAdmin(registration.getIsAdmin());
    if (registration.getIsAdmin().equals("n")) {
      user.setUserType(UserRole.USER);
    } else {
      user.setUserType(UserRole.ADMIN);
    }
    user.setProvider(Provider.LOCAL);
    user.setName("User");
    user.setDescription("User who registered locally");

    if (!accountRepository.existsByName(registration.getAccountName())) {
      throw new ResourceNotFoundException(
          "Account [" + registration.getAccountName() + "] not found!");
    }

    Account account = accountRepository.findAccountByName(registration.getAccountName());
    user.setAccountId(account);

    String token = userService.signUpUser(user);

    if (token.equals("email exists")) {
      return "error";
    }

    String link = "https://localhost:8080/api/v1/confirmationToken/confirm/" + token;

    //Send mail
    emailSender.send(registration.getContactEmail(),
        buildEmail(registration.getScreenName(), link));

    return "success";
  }

  /**
   * This method is used to validate an User
   *
   * @param token UUID generated and sent to the user to validate their account.
   * @return Returns a message stating if the user was validated or if there were any errors
   */
  public String confirmToken(String token) {
    if (!confirmationTokenRepository.existsConfirmationTokenByToken(token)) {
      return "Oops! Token not found";
    }

    ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);
    User user = userRepository.findUserById(confirmationToken.getUserId().getId());

    //Check if user already verified account with different ConfirmationToken
    boolean enabled = user.isEnabled();

    if (enabled || confirmationToken.getConfirmedAt() != null) {
      return "Account is already verified. Go ahead and login";
    }

    LocalDateTime expiredAt = confirmationToken.getExpiresAt();

    if (expiredAt.isBefore(LocalDateTime.now())) {
      return "Token has expired. Try logging in again to regenerate token.";
    }

    confirmationTokenService.updateTokenConfirmedAt(token, LocalDateTime.now());

    user.setEnabled(true);
    userRepository.save(user);

    return "Account verified. Go ahead and login";
  }

  /**
   * This method is used to build the body of the email
   *
   * @param screenName Screen name of the userList.
   * @param link       Activation link that is sent in email.
   * @return String containing the HTML block.
   */
  private String buildEmail(String screenName, String link) {
    return
        "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n"
            +
            "\n" +
            "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
            "\n" +
            "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
            +
            "    <tbody><tr>\n" +
            "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
            "        \n" +
            "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n"
            +
            "          <tbody><tr>\n" +
            "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
            "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
            +
            "                  <tbody><tr>\n" +
            "                    <td style=\"padding-left:10px\">\n" +
            "                  \n" +
            "                    </td>\n" +
            "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n"
            +
            "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n"
            +
            "                    </td>\n" +
            "                  </tr>\n" +
            "                </tbody></table>\n" +
            "              </a>\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "        </tbody></table>\n" +
            "        \n" +
            "      </td>\n" +
            "    </tr>\n" +
            "  </tbody></table>\n" +
            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
            +
            "    <tbody><tr>\n" +
            "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
            "      <td>\n" +
            "        \n" +
            "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
            +
            "                  <tbody><tr>\n" +
            "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
            "                  </tr>\n" +
            "                </tbody></table>\n" +
            "        \n" +
            "      </td>\n" +
            "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
            "    </tr>\n" +
            "  </tbody></table>\n" +
            "\n" +
            "\n" +
            "\n" +
            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
            +
            "    <tbody><tr>\n" +
            "      <td height=\"30\"><br></td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
            "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n"
            +
            "        \n" +
            "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi "
            + screenName
            + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\""
            + link
            + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>"
            +
            "        \n" +
            "      </td>\n" +
            "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "      <td height=\"30\"><br></td>\n" +
            "    </tr>\n" +
            "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
            "\n" +
            "</div></div>";
  }
}
