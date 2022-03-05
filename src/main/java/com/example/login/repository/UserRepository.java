package com.example.login.repository;

import com.example.login.model.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository of {@link com.example.login.model.User User} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, UUID> {

  /**
   * This method is used to find all User that belong to the same Account
   *
   * @param id UUID of an Account.
   * @return Returns a User.
   */
  List<User> findByAccountId_id(UUID id);

  /**
   * This method is used to find a User using id
   *
   * @param id ID of a User.
   * @return Returns a User.
   */
  User findUserById(UUID id);

  /**
   * This method is used to find a User using Email
   *
   * @param contactEmail Email of a User.
   * @return Returns a User.
   */
  User findByContactEmail(String contactEmail);

  /**
   * This method is used to find a User using screenName
   *
   * @param contactEmail Screen name of a User.
   * @return Returns a User.
   */
  User findUserByScreenName(String contactEmail);

  /**
   * This method is used to find the screen name of an account using email
   *
   * @param contactEmail email registered with User.
   * @return Returns a User.
   */
  @Query("SELECT a.screenName FROM User a WHERE a.contactEmail = ?1")
  String getScreenNameByContactEmail(String contactEmail);

  /**
   * This method is used to delete a User
   *
   * @param contactEmail email of User.
   */
  void deleteUserByContactEmail(String contactEmail);

  /**
   * This method is used to check if an User exists using id.
   *
   * @param id UUID of an User.
   */
  boolean existsById(UUID id);

  /**
   * This method is used to check if an User exists using contactEmail.
   *
   * @param contactEmail Email of a User.
   */
  boolean existsUserByContactEmail(String contactEmail);

  /**
   * This method is used to check if an User exists using screenName.
   *
   * @param screenName Screen name of a User.
   */
  boolean existsUserByScreenName(String screenName);

  /**
   * Displays all the users who are part of a UserGroup
   *
   * @param groupId Id of {@link com.example.login.model.UserGroup UserGroup}
   * @return Returns the {@link List} of User
   */
  List<User> findUsersByUserGroupsId(UUID groupId);
}
