package com.example.login.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.login.model.Account;
import com.example.login.model.AccountType;
import com.example.login.model.Status;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountRepositoryTest {

  @Autowired
  AccountRepository accountRepository;

  Account account = new Account(), account1 = new Account();

  @BeforeEach
  void setUp() throws ParseException {
    account.setAccountType(AccountType.PREMIUM);
    account.setAccountStatus(Status.ACTIVE);
    account.setIsInternal("y");
    SimpleDateFormat df = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
    Date date = df.parse(new Date().toString());
    account.setTimeActiveFrom(date);
    account.setName("Name");
    account.setDescription("Description");
    account.setInternalProperties("Internal Properties");

    account1.setAccountType(AccountType.BASIC);
    account1.setName("Name");
    account1.setDescription("Description");
  }

  @Test
  void deleteById() {
    accountRepository.save(account);
    accountRepository.deleteById(account.getId());
    Account fetchAccount = accountRepository.findAccountByName(account.getName());

    assertThat(fetchAccount).isNull();
  }

  @Test
  void existsById() {
    accountRepository.save(account);

    assertThat(account.getId()).isNotNull();
  }

  @Test
  void findAccountById() {
    accountRepository.save(account);
    Account fetchAccount = accountRepository.findAccountByName(account.getName());

    assertThat(fetchAccount.getId()).isEqualTo(account.getId());
  }
}