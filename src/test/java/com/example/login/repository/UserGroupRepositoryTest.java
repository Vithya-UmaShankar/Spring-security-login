package com.example.login.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.login.model.Account;
import com.example.login.model.AccountOwner;
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
class UserGroupRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserGroupRepository userGroupRepository;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  AccountOwnerRepository accountOwnerRepository;

  AccountOwner accountOwner = new AccountOwner();
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
    group1.add(userGroup1);

    Set<UserGroup> group2 = new HashSet<>();
    group2.add(userGroup2);

    account.setAccountType(AccountType.BASIC);
    account.setName("Name");
    account.setDescription("Description");

    accountOwner.setName("Company");
    accountOwner.setPassword("password");
    accountOwner.setAccount(account);

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

    Set<User> userSet = new HashSet<>();
    userSet.add(user);
    userSet.add(user1);

    Set<User> userSet1 = new HashSet<>();
    userSet1.add(user);

    Set<User> userSet2 = new HashSet<>();
    userSet2.add(user);

    userGroup.setUsers(userSet);
    userGroup1.setUsers(userSet1);
    userGroup2.setUsers(userSet2);
    //accountOwnerRepository.save(accountOwner);
    accountRepository.save(account);
    userRepository.save(user);
    userGroupRepository.save(userGroup);
  }

  @Test
  void findUserGroupById() {

    UserGroup fetchUserGroup = null;
    if (userGroupRepository.existsById(userGroup.getId())) {
      fetchUserGroup = userGroupRepository.findUserGroupById(userGroup.getId());
    }

    assertThat(fetchUserGroup.getId()).isEqualTo(userGroup.getId());
  }

  @Test
  void findUserGroupsByUsersId() {

    List<UserGroup> userGroupList = null;

    if (userRepository.existsById(user.getId())) {
      userGroupList = userGroupRepository.findUserGroupsByUsersId(user.getId());
    }

    assertThat(userGroupList.size()).isNotEqualTo(0);
  }

  @Test
  void findByGroupName() {

    UserGroup fetchUserGroup = null;
    if (userGroupRepository.existsUserGroupByGroupName(userGroup.getGroupName())) {
      fetchUserGroup = userGroupRepository.findByGroupName(userGroup.getGroupName());
    }

    assertThat(fetchUserGroup.getGroupName()).isEqualTo(userGroup.getGroupName());
  }

  @Test
  void existsById() {

    boolean userGroupExists = userGroupRepository.existsById(userGroup.getId());

    assertThat(userGroupExists).isEqualTo(true);
  }

  @Test
  void existsUserGroupByGroupName() {

    boolean userGroupExists = userGroupRepository.existsUserGroupByGroupName(
        userGroup.getGroupName());

    assertThat(userGroupExists).isEqualTo(true);
  }

  @Test
  void deleteById() {

    if (userGroupRepository.existsById(userGroup.getId())) {
      Set<User> userList = userGroup.getUsers();
      for (User u : userList) {
        u.getUserGroups().remove(userGroup);
      }
      userGroupRepository.deleteById(userGroup.getId());
    }

    UserGroup fetchUserGroup = null;

    if (userGroupRepository.existsById(userGroup.getId())) {
      fetchUserGroup = userGroupRepository.findUserGroupById(userGroup.getId());
    }

    assertThat(fetchUserGroup).isNull();
  }
}