package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.model.UserGroup;
import com.example.login.service.UserGroupService;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller of {@link com.example.login.model.UserGroup UserGroup} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("api/v1/userGroup")
public class UserGroupController {

  @Autowired
  UserGroupService userGroupService;

  /**
   * Used to display all the UserGroups
   */
  @GetMapping("/")
  public List<UserGroup> getAllUserGroups() {
    return userGroupService.getAllUserGroups();
  }

  /**
   * Used to get a userGroup using the id
   *
   * @param groupId ID of {@link com.example.login.model.UserGroup UserGroup}
   * @return Returns a UserGroup
   */
  @GetMapping("/{groupId}")
  public UserGroup getUserGroupsByGroupId(@PathVariable @NotBlank UUID groupId) {
    return userGroupService.getUserGroupsByGroupId(groupId);
  }

  /**
   * Displays all the users who are part of a UserGroup
   *
   * @param groupId ID of {@link com.example.login.model.UserGroup UserGroup}
   * @return Returns the {@link List} of User
   */
  @GetMapping("/users/{groupId}")
  public List<User> getAllUsersByGroupId(@PathVariable @NotBlank UUID groupId) {
    return userGroupService.getAllUsersByGroupId(groupId);
  }

  /**
   * Display all the UserGroups a User belongs to
   *
   * @param userId id of {@link com.example.login.model.User User}
   * @return Returns the {@link List} of UserGroup
   */
  @GetMapping("/userGroups/{userId}")
  public List<UserGroup> getAllUserGroupsByUserId(@PathVariable @NotBlank UUID userId) {
    return userGroupService.getAllUserGroupsByUserId(userId);
  }

  /**
   * Updates a UserGroup
   *
   * @param userGroup Instance of the UserGroup that has to be updated.
   * @return Instance of the UserGroup after it has been updated.
   */
  @PutMapping("/")
  public UserGroup updateUserGroup(@Valid @RequestBody UserGroup userGroup) {
    return userGroupService.updateUserGroup(userGroup);
  }
}