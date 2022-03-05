package com.example.login.controller;

import com.example.login.model.Account;
import com.example.login.service.AccountService;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller of {@link com.example.login.model.Account Account} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@RestController
@RequestMapping("api/v1/account")
public class AccountController {

  @Autowired
  AccountService accountService;

  /**
   * This method is used to create a new Account
   *
   * @param account Instance of account that has to be inserted.
   * @return Returns the instance of account after it has been inserted.
   */

  //AccountOwner creates an account. So not needed here
  @PostMapping("/")
  public Account createAccount(@RequestBody @Valid Account account) {
    return accountService.createAccount(account);
  }

  /**
   * This method is used to return all the accounts in account table
   *
   * @return Returns the {@link List} of accounts.
   */
  @GetMapping("/")
  public List<Account> findAllAccount() {
    return accountService.findAllAccount();
  }

  /**
   * This method is used to find an account using the ID
   *
   * @param accountName Name of an {@link com.example.login.model.Account Account}.
   * @return Returns the Account.
   */
  @GetMapping("/{accountName}")
  public Account findByAccountName(@PathVariable @NotBlank String accountName) {
    return accountService.findByAccountName(accountName);
  }

  /**
   * This method is used to update an existing Account
   *
   * @param account Instance of account that has to be updated.
   * @return Returns the instance of account after it has been updated.
   */
  @PutMapping("/")
  public Account updateAccount(@RequestBody @Valid Account account) {
    return accountService.updateAccount(account);
  }

  /**
   * This method is used to delete an account using the ID
   *
   * @param accountId UUID of an {@link com.example.login.model.Account Account}.
   */
  @DeleteMapping("/{accountId}")
  public void deleteAccount(@PathVariable @NotBlank UUID accountId) {
    accountService.deleteAccount(accountId);
  }

}
