package com.example.login.service;

import com.example.login.exception.ResourceNotFoundException;
import com.example.login.model.Account;
import com.example.login.repository.AccountRepository;
import com.example.login.repository.UserRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service of {@link com.example.login.model.Account Account} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Slf4j
public class AccountService {

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserService userService;

  /**
   * This method is used to create a new Account
   *
   * @param account Instance of account that has to be inserted.
   * @return Returns account after it has been inserted.
   */
  public Account createAccount(Account account) {
    return accountRepository.save(account);
  }

  /**
   * This method is used to return all the accounts in account table
   *
   * @return Returns the {@link List} of accounts.
   */
  public List<Account> findAllAccount() {
    return accountRepository.findAll();
  }

  /**
   * This method is used to find an account using the id
   *
   * @param accountId UUID of an {@link com.example.login.model.Account Account}.
   * @return Returns the Account.
   */
  public Account findByAccountName(String accountName) {
    if (accountRepository.existsByName(accountName)) {
      return accountRepository.findAccountByName(accountName);
    } else {
      throw new ResourceNotFoundException("Account [" + accountName + "] not found!");
    }
  }

  /**
   * This method is used to delete an account using the id
   *
   * @param accountId UUID of an {@link com.example.login.model.Account Account}.
   */
  public void deleteAccount(UUID accountId) {
    if (accountRepository.existsById(accountId)) {
      accountRepository.deleteById(accountId);
    } else {
      throw new ResourceNotFoundException("Account with Account Id [" + accountId + "] not found");
    }
  }

  /**
   * This method is used to update an existing Account
   *
   * @param account Instance of account that has to be updated.
   * @return Returns account after it has been updated.
   */
  public Account updateAccount(Account account) {

    if (accountRepository.existsByName(account.getName())) {
      Account a = accountRepository.findAccountByName(account.getName());
      a.setTimeUpdated(new Date());

      if (account.getAccountType() != null) {
        a.setAccountType(account.getAccountType());
      }
      if (account.getAccountStatus() != null) {
        a.setAccountStatus(account.getAccountStatus());
      }
      if (account.getIsInternal() != null) {
        a.setIsInternal(account.getIsInternal());
      }
      if (account.getTimeActiveFrom() != null) {
        a.setTimeActiveFrom(account.getTimeActiveFrom());
      }
      if (account.getTimeActiveUntil() != null) {
        a.setTimeActiveUntil(account.getTimeActiveUntil());
      }
      if (account.getTimeAccessRestrictedFrom() != null) {
        a.setTimeAccessRestrictedFrom(account.getTimeAccessRestrictedFrom());
      }
      if (account.getTimeAccessRestrictedUntil() != null) {
        a.setTimeAccessRestrictedUntil(account.getTimeAccessRestrictedUntil());
      }
      if (account.getName() != null) {
        a.setName(account.getName());
      }
      if (account.getDescription() != null) {
        a.setDescription(account.getDescription());
      }
      if (account.getInternalProperties() != null) {
        a.setInternalProperties(account.getInternalProperties());
      }

      return accountRepository.save(a);
    } else {
      throw new ResourceNotFoundException("Account [" + account.getName() + "] not found!");
    }
  }

}

