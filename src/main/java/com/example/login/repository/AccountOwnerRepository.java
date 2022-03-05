package com.example.login.repository;

import com.example.login.model.AccountOwner;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository of {@link com.example.login.model.AccountOwner AccountOwner} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Repository
@Transactional
public interface AccountOwnerRepository extends JpaRepository<AccountOwner, UUID> {

  /**
   * This method is used to find an AccountOwner using the id
   *
   * @param id UUID of an AccountOwner.
   * @return Returns the AccountOwner.
   */
  AccountOwner findAccountOwnerById(UUID id);

  /**
   * This method is used to delete an AccountOwner using the id
   *
   * @param id UUID of an AccountOwner.
   */
  void deleteById(UUID id);

  /**
   * This method is used to check if an AccountOwner exists using the id
   *
   * @param id UUID of an AccountOwner.
   */
  boolean existsById(UUID id);
}
