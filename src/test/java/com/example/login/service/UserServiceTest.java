package com.example.login.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.login.model.Account;
import com.example.login.model.AccountOwner;
import com.example.login.model.AccountType;
import com.example.login.model.ConfirmationToken;
import com.example.login.model.User;
import com.example.login.model.UserGroup;
import com.example.login.model.UserRole;
import com.example.login.repository.AccountOwnerRepository;
import com.example.login.repository.AccountRepository;
import com.example.login.repository.UserGroupRepository;
import com.example.login.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  UserGroupRepository userGroupRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  AccountRepository accountRepository;

  @Mock
  AccountOwnerRepository accountOwnerRepository;

  @Mock
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Mock
  ConfirmationTokenService confirmationTokenService;

  @Mock
  UserGroupService userGroupService;

  @InjectMocks
  UserService userService;

  AccountOwner accountOwner = new AccountOwner();
  User user = new User(), user1 = new User();
  Account account = new Account();
  UserGroup userGroup = new UserGroup(), userGroup1 = new UserGroup(), userGroup2 = new UserGroup();
  List<UserGroup> userGroupList = new ArrayList<>();
  List<User> userList = new ArrayList<>();
  ConfirmationToken confirmationToken = new ConfirmationToken();

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
  void loadUserByUsername() {
    when(userRepository.existsUserByContactEmail(user.getContactEmail())).thenReturn(true);
    when(userRepository.findByContactEmail(user.getContactEmail())).thenReturn(user);
    UserDetails userDetails = userService.loadUserByUsername(user.getContactEmail());

    assertThat(userDetails.getUsername()).isEqualTo(user.getContactEmail());
  }

  @Test
  void findAllUser() {
    when(userRepository.findAll()).thenReturn(userList);
    List<User> userFetch = userService.findAllUser();

    assertThat(userFetch).isEqualTo(userList);
  }

  @Test
  void getUserByUserId() {
    when(userRepository.existsById(user.getId())).thenReturn(true);
    when(userRepository.findUserById(user.getId())).thenReturn(user);
    User userFetch = userService.getUserByUserId(user.getId());

    assertThat(userFetch).isEqualTo(user);
  }

  @Test
  void createUser() {
    when(accountRepository.existsByName(account.getName())).thenReturn(true);
    when(accountRepository.findAccountByName(account.getName())).thenReturn(account);
    when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
    User userFetch = userService.createUser(user, account.getName());

    assertThat(userFetch).isEqualTo(user);
  }

  @Test
  void deleteUser() {
    when(userRepository.existsUserByContactEmail(user.getContactEmail())).thenReturn(true);
    when(userRepository.findByContactEmail(user.getContactEmail())).thenReturn(user);
    doNothing().when(userRepository).deleteUserByContactEmail(user.getContactEmail());
    userService.deleteUser(user.getContactEmail());

    assertThat(userRepository.existsById(user.getId())).isEqualTo(false);
  }

  @Test
  void updateUser() {
    when(userRepository.existsById(user.getId())).thenReturn(true);
    when(userRepository.findUserById(user.getId())).thenReturn(user);
    when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
    User userFetch = userService.updateUser(user);

    assertThat(userFetch).isEqualTo(user);
  }

  @Test
  void signUpUser() {
    when(userRepository.existsUserByContactEmail(user.getContactEmail())).thenReturn(true);
    when(userRepository.findByContactEmail(user.getContactEmail())).thenReturn(user);
    String token = userService.signUpUser(user);

    assertThat(token).isNotNull();
  }

  @Test
  void generateConfirmationToken() {
    String token = userService.generateConfirmationToken(user);

    assertThat(token).isNotNull();
  }

  @Test
  void getScreenNameByContactEmail() {
    when(userRepository.getScreenNameByContactEmail(user.getContactEmail())).thenReturn(
        user.getScreenName());
    String screenName = userRepository.getScreenNameByContactEmail(user.getContactEmail());

    assertThat(screenName).isEqualTo(user.getScreenName());
  }

  @Test
  void processOAuthPostLogin() {
    when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
    User userFetch = userService.processOAuthPostLogin(user.getScreenName(), user.getContactEmail(),
        "Google");

    assertThat(userFetch.getContactEmail()).isEqualTo(user.getContactEmail());
  }
}