package com.example.login.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.login.model.Account;
import com.example.login.model.AccountType;
import com.example.login.model.Status;
import com.example.login.service.AccountService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

  @Mock
  AccountService accountService;

  @InjectMocks
  AccountController accountController;
  Account account = new Account(), account1 = new Account();
  List<Account> accountList = new ArrayList<>();
  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    account.setAccountType(AccountType.BASIC);
    account.setName("ABC");
    account.setDescription("Description of ABC");

    account1.setAccountType(AccountType.PREMIUM);
    account1.setName("XYZ");
    account1.setDescription("Description of xyz");

    accountList.add(account);
    accountList.add(account1);
    mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
  }

  @Test
  void createAccount() throws Exception {
    when(accountService.createAccount(account)).thenReturn(account);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJson(account)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(account.getName())));
  }

  @Test
  void findAllAccount() throws Exception {
    when(accountService.findAllAccount()).thenReturn(accountList);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/account/")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  void findByAccountName() throws Exception {
    when(accountService.findByAccountName(account.getName())).thenReturn(account);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/account/" + account.getName())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(account.getName())));
  }

  @Test
  void updateAccount() throws Exception {
    account.setAccountStatus(Status.INACTIVE);

    when(accountService.updateAccount(account)).thenReturn(account);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/account/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJson(account)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accountStatus", is(account.getAccountStatus().toString())));
  }

  @Test
  void deleteAccount() throws Exception {
    doNothing().when(accountService).deleteAccount(account.getId());

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/account/" + account.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk());
  }
}