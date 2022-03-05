package com.example.login.service;

import com.example.login.exception.ResourceNotFoundException;
import com.example.login.model.Account;
import com.example.login.model.AccountOwner;
import com.example.login.model.AccountType;
import com.example.login.model.Status;
import com.example.login.repository.AccountOwnerRepository;
import com.example.login.repository.AccountRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service of {@link com.example.login.model.AccountOwner AccountOwner} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class AccountOwnerService {

  @Autowired
  AccountOwnerRepository accountOwnerRepository;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  AccountService accountService;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * This method is used to create a new AccountOwner
   *
   * @param accountOwner Instance of AccountOwner that has to be inserted.
   * @return Returns AccountOwner after it has been inserted.
   */
  public AccountOwner createAccountOwner(AccountOwner accountOwner) {
    Account account = new Account();
    account.setAccountType(AccountType.BASIC);
    account.setAccountStatus(Status.ACTIVE);
    account.setName(accountOwner.getName());
    account.setDescription("Account for " + accountOwner.getName());
    accountRepository.save(account);

    accountOwner.setAccount(account);
    accountOwner.setPassword(bCryptPasswordEncoder.encode(accountOwner.getPassword()));
    return accountOwnerRepository.save(accountOwner);
  }

  /**
   * This method is used to find an account owner using the id
   *
   * @param accountOwnerId UUID of an {@link com.example.login.model.AccountOwner AccountOwner}.
   * @return Returns the AccountOwner.
   */
  public AccountOwner findByAccountId(UUID accountOwnerId) {
    if (accountOwnerRepository.existsById(accountOwnerId)) {
      return accountOwnerRepository.findAccountOwnerById(accountOwnerId);
    } else {
      throw new ResourceNotFoundException("AccountOwner [" + accountOwnerId + "] not found");
    }
  }

  /**
   * This method is used to return all the accountOwners in account_owner table
   *
   * @return Returns the {@link List} of AccountOwner.
   */
  public List<AccountOwner> findAllAccountOwner() {
    return accountOwnerRepository.findAll();
  }

  /**
   * This method is used to update an existing AccountOwner
   *
   * @param accountOwner Instance of account that has to be updated.
   * @return Returns the instance of AccountOwner after it has been updated.
   */
  public AccountOwner updateAccountOwner(AccountOwner accountOwner) {

    if (accountOwnerRepository.existsById(accountOwner.getId())) {
      AccountOwner a = accountOwnerRepository.findAccountOwnerById(accountOwner.getId());

      if (accountOwner.getName() != null) {
        a.setName(accountOwner.getName());
      }
      if (accountOwner.getPassword() != null) {
        a.setPassword(bCryptPasswordEncoder.encode(accountOwner.getPassword()));
      }

      return accountOwnerRepository.save(a);
    } else {
      throw new ResourceNotFoundException(
          "AccountOwner Id [" + accountOwner.getId() + "] doesn't exist");
    }
  }

  /**
   * This method is used to delete an account owner using the id
   *
   * @param accountOwnerId UUID of an {@link com.example.login.model.AccountOwner AccountOwner}.
   */
  public void deleteAccountOwner(UUID accountOwnerId) {
    accountOwnerRepository.deleteById(accountOwnerId);
  }
}
