package com.example.login.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.login.model.Account;
import com.example.login.model.AccountType;
import com.example.login.model.ConfirmationToken;
import com.example.login.model.User;
import com.example.login.model.UserGroup;
import com.example.login.model.UserRole;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConfirmationTokenRepositoryTest {

  @Autowired
  ConfirmationTokenRepository confirmationTokenRepository;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserGroupRepository userGroupRepository;

  User user = new User(), user1 = new User();
  Account account = new Account();
  UserGroup userGroup = new UserGroup(), userGroup1 = new UserGroup(), userGroup2 = new UserGroup();
  ConfirmationToken confirmationToken = new ConfirmationToken();

  @AfterEach
  void tearDown() {
    account = null;
    user = user1 = null;
    userGroup = userGroup1 = userGroup2 = null;
    confirmationToken = null;
  }

  @BeforeEach
  void setUp() {
    userGroup.setGroupName("Group 1");
    userGroup1.setGroupName("Group 2");
    userGroup2.setGroupName("Group 3");

    Set<UserGroup> group1 = new HashSet<>();
    group1.add(userGroup);
    group1.add(userGroup2);

    Set<UserGroup> group2 = new HashSet<>();
    group2.add(userGroup1);
    group2.add(userGroup2);

    account.setAccountType(AccountType.BASIC);
    account.setName("Name");
    account.setDescription("Description");

    user.setUserType(UserRole.USER);
    user.setIsAdmin("y");
    user.setContactEmail("abc@gmail.com");
    user.setScreenName("Abc");
    user.setName("Name");
    user.setDescription("Description");
    user.setAccountId(account);
    user.setUserGroups(group1);

    user1.setUserType(UserRole.ADMIN);
    user1.setIsAdmin("n");
    user1.setContactEmail("asdf@gmail.com");
    user1.setScreenName("Asdf");
    user1.setName("Name");
    user1.setDescription("Description");
    user1.setAccountId(account);
    user1.setUserGroups(group2);

    confirmationToken.setToken(UUID.randomUUID().toString());
    confirmationToken.setCreatedAt(LocalDateTime.now());
    confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
    confirmationToken.setUserId(user);
  }

  @Test
  void findByToken() {
    accountRepository.save(account);
    userGroupRepository.save(userGroup);
    userRepository.save(user);
    confirmationTokenRepository.save(confirmationToken);

    ConfirmationToken fetchConfirmationToken = null;
    if (confirmationTokenRepository.existsConfirmationTokenByToken(confirmationToken.getToken())) {
      fetchConfirmationToken = confirmationTokenRepository.findByToken(
          confirmationToken.getToken());
    }

    assertThat(fetchConfirmationToken.getToken()).isEqualTo(confirmationToken.getToken());
  }

  @Test
  void existsConfirmationTokenByToken() {
    accountRepository.save(account);
    userGroupRepository.save(userGroup);
    userRepository.save(user);
    confirmationTokenRepository.save(confirmationToken);

    boolean confirmationTokenExists = confirmationTokenRepository.existsConfirmationTokenByToken(
        confirmationToken.getToken());
    assertThat(confirmationTokenExists).isEqualTo(true);
  }

  @Test
  void updateConfirmedAt() {
    accountRepository.save(account);
    userGroupRepository.save(userGroup);
    userRepository.save(user);
    confirmationTokenRepository.save(confirmationToken);

    if (confirmationTokenRepository.existsConfirmationTokenByToken(confirmationToken.getToken())) {
      confirmationTokenRepository.updateConfirmedAt(confirmationToken.getToken(),
          LocalDateTime.now());
    }

    ConfirmationToken fetchConfirmationToken = null;
    if (confirmationTokenRepository.existsConfirmationTokenByToken(confirmationToken.getToken())) {
      fetchConfirmationToken = confirmationTokenRepository.findByToken(
          confirmationToken.getToken());
    }

    assertThat(fetchConfirmationToken.getConfirmedAt()).isEqualTo(
        confirmationToken.getConfirmedAt());
  }
}