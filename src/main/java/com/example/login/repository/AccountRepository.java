package com.example.login.repository;

import com.example.login.model.Account;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository of {@link com.example.login.model.Account Account} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, UUID> {


  /**
   * This method is used to delete an account using the id
   *
   * @param id UUID of an Account.
   */
  void deleteById(UUID id);

  /**
   * This method is used to check if an account exists using the id
   *
   * @param id UUID of an Account.
   */
  boolean existsByName(String name);

  /**
   * This method is used to retrieve an account using the name
   *
   * @param id Name of an Account.
   * @return Instance of Account.
   */
  Account findAccountByName(String name);
}
