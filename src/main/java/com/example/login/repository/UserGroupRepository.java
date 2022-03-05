package com.example.login.repository;

import com.example.login.model.UserGroup;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository of {@link com.example.login.model.UserGroup UserGroup} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Repository
@Transactional
public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {

  /**
   * This method is used to find an UserGroup using id
   *
   * @param id UUID of an UserGroup.
   * @return Returns the UserGroup.
   */
  UserGroup findUserGroupById(UUID id);

  /**
   * This method is used to find all UserGroup that belong to the same User
   *
   * @param id UUID of a User.
   * @return Returns the {@link List} of UserGroup.
   */
  List<UserGroup> findUserGroupsByUsersId(UUID id);

  /**
   * This method is used to find an UserGroup using groupName.
   *
   * @param groupName Name of an UserGroup.
   * @return Returns the UserGroup.
   */
  UserGroup findByGroupName(String groupName);

  /**
   * This method is used to check if an UserGroup exists using id.
   *
   * @param id UUID of an UserGroup.
   */
  boolean existsById(UUID id);

  /**
   * This method is used to check if an UserGroup exists using group name.
   *
   * @param groupName Name of the UserGroup.
   */
  boolean existsUserGroupByGroupName(String groupName);

  /**
   * This method is used to delete an UserGroup using userGroupId(UUID).
   *
   * @param id UUID of an UserGroup.
   */
  void deleteById(UUID id);

}
