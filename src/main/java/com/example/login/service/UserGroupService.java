package com.example.login.service;

import com.example.login.exception.ResourceNotFoundException;
import com.example.login.model.User;
import com.example.login.model.UserGroup;
import com.example.login.repository.UserGroupRepository;
import com.example.login.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service of {@link com.example.login.model.UserGroup UserGroup} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserGroupService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserGroupRepository userGroupRepository;

  /**
   * Used to create a new UserGroup and to add the current user to the group
   *
   * @param contactEmail Email of {@link com.example.login.model.User User}
   * @param groupName    Name of the {@link com.example.login.model.UserGroup UserGroup}.
   * @return Instance of the UserGroup after it has been inserted.
   */
  public UserGroup addUserToUserGroup(String contactEmail, String groupName) {

    if (userRepository.existsUserByContactEmail(contactEmail)) {
      User user = userRepository.findByContactEmail(contactEmail);

      // UserGroup exists
      if (userGroupRepository.existsUserGroupByGroupName(groupName)) {
        UserGroup ug = userGroupRepository.findByGroupName(groupName);
        user.addUserGroups(ug);
        User _user = userRepository.save(user);
        return ug;
      }

      UserGroup userGroup = new UserGroup();
      userGroup.setGroupName(groupName);

      // add and create new UserGroup
      user.addUserGroups(userGroup);
      return userGroupRepository.save(userGroup);
    } else {
      throw new ResourceNotFoundException("User with email [" + contactEmail + "] not found");
    }
  }

  /**
   * Used to display all the UserGroups
   */
  public List<UserGroup> getAllUserGroups() {
    List<UserGroup> userGroups = new ArrayList<UserGroup>();

    userGroupRepository.findAll().forEach(userGroups::add);

    return userGroups;
  }

  /**
   * Used to get a userGroup using the id
   *
   * @param groupId Id of {@link com.example.login.model.UserGroup UserGroup}
   * @return Returns a UserGroup
   */
  public UserGroup getUserGroupsByGroupId(UUID groupId) {
    return userGroupRepository.findUserGroupById(groupId);
  }

  /**
   * Displays all the users who are part of a UserGroup
   *
   * @param groupId Id of {@link com.example.login.model.UserGroup UserGroup}
   * @return Returns the {@link List} of User
   */
  public List<User> getAllUsersByGroupId(UUID groupId) {
    if (!userGroupRepository.existsById(groupId)) {
      throw new ResourceNotFoundException("User Group  with id [" + groupId + "] not found");
    }

    return userRepository.findUsersByUserGroupsId(groupId);
  }

  /**
   * Display all the UserGroups a User belongs to
   *
   * @param userId id of {@link com.example.login.model.User User}
   * @return Returns the {@link List} of UserGroup
   */
  public List<UserGroup> getAllUserGroupsByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException("User with id [" + userId + "] not found");
    }

    return userGroupRepository.findUserGroupsByUsersId(userId);
  }

  /**
   * Updates a UserGroup
   *
   * @param userGroup Instance of the UserGroup that has to be updated.
   * @return Instance of the UserGroup after it has been updated.
   */
  public UserGroup updateUserGroup(UserGroup userGroup) {
    if (userGroupRepository.existsById(userGroup.getId())) {
      UserGroup ug = userGroupRepository.findUserGroupById(userGroup.getId());

      ug.setGroupName(userGroup.getGroupName());

      return userGroupRepository.save(ug);
    } else {
      throw new ResourceNotFoundException("GroupId " + userGroup.getId() + "not found");
    }
  }

  /**
   * Removes a UserGroup from the list of UserGroups the user belongs to( Also removes the user from
   * the list of Users who belong to a same UserGroup)
   *
   * @param contactEmail Email of {@link com.example.login.model.User User}
   * @param groupName    Name of {@link com.example.login.model.UserGroup UserGroup}
   */
  public void deleteUserGroupFromUser(String contactEmail, String groupName) {

    if (!userGroupRepository.existsUserGroupByGroupName(groupName)) {
      throw new ResourceNotFoundException("Group Name [" + groupName + "] not found");
    }
    if (!userRepository.existsUserByContactEmail(contactEmail)) {
      throw new ResourceNotFoundException("User email[" + contactEmail + "] not found");
    } else {
      User user = userRepository.findByContactEmail(contactEmail);
      UserGroup userGroup = userGroupRepository.findByGroupName(groupName);

      Set<UserGroup> userGroupList = user.getUserGroups();
      Set<User> userList = userGroup.getUsers();

      userGroupList.remove(userGroup);
      userList.remove(user);

      userRepository.save(user);
      userGroupRepository.save(userGroup);
    }
  }

  /**
   * Deletes a UserGroup
   *
   * @param groupName Name of {@link com.example.login.model.UserGroup UserGroup}
   */
  public void deleteUserGroup(String groupName) {
    if (!userGroupRepository.existsUserGroupByGroupName(groupName)) {
      throw new ResourceNotFoundException("Group Name [" + groupName + "] not found");
    } else {
      UserGroup userGroup = userGroupRepository.findByGroupName(groupName);
      Set<User> userList = userGroup.getUsers();
      for (User user : userList) {
        deleteUserGroupFromUser(user.getContactEmail(), groupName);
      }

      userGroupRepository.deleteById(userGroup.getId());
    }
  }
}