package com.example.login.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.login.model.Account;
import com.example.login.model.AccountOwner;
import com.example.login.model.AccountType;
import com.example.login.repository.AccountOwnerRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountOwnerServiceTest {

  @Mock
  AccountRepository accountRepository;

  @Mock
  AccountOwnerRepository accountOwnerRepository;

  @Mock
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  AccountOwnerService accountOwnerService;

  AccountOwner accountOwner = new AccountOwner(), accountOwner1 = new AccountOwner();
  Account account = new Account(), account1 = new Account();
  List<AccountOwner> accountOwnerList = new ArrayList<>();

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

    accountOwnerRepository.save(accountOwner);
    accountRepository.save(account);
    accountOwnerRepository.save(accountOwner1);
    accountRepository.save(account1);

    accountOwnerList.add(accountOwner);
    accountOwnerList.add(accountOwner1);
  }

  @Test
  void createAccountOwner() {
    when(bCryptPasswordEncoder.encode(accountOwner.getPassword())).thenReturn(
        accountOwner.getPassword());
    when(accountOwnerRepository.save(ArgumentMatchers.any(AccountOwner.class))).thenReturn(
        accountOwner);
    AccountOwner accountOwnerFetch = accountOwnerService.createAccountOwner(accountOwner);

    assertThat(accountOwnerFetch).isEqualTo(accountOwner);

  }

  @Test
  void findByAccountId() {
    when(accountOwnerRepository.existsById(accountOwner.getId())).thenReturn(true);
    when(accountOwnerRepository.findAccountOwnerById(accountOwner.getId())).thenReturn(
        accountOwner);
    AccountOwner accountOwnerFetch = accountOwnerService.findByAccountId(accountOwner.getId());

    assertThat(accountOwnerFetch).isEqualTo(accountOwner);
  }

  @Test
  void findAllAccountOwner() {
    when(accountOwnerRepository.findAll()).thenReturn(accountOwnerList);
    List<AccountOwner> accountOwnerListFetch = accountOwnerService.findAllAccountOwner();

    assertThat(accountOwnerListFetch).isEqualTo(accountOwnerList);
  }

  @Test
  void updateAccountOwner() {
    when(accountOwnerRepository.existsById(accountOwner.getId())).thenReturn(true);
    when(accountOwnerRepository.findAccountOwnerById(accountOwner.getId())).thenReturn(
        accountOwner);
    when(bCryptPasswordEncoder.encode(accountOwner.getPassword())).thenReturn(
        accountOwner.getPassword());
    when(accountOwnerRepository.save(ArgumentMatchers.any(AccountOwner.class))).thenReturn(
        accountOwner);
    AccountOwner accountOwnerFetch = accountOwnerService.updateAccountOwner(accountOwner);

    assertThat(accountOwnerFetch).isEqualTo(accountOwner);
  }

  @Test
  void deleteAccountOwner() {
    when(accountOwnerRepository.existsById(accountOwner.getId())).thenReturn(true);
    doNothing().when(accountOwnerRepository).deleteById(accountOwner.getId());
    accountOwnerService.deleteAccountOwner(accountOwner.getId());
    AccountOwner accountOwnerFetch = accountOwnerService.findByAccountId(accountOwner.getId());

    assertThat(accountOwnerFetch).isNull();
  }
}