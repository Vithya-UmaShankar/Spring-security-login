package com.example.login.controller;

import com.example.login.model.Event;
import com.example.login.model.User;
import com.example.login.model.UserGroup;
import com.example.login.service.UserGroupService;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Controller of {@link com.example.login.model.UserGroup UserGroup} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("api/v1/groups")
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
   * Used to create a new UserGroup
   *
   * @return  Returns the created UserGroup
   */
  @PostMapping("/")
  public UserGroup createUserGroup(@RequestBody @Valid UserGroup userGroup) {
    return userGroupService.createUserGroup(userGroup);
  }

  /**
   * Used to get a userGroup using the id
   *
   * @param groupId ID of {@link com.example.login.model.UserGroup UserGroup}
   * @return Returns a UserGroup
   */
  @GetMapping("/{groupId}")
  public UserGroup getUserGroupsByGroupId(@PathVariable @NotNull UUID groupId) {
    return userGroupService.getUserGroupsByGroupId(groupId);
  }

  /**
   * Updates a UserGroup
   *
   * @param userGroup Instance of the UserGroup that has to be updated.
   * @return Instance of the UserGroup after it has been updated.
   */
  @PutMapping("/{groupId}")
  public UserGroup updateUserGroup(@PathVariable @NotNull UUID groupId, @Valid @RequestBody UserGroup userGroup) {
    return userGroupService.updateUserGroup(groupId, userGroup);
  }

  /**
   * Deletes a UserGroup
   *
   * @param groupId UUID of {@link com.example.login.model.UserGroup UserGroup}
   */
  @DeleteMapping("/{groupId}")
  public void deleteUserGroup(@PathVariable @NotNull UUID groupId){
    userGroupService.deleteUserGroup(groupId);
  }

  /**
   * Displays all the users who are part of a UserGroup
   *
   * @param groupId ID of {@link com.example.login.model.UserGroup UserGroup}
   * @return Returns the {@link List} of User
   */
  @GetMapping("/{groupId}/groupMembers")
  public List<User> getAllUsersByGroupId(@PathVariable @NotBlank UUID groupId) {
    return userGroupService.getAllUsersByGroupId(groupId);
  }

  /**
   * Create a new User and add to UserGroup
   *
   * @param groupId ID of {@link com.example.login.model.UserGroup UserGroup}
   * @return Returns the {@link List} of User
   */
  @PostMapping("/{groupId}/groupMembers")
  public User addNewUserToUserGroup(@PathVariable @NotBlank UUID groupId, @RequestBody @Valid User user) {
    return userGroupService.addNewUserToUserGroup(groupId, user);
  }

  /**
   * Displays a user who is part of a UserGroup
   *
   * @param groupId ID of {@link com.example.login.model.UserGroup UserGroup}
   * @param userId ID of {@link com.example.login.model.User User}
   * @return Returns the {@link List} of User
   */

  @GetMapping("/{groupId}/groupMembers/{userId}")
  public User getUserFromUserGroup(@PathVariable @NotNull UUID groupId, @PathVariable @NotNull UUID userId){
    return userGroupService.getUserFromUserGroup(groupId, userId);
  }

  /**
   * Updates a User who is part of a UserGroup
   *
   * @param groupId ID of {@link com.example.login.model.UserGroup UserGroup}
   * @param userId ID of {@link com.example.login.model.User User}
   * @return Returns the {@link List} of User
   */
  @PutMapping("/{groupId}/groupMembers/{userId}")
  public User updateUserFromUserGroup(@PathVariable @NotNull UUID groupId, @PathVariable @NotNull UUID userId, @RequestBody @Valid User user){
    return userGroupService.updateUserFromUserGroup(groupId, userId, user);
  }

  /**
   * Deletes a User who is part of a UserGroup
   *
   * @param groupId ID of {@link com.example.login.model.UserGroup UserGroup}
   * @param userId ID of {@link com.example.login.model.User User}
   */
  @DeleteMapping("/{groupId}/groupMembers/{userId}")
  public void deleteUserGroupFromUser(@PathVariable @NotNull UUID groupId, @PathVariable @NotNull UUID userId) {
    userGroupService.deleteUserGroupFromUser(groupId, userId);
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
   * Display all the events a group is hosting
   *
   * @param groupId UUID of {@link com.example.login.model.UserGroup UserGroup}
   * @return Returns the {@link List} of Event
   */
  @GetMapping("/{groupId}/hostedEvents")
  public List<Event> getAllHostedEventsInGroup(@PathVariable @NotNull UUID groupId){
    return userGroupService.getAllHostedEventsInGroup(groupId);
  }

  /**
   * Create a new Event in a Group
   *
   * @param groupId UUID of {@link com.example.login.model.Event Event}
   * @return Returns the Event
   */
  @PostMapping("/{groupId}/hostedEvents")
  public Event createNewHostedEventInGroup(@PathVariable @NotNull UUID groupId, @RequestBody @Valid Event event){
    return userGroupService.createNewHostedEventInGroup(groupId, event);
  }

  /**
   * Display an event that a group is hosting
   *
   * @param groupId UUID of {@link com.example.login.model.UserGroup UserGroup}
   * @param hostedEventId UUID of {@link com.example.login.model.Event Event}
   * @return Returns the Event
   */
  @GetMapping("/{groupId}/hostedEvents/{hostedEventId}")
  public Event getHostedEventInGroup(@PathVariable @NotNull UUID groupId, @PathVariable @NotNull UUID hostedEventId){
    return userGroupService.getHostedEventInGroup(groupId, hostedEventId);
  }

  /**
   * Update an event that a group is hosting
   *
   * @param groupId UUID of {@link com.example.login.model.UserGroup UserGroup}
   * @param hostedEventId UUID of {@link com.example.login.model.Event Event}
   * @return Returns the updated Event
   */
  @PutMapping("/{groupId}/hostedEvents/{hostedEventId}")
  public Event updateHostedEventInGroup(@PathVariable @NotNull UUID groupId, @PathVariable @NotNull UUID hostedEventId, @RequestBody @Valid Event event){
    return userGroupService.updateHostedEventInGroup(groupId, hostedEventId, event);
  }

  /**
   * Delete an event that a group is hosting
   *
   * @param groupId UUID of {@link com.example.login.model.UserGroup UserGroup}
   * @param hostedEventId UUID of {@link com.example.login.model.Event Event}
   * @return Returns the updated Event
   */
  @Transactional
  @DeleteMapping("/{groupId}/hostedEvents/{hostedEventId}")
  public void deleteHostedEventInGroup(@PathVariable @NotNull UUID groupId, @PathVariable @NotNull UUID hostedEventId){
    userGroupService.deleteHostedEventInGroup(groupId, hostedEventId);
  }
}