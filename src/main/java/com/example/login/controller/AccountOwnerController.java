package com.example.login.controller;

import com.example.login.model.AccountOwner;
import com.example.login.service.AccountOwnerService;
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
 * Controller of {@link com.example.login.model.AccountOwner AccountOwner} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@RestController
@RequestMapping("api/v1/accountOwner")
public class AccountOwnerController {

  @Autowired
  AccountOwnerService accountOwnerService;

  /**
   * This method is used to create a new AccountOwner
   *
   * @param accountOwner Instance of AccountOwner that has to be inserted.
   * @return Returns the instance of AccountOwner after it has been inserted.
   */
  @PostMapping("/")
  public AccountOwner createAccountOwner(@RequestBody @Valid AccountOwner accountOwner) {
    return accountOwnerService.createAccountOwner(accountOwner);
  }

  /**
   * This method is used to return all the accountOwners in account_owner table
   *
   * @return Returns the {@link List} of AccountOwner.
   */
  @GetMapping("/")
  public List<AccountOwner> findAllAccountOwner() {
    return accountOwnerService.findAllAccountOwner();
  }

  /**
   * This method is used to find an account owner using the id
   *
   * @param accountOwnerId UUID of an {@link com.example.login.model.AccountOwner AccountOwner}.
   * @return Returns the AccountOwner.
   */
  @GetMapping("/{accountOwnerId}")
  public AccountOwner findByAccountId(@PathVariable @NotBlank UUID accountOwnerId) {
    return accountOwnerService.findByAccountId(accountOwnerId);
  }

  /**
   * This method is used to update an existing AccountOwner
   *
   * @param accountOwner Instance of account that has to be updated.
   * @return Returns account after it has been updated.
   */
  @PutMapping("/")
  public AccountOwner updateAccountOwner(@RequestBody @Valid AccountOwner accountOwner) {
    return accountOwnerService.updateAccountOwner(accountOwner);
  }

  /**
   * This method is used to delete an account owner using the id
   *
   * @param accountOwnerId UUID of an {@link com.example.login.model.AccountOwner AccountOwner}.
   */
  @DeleteMapping("/{accountOwnerId}")
  public void deleteAccountOwner(@PathVariable @NotBlank UUID accountOwnerId) {
    accountOwnerService.deleteAccountOwner(accountOwnerId);
  }
}
