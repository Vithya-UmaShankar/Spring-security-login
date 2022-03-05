package com.example.login.service;

import com.example.login.exception.ResourceNotFoundException;
import com.example.login.model.Account;
import com.example.login.model.ConfirmationToken;
import com.example.login.model.Provider;
import com.example.login.model.User;
import com.example.login.model.UserGroup;
import com.example.login.model.UserRole;
import com.example.login.repository.AccountRepository;
import com.example.login.repository.UserGroupRepository;
import com.example.login.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service of {@link com.example.login.model.User User} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  UserGroupRepository userGroupRepository;

  @Autowired
  UserGroupService userGroupService;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  ConfirmationTokenService confirmationTokenService;

  /**
   * This method is used to find the UserDetails using email
   *
   * @param contactEmail Email ID with which the userList registered.
   */
  @Override
  public UserDetails loadUserByUsername(String contactEmail) throws UsernameNotFoundException {
    if (!userRepository.existsUserByContactEmail(contactEmail)) {
      throw new UsernameNotFoundException("User with email " + contactEmail + "not found");
    }
    return userRepository.findByContactEmail(contactEmail);
  }

  /**
   * This method is used to return all the users in User table
   *
   * @return Returns the {@link List} of Users.
   */
  public List<User> findAllUser() {
    return userRepository.findAll();
  }

  /**
   * This method is used to find a User using id
   *
   * @param userId UUID of a {@link com.example.login.model.User User}.
   * @return Returns the User.
   */
  public User getUserByUserId(UUID userId) {
    if (userRepository.existsById(userId)) {
      return userRepository.findUserById(userId);
    } else {
      throw new ResourceNotFoundException("User Id [" + userId + "] doesn't exist");
    }
  }

  /**
   * This method is used to create a new User
   *
   * @param user        Instance of User that has to be inserted.
   * @param accountName Name of an {@link com.example.login.model.Account account}.
   * @return Returns the instance of User after it has been inserted.
   */
  public User createUser(User user, String accountName) {
    if (accountRepository.existsByName(accountName)) {
      user.setAccountId(accountRepository.findAccountByName(accountName));
      return userRepository.save(user);
    } else {
      throw new ResourceNotFoundException("Account [" + accountName + "] not found!");
    }

  }

  /**
   * This method is used to delete a User
   *
   * @param contactEmail email of a User.
   */
  public void deleteUser(String contactEmail) {
    if (userRepository.existsUserByContactEmail(contactEmail)) {
      User user = userRepository.findByContactEmail(contactEmail);
      Set<UserGroup> userGroupList = user.getUserGroups();
      for (UserGroup userGroup : userGroupList) {
        Set<User> u = userGroup.getUsers();
        userGroup.getUsers().remove(user);
      }

      userRepository.deleteUserByContactEmail(contactEmail);
    } else {
      throw new ResourceNotFoundException("User email [" + contactEmail + "] doesn't exist");
    }
  }

  /**
   * This method is used to update a new User
   *
   * @param user Instance of User that has to be updated.
   * @return Returns the instance of User after it has been updated.
   */
  public User updateUser(User user) {

    if (userRepository.existsById(user.getId())) {
      User a = userRepository.findUserById(user.getId());
      a.setTimeUpdated(new Date());

      if (user.getUserType() != null) {
        a.setUserType(user.getUserType());
      }
      if (user.getIsAdmin() != null) {
        a.setIsAdmin(user.getIsAdmin());
      }
      if (user.getContactEmail() != null) {
        a.setContactEmail(user.getContactEmail());
      }
      if (user.getProfileImage() != null) {
        a.setProfileImage(user.getProfileImage());
      }
      if (user.getIsPrimary() != null) {
        a.setIsPrimary(user.getIsPrimary());
      }
      if (user.getCellPhone() != null) {
        a.setCellPhone(user.getCellPhone());
      }
      if (user.getScreenName() != null) {
        a.setScreenName(user.getScreenName());
      }
      if (user.getVisibilityStatus() != null) {
        a.setVisibilityStatus(user.getVisibilityStatus());
      }
      if (user.getAddress() != null) {
        a.setAddress(user.getAddress());
      }
      if (user.getAccountUsercol() != null) {
        a.setAccountUsercol(user.getAccountUsercol());
      }
      return userRepository.save(a);
    } else {
      throw new ResourceNotFoundException("User Id [" + user.getId() + "] doesn't exist");
    }
  }

  /**
   * This method is used to encrypt the password and save the User object passed by {@link
   * com.example.login.service.RegistrationService RegistrationService}. {@link
   * com.example.login.model.ConfirmationToken ConfirmationToken} is also generated.
   *
   * @param user Instance of User that has to be saved.
   * @return Returns the token that is generated.
   */
  public String signUpUser(User user) {
    if (userRepository.existsUserByContactEmail(user.getContactEmail())) {
      User u = userRepository.findByContactEmail(user.getContactEmail());
      ;
      if (u.isEnabled()) {
        return "email exists";
      } else {
        return generateConfirmationToken(u);
      }
    }

    String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);
    User createdUser = userRepository.save(user);

    return generateConfirmationToken(createdUser);
  }

  /**
   * This method is used to generate a new {@link com.example.login.model.ConfirmationToken
   * ConfirmationToken}.
   *
   * @param user Instance of User for which the {@link com.example.login.model.ConfirmationToken
   *             ConfirmationToken} is being generated.
   * @return Returns the token that is generated.
   */
  public String generateConfirmationToken(User user) {
    String token = UUID.randomUUID().toString();
    ConfirmationToken confirmationToken = new ConfirmationToken();
    confirmationToken.setToken(token);
    confirmationToken.setCreatedAt(LocalDateTime.now());
    confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
    confirmationToken.setUserId(user);
    ConfirmationToken ct = confirmationTokenService.saveConfirmationToken(confirmationToken);

    return token;
  }

  /**
   * This method is used to find the screen name of the user from their email ID
   *
   * @param contactEmail Email of User.
   * @return Returns the screen name
   */
  public String getScreenNameByContactEmail(String contactEmail) {
    if (userRepository.existsUserByContactEmail(contactEmail)) {
      return userRepository.getScreenNameByContactEmail(contactEmail);
    } else {
      throw new ResourceNotFoundException(
          "Can't find account linked with email[" + contactEmail + "]");
    }
  }

  /**
   * This method is used to create a new User. It is called on a successful oAuth2 login
   *
   * @param screenName   Name that is registered in oAuth provider with which the userList logged
   *                     in.
   * @param contactEmail Email that is linked to the oAuth provider with which the userList logged
   *                     in.
   */
  public User processOAuthPostLogin(String screenName, String contactEmail, String providerName) {

    if (userRepository.existsUserByScreenName(screenName)) {
      User user = userRepository.findUserByScreenName(screenName);
      return user;
    } else {
      String description = "User who registered using ";
      User user = new User();
      user.setScreenName(screenName);
      user.setContactEmail(contactEmail);
      user.setEnabled(true);
      user.setUserType(UserRole.USER);
      user.setIsAdmin("n");
      user.setName("User");

      if (providerName.equals("Google")) {
        user.setProvider(Provider.GOOGLE);
        description = description + "Google";
      } else if (providerName.equals("Facebook")) {
        user.setProvider(Provider.FACEBOOK);
        description = description + "Facebook";
      }

      Account account = accountRepository.findAccountByName("CompanyA");
      user.setAccountId(account);

      user.setDescription(description);
      return userRepository.save(user);
    }
  }

  /**
   * Find UUID given email
   *
   * @param contactEmail email of the User
   * @return User UUID
   */
  public UUID getIdByContactEmail(String contactEmail) {
    if (userRepository.existsUserByContactEmail(contactEmail)) {
      return userRepository.findByContactEmail(contactEmail).getId();
    } else {
      throw new ResourceNotFoundException("Email [" + contactEmail + "] doesn't exist");
    }
  }
}