package com.example.login.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.login.model.Account;
import com.example.login.model.AccountType;
import com.example.login.model.User;
import com.example.login.model.UserGroup;
import com.example.login.model.UserRole;
import com.example.login.service.UserGroupService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class UserGroupControllerTest {

  @Mock
  UserGroupService userGroupService;
  @Mock
  Model model;
  @Mock
  Principal principal;
  @InjectMocks
  UserGroupController userGroupController;
  User user = new User(), user1 = new User();
  Account account = new Account();
  UserGroup userGroup = new UserGroup(), userGroup1 = new UserGroup(), userGroup2 = new UserGroup();
  List<UserGroup> userGroupList = new ArrayList<>();
  List<User> userList = new ArrayList<>();
  Set<User> userSet = new HashSet<>(), userSet1 = new HashSet<>(), userSet2 = new HashSet<>();
  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    userGroup.setGroupName("Group1");
    userGroup1.setGroupName("Group2");
    userGroup2.setGroupName("Group3");

    Set<UserGroup> group1 = new HashSet<>();
    group1.add(userGroup);
    group1.add(userGroup2);

    Set<UserGroup> group2 = new HashSet<>();
    group2.add(userGroup1);
    group2.add(userGroup2);
    group2.add(userGroup);

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

    userSet.add(user);
    userSet.add(user1);

    userSet1.add(user);

    userSet2.add(user);

    userGroup.setUsers(userSet);
    userGroup1.setUsers(userSet1);
    userGroup2.setUsers(userSet2);

    userList.add(user);
    userList.add(user1);

    userGroupList.add(userGroup);
    userGroupList.add(userGroup1);
    userGroupList.add(userGroup2);
    mockMvc = MockMvcBuilders.standaloneSetup(userGroupController).build();
  }

  @Test
  void getAllUserGroups() throws Exception {
    when(userGroupService.getAllUserGroups()).thenReturn(userGroupList);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/userGroup/")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));
  }

  @Test
  void getUserGroupsByGroupId() throws Exception {
    when(userGroupService.getUserGroupsByGroupId(userGroup.getId())).thenReturn(userGroup);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/userGroup/" + userGroup.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.groupName", is(userGroup.getGroupName())));
  }

  @Test
  void getAllUsersByGroupId() throws Exception {
    when(userGroupService.getAllUsersByGroupId(userGroup.getId())).thenReturn(userList);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/userGroup/users/" + userGroup.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  void getAllUserGroupsByUserId() throws Exception {
    when(userGroupService.getAllUserGroupsByUserId(user.getId())).thenReturn(userGroupList);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/userGroup/userGroups/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));
  }

  @Test
  void updateUserGroup() throws Exception {
    /*
    userGroup.setGroupName("Group A");

    doReturn(userGroup).when(userGroupService).updateUserGroup(userGroup);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/userGroup/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJson(userGroup)))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.groupName", is("Group A")));
     */
  }
}