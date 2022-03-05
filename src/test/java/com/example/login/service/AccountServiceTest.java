package com.example.login.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.login.model.Account;
import com.example.login.model.AccountType;
import com.example.login.repository.AccountRepository;
import java.util.ArrayList;
import java.util.List;
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
class AccountServiceTest {

  @Mock
  AccountRepository accountRepository;

  @InjectMocks
  AccountService accountService;

  Account account = new Account(), account1 = new Account();
  List<Account> accountList = new ArrayList<>();

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
  }

  @Test
  void createAccount() {
    when(accountRepository.save(ArgumentMatchers.any(Account.class))).thenReturn(account);
    Account accountFetch = accountService.createAccount(account);

    assertThat(accountFetch).isEqualTo(account);
  }

  @Test
  void findAllAccount() {
    when(accountRepository.findAll()).thenReturn(accountList);
    List<Account> accountFetch = accountService.findAllAccount();

    assertThat(accountFetch).isEqualTo(accountList);
  }

  @Test
  void findByAccountName() {
    when(accountRepository.existsByName(account.getName())).thenReturn(true);
    when(accountRepository.findAccountByName(account.getName())).thenReturn(account);
    Account accountFetch = accountService.findByAccountName(account.getName());

    assertThat(accountFetch.getId()).isEqualTo(account.getId());
  }

  @Test
  void deleteAccount() {
    when(accountRepository.existsById(account.getId())).thenReturn(true);
    doNothing().when(accountRepository).deleteById(account.getId());
    accountService.deleteAccount(account.getId());
    Account accountFetch = null;
    if (accountRepository.existsByName(account.getName())) {
      accountFetch = accountService.findByAccountName(account.getName());
    }
    assertThat(accountFetch).isNull();
  }

  @Test
  void updateAccount() {
    when(accountRepository.existsByName(account.getName())).thenReturn(true);
    when(accountRepository.findAccountByName(account.getName())).thenReturn(account);
    when(accountRepository.save(ArgumentMatchers.any(Account.class))).thenReturn(account);
    Account accountFetch = accountService.updateAccount(account);

    assertThat(accountFetch).isEqualTo(account);
  }
}