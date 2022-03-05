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
import com.example.login.service.AccountOwnerService;
import com.example.login.util.JsonUtil;
import java.util.ArrayList;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
class AccountOwnerControllerTest {

  @Mock
  AccountOwnerService accountOwnerService;

  @InjectMocks
  AccountOwnerController accountOwnerController;
  AccountOwner accountOwner = new AccountOwner(), accountOwner1 = new AccountOwner();
  Account account = new Account(), account1 = new Account();
  List<AccountOwner> accountOwnerList = new ArrayList<>();
  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    account.setAccountType(AccountType.BASIC);
    account.setName("Name");
    account.setDescription("Description");

    accountOwner.setName("Company");
    accountOwner.setPassword("password");
    accountOwner.setAccount(account);

    account1.setAccountType(AccountType.PREMIUM);
    account1.setName("User");
    account1.setDescription("Description");

    accountOwner1.setName("Owner");
    accountOwner1.setPassword("password");
    accountOwner1.setAccount(account);

    accountOwnerList.add(accountOwner);
    accountOwnerList.add(accountOwner1);
    mockMvc = MockMvcBuilders.standaloneSetup(accountOwnerController).build();
  }

  @Test
  void createAccountOwner() throws Exception {
    when(accountOwnerService.createAccountOwner(accountOwner)).thenReturn(accountOwner);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accountOwner/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJson(accountOwner)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(accountOwner.getName())));
  }

  @Test
  void findAllAccountOwner() throws Exception {
    when(accountOwnerService.findAllAccountOwner()).thenReturn(accountOwnerList);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accountOwner/")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  void findByAccountId() throws Exception {
    when(accountOwnerService.findByAccountId(accountOwner.getId())).thenReturn(accountOwner);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accountOwner/" + accountOwner.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(accountOwner.getName())));
  }

  @Test
  void updateAccountOwner() throws Exception {
    accountOwner.setName("Abc_123");

    when(accountOwnerService.updateAccountOwner(accountOwner)).thenReturn(accountOwner);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accountOwner/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJson(accountOwner)))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(accountOwner.getName())));
  }

  @Test
  void deleteAccountOwner() throws Exception {
    doNothing().when(accountOwnerService).deleteAccountOwner(accountOwner.getId());

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accountOwner/" + accountOwner.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk());
  }
}