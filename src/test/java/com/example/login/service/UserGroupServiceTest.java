package com.example.login.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.login.model.Account;
import com.example.login.model.AccountType;
import com.example.login.model.User;
import com.example.login.model.UserGroup;
import com.example.login.model.UserRole;
import com.example.login.repository.AccountRepository;
import com.example.login.repository.UserGroupRepository;
import com.example.login.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserGroupServiceTest {

  @Mock
  UserGroupRepository userGroupRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  AccountRepository accountRepository;

  @InjectMocks
  UserGroupService userGroupService;

  User user = new User(), user1 = new User();
  Account account = new Account();
  UserGroup userGroup = new UserGroup(), userGroup1 = new UserGroup(), userGroup2 = new UserGroup();
  List<UserGroup> userGroupList = new ArrayList<>();
  List<User> userList = new ArrayList<>();

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

    userList.add(user);
    userList.add(user1);

    accountRepository.save(account);
    userRepository.save(user);
    userGroupRepository.save(userGroup);
  }

  @Test
  void createUserGroupAddUser() {
    when(userRepository.existsUserByContactEmail(user.getContactEmail())).thenReturn(true);
    when(userRepository.findByContactEmail(user.getContactEmail())).thenReturn(user);
    when(userGroupRepository.existsUserGroupByGroupName(userGroup.getGroupName())).thenReturn(true);
    when(userGroupRepository.findByGroupName(userGroup.getGroupName())).thenReturn(userGroup);
    UserGroup userGroupFetch = userGroupService.addUserToUserGroup(user.getContactEmail(),
        userGroup.getGroupName());

    assertThat(userGroupFetch).isEqualTo(userGroup);
  }

  @Test
  void getAllUserGroups() {
    when(userGroupRepository.findAll()).thenReturn(userGroupList);
    List<UserGroup> userGroupListFetch = userGroupService.getAllUserGroups();

    assertThat(userGroupListFetch).isEqualTo(userGroupList);
  }

  @Test
  void getUserGroupsByGroupId() {
    when(userGroupRepository.findUserGroupById(userGroup.getId())).thenReturn(userGroup);
    UserGroup userGroupFetch = userGroupService.getUserGroupsByGroupId(userGroup.getId());

    assertThat(userGroupFetch).isEqualTo((userGroup));
  }

  @Test
  void getAllUsersByGroupId() {
    when(userGroupRepository.existsById(userGroup.getId())).thenReturn(true);
    when(userRepository.findUsersByUserGroupsId(userGroup.getId())).thenReturn(userList);
    List<User> userListFetch = userGroupService.getAllUsersByGroupId(userGroup.getId());

    assertThat(userListFetch).isEqualTo(userList);
  }

  @Test
  void getAllUserGroupsByUserId() {
    when(userRepository.existsById(user.getId())).thenReturn(true);
    when(userGroupRepository.findUserGroupsByUsersId(user.getId())).thenReturn(userGroupList);
    List<UserGroup> userGroupListFetch = userGroupService.getAllUserGroupsByUserId(user.getId());

    assertThat(userGroupListFetch).isEqualTo(userGroupList);
  }

  @Test
  void updateUserGroup() {
    when(userGroupRepository.existsById(userGroup.getId())).thenReturn(true);
    when(userGroupRepository.findUserGroupById(userGroup.getId())).thenReturn(userGroup);
    when(userGroupRepository.save(ArgumentMatchers.any(UserGroup.class))).thenReturn(userGroup);
    UserGroup userGroupFetch = userGroupService.updateUserGroup(userGroup);

    assertThat(userGroupFetch).isEqualTo(userGroup);
  }

  @Test
  void deleteUserGroupFromUser() {
    when(userGroupRepository.existsUserGroupByGroupName(userGroup.getGroupName())).thenReturn(true);
    when(userRepository.existsUserByContactEmail(user.getContactEmail())).thenReturn(true);
    when(userGroupRepository.findByGroupName(userGroup.getGroupName())).thenReturn(userGroup);
    when(userRepository.findByContactEmail(user.getContactEmail())).thenReturn(user);
    userGroupService.deleteUserGroupFromUser(user.getContactEmail(), userGroup.getGroupName());
    UserGroup userGroupFetch = userGroupRepository.findUserGroupById(userGroup.getId());

    assertThat(userGroupFetch).isNull();
  }

  @Test
  void deleteUserGroup() {
    when(userGroupRepository.existsUserGroupByGroupName(userGroup.getGroupName())).thenReturn(true);
    when(userGroupRepository.findByGroupName(userGroup.getGroupName())).thenReturn(userGroup);
    doNothing().when(userGroupRepository).deleteById(userGroup.getId());
    userGroupService.deleteUserGroup(userGroup.getGroupName());
    boolean userGroupExists = userGroupRepository.existsById(userGroup.getId());

    assertThat(userGroupExists).isEqualTo(false);
  }

}