package com.example.login.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.login.model.Account;
import com.example.login.model.AccountType;
import com.example.login.model.User;
import com.example.login.model.UserGroup;
import com.example.login.model.UserRole;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserGroupRepository userGroupRepository;

  @Autowired
  AccountRepository accountRepository;

  User user = new User(), user1 = new User();
  Account account = new Account();
  UserGroup userGroup = new UserGroup(), userGroup1 = new UserGroup(), userGroup2 = new UserGroup();

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
  }

  @Test
  void findByAccountId_id() {
    accountRepository.save(account);
    userRepository.save(user);
    userRepository.save(user1);

    List<User> user = userRepository.findByAccountId_id(account.getId());

    assertThat(user.size()).isEqualTo(2);
  }

  @Test
  void findUserById() {

    accountRepository.save(account);
    userRepository.save(user);

    User fetchUser = null;
    if (userRepository.existsById(user.getId())) {
      fetchUser = userRepository.findUserById(user.getId());
    }

    assertThat(fetchUser.getId()).isEqualTo(user.getId());
  }

  @Test
  void findByContactEmail() {
    accountRepository.save(account);
    userRepository.save(user);

    User fetchUser = null;
    if (userRepository.existsUserByContactEmail(user.getContactEmail())) {
      fetchUser = userRepository.findByContactEmail(user.getContactEmail());
    }

    assertThat(fetchUser.getContactEmail()).isEqualTo(user.getContactEmail());
  }

  @Test
  void getScreenNameByContactEmail() {
    accountRepository.save(account);
    userRepository.save(user);

    String contactEmail = "";
    if (userRepository.existsUserByContactEmail(user.getContactEmail())) {
      contactEmail = userRepository.getScreenNameByContactEmail(user.getContactEmail());
    }

    assertThat(contactEmail).isEqualTo(user.getScreenName());
  }

  @Test
  void deleteUserByContactEmail() {
    accountRepository.save(account);
    userRepository.save(user);

    if (userRepository.existsUserByContactEmail(user.getContactEmail())) {
      userRepository.deleteUserByContactEmail(user.getContactEmail());
    }

    User fetchUser = null;

    if (userRepository.existsUserByContactEmail(user.getContactEmail())) {
      fetchUser = userRepository.findByContactEmail(user.getContactEmail());
    }

    assertThat(fetchUser).isNull();

  }

  @Test
  void existsById() {
    accountRepository.save(account);
    userRepository.save(user);

    boolean userExists = userRepository.existsById(user.getId());

    assertThat(userExists).isEqualTo(true);
  }

  @Test
  void existsUserByContactEmail() {
    accountRepository.save(account);
    userRepository.save(user);

    boolean userExists = userRepository.existsUserByContactEmail(user.getContactEmail());

    assertThat(userExists).isEqualTo(true);
  }

  @Test
  void findUsersByUserGroupsId() {
    accountRepository.save(account);
    userRepository.save(user);
    userRepository.save(user1);
    userGroupRepository.save(userGroup);
    userGroupRepository.save(userGroup1);
    userGroupRepository.save(userGroup2);

    List<User> userList = null;
    if (userGroupRepository.existsById(userGroup2.getId())) {
      userList = userRepository.findUsersByUserGroupsId(userGroup2.getId());
    }

    assertThat(userList.size()).isEqualTo(2);
  }
}