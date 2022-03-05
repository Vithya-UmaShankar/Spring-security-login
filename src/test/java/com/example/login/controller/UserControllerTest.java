package com.example.login.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.login.model.Account;
import com.example.login.model.AccountOwner;
import com.example.login.model.AccountType;
import com.example.login.model.ConfirmationToken;
import com.example.login.model.User;
import com.example.login.model.UserGroup;
import com.example.login.model.UserRole;
import com.example.login.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock
  UserService userService;

  @InjectMocks
  UserController userController;
  AccountOwner accountOwner = new AccountOwner();
  User user = new User(), user1 = new User();
  Account account = new Account();
  UserGroup userGroup = new UserGroup(), userGroup1 = new UserGroup(), userGroup2 = new UserGroup();
  List<UserGroup> userGroupList = new ArrayList<>();
  List<User> userList = new ArrayList<>();
  ConfirmationToken confirmationToken = new ConfirmationToken();
  @Autowired
  private MockMvc mockMvc;

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
    user1.setName("User");
    user1.setDescription("User who registered using Google");
    user1.setAccountId(account);

    userList.add(user);
    userList.add(user1);

    confirmationToken.setToken(UUID.randomUUID().toString());
    confirmationToken.setCreatedAt(LocalDateTime.now());
    confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
    confirmationToken.setUserId(user);

    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  @WithMockUser(username = "abc@gmail.com", roles = {"USER"})
  void createUser() throws Exception {
    /*
    when(userService.createUser(user, account.getName())).thenReturn(user);

    mockMvc.perform(
		MockMvcRequestBuilders
		.post(
		"/api/v1/user/" + account.getName())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJson(user1)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.screenName", is(user.getScreenName())));

     */
  }

  @Test
  void findAllUser() throws Exception {
    when(userService.findAllUser()).thenReturn(userList);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  void getUserByUserId() throws Exception {
    when(userService.getUserByUserId(user.getId())).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.screenName", is(user.getScreenName())));
  }

  @Test
  void deleteUser() throws Exception {
    doNothing().when(userService).deleteUser(user.getContactEmail());

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/user/" + user.getContactEmail())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk());
  }

  //org.springframework.security.core.GrantedAuthority` (no Creators, like default constructor, exist):
  // abstract types either need to be mapped to concrete types, have custom deserializer, or contain additional type information
  @Test
  @WithMockUser(username = "abc@gmail.com", roles = {"USER"})
  void updateUser() throws Exception {
    /*
    user.setScreenName("Abc_123");

    when(userService.updateUser(user)).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/user/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJson(user)))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.screenName", is(user.getScreenName())));
     */
  }
}