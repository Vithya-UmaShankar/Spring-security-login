package com.example.login.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.login.model.Account;
import com.example.login.model.AccountOwner;
import com.example.login.model.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountOwnerRepositoryTest {

  @Autowired
  AccountOwnerRepository accountOwnerRepository;

  @Autowired
  AccountRepository accountRepository;

  AccountOwner accountOwner = new AccountOwner();
  Account account = new Account();

  @BeforeEach
  void setUp() {
    account.setAccountType(AccountType.BASIC);
    account.setName("Name");
    account.setDescription("Description");

    accountOwner.setAccount(account);
    accountOwner.setName("ABC");
    accountOwner.setPassword("123456789");
    accountRepository.save(account);
    accountOwnerRepository.save(accountOwner);
  }

  @Test
  void findAccountOwnerById() {

    AccountOwner fetchAccountOwner = null;
    if (accountOwnerRepository.existsById(accountOwner.getId())) {
      fetchAccountOwner = accountOwnerRepository.findAccountOwnerById(accountOwner.getId());
    }
    assertThat(fetchAccountOwner.getId()).isEqualTo(accountOwner.getId());

  }

  @Test
  void deleteById() {

    if (accountOwnerRepository.existsById(accountOwner.getId())) {
      accountOwnerRepository.deleteById(accountOwner.getId());
    }
    AccountOwner fetchAccountOwner = null;
    if (accountOwnerRepository.existsById(accountOwner.getId())) {
      fetchAccountOwner = accountOwnerRepository.findAccountOwnerById(accountOwner.getId());
    }

    assertThat(fetchAccountOwner).isNull();
  }

  @Test
  void existsById() {
    boolean accountExists = accountOwnerRepository.existsById(accountOwner.getId());

    assertThat(accountExists).isEqualTo(true);
  }
}