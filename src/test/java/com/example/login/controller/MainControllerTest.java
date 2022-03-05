package com.example.login.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.login.model.Account;
import com.example.login.model.AccountType;
import com.example.login.model.User;
import com.example.login.model.UserGroup;
import com.example.login.model.UserRole;
import com.example.login.service.UserGroupService;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class MainControllerTest {

  @InjectMocks
  MainController mainController;
  @Mock
  UserGroupService userGroupService;
  User user = new User();
  Account account = new Account();
  UserGroup userGroup = new UserGroup(), userGroup1 = new UserGroup(), userGroup2 = new UserGroup();
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
    mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
  }

  @Test
  void addUserToUserGroup() throws Exception {
    Principal principal = Mockito.mock(Principal.class);
    when(principal.getName()).thenReturn(user.getContactEmail());
    when(userGroupService.addUserToUserGroup(user.getContactEmail(),
        userGroup.getGroupName())).thenReturn(userGroup);

    mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/userGroup/addUser/" + userGroup.getGroupName())
                .contentType(MediaType.APPLICATION_JSON)
                .principal(principal))
        .andDo(print())
        .andExpect(status().is(302));
  }

  @Test
  void deleteUserGroupFromUser() throws Exception {
    Principal principal = Mockito.mock(Principal.class);
    when(principal.getName()).thenReturn(user.getContactEmail());
    doNothing().when(userGroupService)
        .deleteUserGroupFromUser(user.getContactEmail(), userGroup.getGroupName());

    mockMvc.perform(MockMvcRequestBuilders.get(
                "/api/v1/userGroup/removeUser/" + userGroup.getGroupName())
            .contentType(MediaType.APPLICATION_JSON)
            .principal(principal))
        .andDo(print())
        .andExpect(status().is(302));
  }

  @Test
  void deleteUserGroup() throws Exception {
    doNothing().when(userGroupService).deleteUserGroup(userGroup.getGroupName());

    mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/userGroup/removeGroup/" + userGroup.getGroupName())
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is(302));
  }
}