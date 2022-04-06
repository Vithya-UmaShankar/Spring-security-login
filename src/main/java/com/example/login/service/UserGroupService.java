package com.example.login.service;

import com.example.login.exception.ResourceNotFoundException;
import com.example.login.model.Event;
import com.example.login.model.User;
import com.example.login.model.UserGroup;
import com.example.login.repository.EventRepository;
import com.example.login.repository.UserGroupRepository;
import com.example.login.repository.UserRepository;

import java.util.*;

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

  @Autowired
  EventRepository eventRepository;

  /**
   * Used to display all the UserGroups
   */
  public List<UserGroup> getAllUserGroups() {
    List<UserGroup> userGroups = new ArrayList<UserGroup>();
    userGroupRepository.findAll().forEach(userGroups::add);

    return userGroups;
  }

  /**
   * Used to create a new UserGroup
   *
   * @return  Returns the created UserGroup
   */
  public UserGroup createUserGroup(UserGroup userGroup) {
    return userGroupRepository.save(userGroup);
  }

  /**
   * Used to get a userGroup using the id
   *
   * @param groupId Id of {@link com.example.login.model.UserGroup UserGroup}
   * @return Returns a UserGroup
   */
  public UserGroup getUserGroupsByGroupId(UUID groupId) {
    if (!userGroupRepository.existsById(groupId)) {
      throw new ResourceNotFoundException("User Group  with id [" + groupId + "] not found");
    }
    return userGroupRepository.findUserGroupById(groupId);
  }

  /**
   * Updates a UserGroup
   *
   * @param userGroup Instance of the UserGroup that has to be updated.
   * @return Instance of the UserGroup after it has been updated.
   */
  public UserGroup updateUserGroup(UUID groupId, UserGroup userGroup) {
    if (userGroupRepository.existsById(groupId)) {
      UserGroup ug = userGroupRepository.findUserGroupById(groupId);

      ug.setGroupName(userGroup.getGroupName());

      return userGroupRepository.save(ug);
    } else {
      throw new ResourceNotFoundException("GroupId " + userGroup.getId() + "not found");
    }
  }

  /**
   * Deletes a UserGroup
   *
   * @param groupId UUID of {@link com.example.login.model.UserGroup UserGroup}
   */
  public void deleteUserGroup(UUID groupId) {
    if (userGroupRepository.existsById(groupId)) {
      UserGroup userGroup = userGroupRepository.findUserGroupById(groupId);
      Set<User> userList = userGroup.getUsers();
      for (User user : userList) {
        deleteUserGroupFromUser(user.getContactEmail(), userGroup.getGroupName());
      }
      List<Event> eventList = eventRepository.findByGroupId(userGroup);
      for(Event event: eventList){
        deleteChildEvents(event);
      }

      userGroupRepository.deleteById(userGroup.getId());
    } else {
      throw new ResourceNotFoundException("Group with id [" + groupId + "] not found");
    }
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
   * Create a new User and add to UserGroup
   *
   * @param groupId ID of {@link com.example.login.model.UserGroup UserGroup}
   * @return Returns the {@link List} of User
   */
  public User addNewUserToUserGroup(UUID groupId, User user) {
    if (userGroupRepository.existsById(groupId)) {
      UserGroup ug = userGroupRepository.findUserGroupById(groupId);
      user.addUserGroups(ug);
      return userRepository.save(user);
    } else {
      throw new ResourceNotFoundException("Group with id [" + groupId + "] not found");
    }
  }

  /**
   * Displays a user who is part of a UserGroup
   *
   * @param groupId ID of {@link com.example.login.model.UserGroup UserGroup}
   * @param userId ID of {@link com.example.login.model.User User}
   * @return Returns the {@link List} of User
   */
  public User getUserFromUserGroup(UUID groupId, UUID userId) {
    if (!userGroupRepository.existsById(groupId)) {
      throw new ResourceNotFoundException("User Group with id [" + groupId + "] not found");
    }

    List<User> userList = userRepository.findUsersByUserGroupsId(groupId);
    for(User user : userList) {
      if(user.getId().equals(userId)) return user;
    }
    throw new ResourceNotFoundException("User Group with id [" + groupId + "]  does not have a Group Member with id [" + userId + "]");
  }

  /**
   * Updates a User who is part of a UserGroup
   *
   * @param groupId ID of {@link com.example.login.model.UserGroup UserGroup}
   * @param userId ID of {@link com.example.login.model.User User}
   * @return Returns the {@link List} of User
   */
  public User updateUserFromUserGroup(UUID groupId, UUID userId, User user) {
    if (!userGroupRepository.existsById(groupId)) {
      throw new ResourceNotFoundException("User Group with id [" + groupId + "] not found");
    }

    List<User> userList = userRepository.findUsersByUserGroupsId(groupId);
    for(User u : userList) {
      if(u.getId().equals(userId)) {
        user.setId(userId);
        deleteUserGroupFromUser(groupId, userId);
        return addNewUserToUserGroup(groupId, user);
      }
    }
    throw new ResourceNotFoundException("User Group with id [" + groupId + "] does not have a Group Member with id [" + userId + "]");
  }

  /**
   * Removes a UserGroup from the list of UserGroups the user belongs to( Also removes the user from
   * the list of Users who belong to a same UserGroup)
   *
   * @param groupId UUID of {@link com.example.login.model.UserGroup UserGroup}
   * @param userId UUID of {@link com.example.login.model.User User}
   */
  public void deleteUserGroupFromUser(UUID groupId, UUID userId) {

    if (!userGroupRepository.existsById(groupId)) {
      throw new ResourceNotFoundException("Group with Id [" + groupId + "] not found");
    }
    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException("User with id [" + userId + "] not found");
    } else {
      User user = userRepository.findUserById(userId);
      UserGroup userGroup = userGroupRepository.findUserGroupById(groupId);

      Set<UserGroup> userGroupList = user.getUserGroups();
      Set<User> userList = userGroup.getUsers();

      userGroupList.remove(userGroup);
      userList.remove(user);

      userRepository.save(user);
      userGroupRepository.save(userGroup);
    }
  }

  /**
   * Display all the events a group is hosting
   *
   * @param groupId UUID of {@link com.example.login.model.UserGroup UserGroup}
   * @return Returns the {@link List} of Event
   */
  public List<Event> getAllHostedEventsInGroup(UUID groupId) {
    if (userGroupRepository.existsById(groupId)) {
      UserGroup userGroup = userGroupRepository.findUserGroupById(groupId);
      return eventRepository.findByGroupId(userGroup);
    } else {
      throw new ResourceNotFoundException("Group with id [" + groupId + "] not found");
    }
  }

  /**
   * Create a new Event in a Group
   *
   * @param groupId UUID of {@link com.example.login.model.Event Event}
   * @return Returns the Event
   */
  public Event createNewHostedEventInGroup(UUID groupId, Event event) {
    if (userGroupRepository.existsById(groupId)) {
      UserGroup userGroup = userGroupRepository.findUserGroupById(groupId);
      event.setGroupId(userGroup);
      return eventRepository.save(event);
    } else {
      throw new ResourceNotFoundException("Group with id [" + groupId + "] not found");
    }
  }

  /**
   * Display an event that a group is hosting
   *
   * @param groupId UUID of {@link com.example.login.model.UserGroup UserGroup}
   * @param hostedEventId UUID of {@link com.example.login.model.Event Event}
   * @return Returns the Event
   */
  public Event getHostedEventInGroup(UUID groupId, UUID hostedEventId) {
    if (!userGroupRepository.existsById(groupId)) {
      throw new ResourceNotFoundException("User Group with id [" + groupId + "] not found");
    }
    UserGroup userGroup = userGroupRepository.findUserGroupById(groupId);
    List<Event> eventList = eventRepository.findByGroupId(userGroup);
    for(Event event : eventList){
      if(event.getId().equals(hostedEventId)) return event;
    }
    throw new ResourceNotFoundException("User Group with id [" + groupId + "] does not have a Event with id [" + hostedEventId + "]");
  }

  /**
   * Update an event that a group is hosting
   *
   * @param groupId UUID of {@link com.example.login.model.UserGroup UserGroup}
   * @param hostedEventId UUID of {@link com.example.login.model.Event Event}
   * @return Returns the updated Event
   */
  public Event updateHostedEventInGroup(UUID groupId, UUID hostedEventId, Event event) {
    if (!userGroupRepository.existsById(groupId)) {
      throw new ResourceNotFoundException("User Group with id [" + groupId + "] not found");
    }
    UserGroup userGroup = userGroupRepository.findUserGroupById(groupId);
    List<Event> eventList = eventRepository.findByGroupId(userGroup);
    for(Event e : eventList){
      if(e.getId().equals(hostedEventId)) {
        event.setId(hostedEventId);
        event.setGroupId(e.getGroupId());
        return eventRepository.save(event);
      }
    }
    throw new ResourceNotFoundException("User Group with id [" + groupId + "] does not have a Event with id [" + hostedEventId + "]");
  }

  /**
   * Delete an event that a group is hosting
   *
   * @param groupId UUID of {@link com.example.login.model.UserGroup UserGroup}
   * @param hostedEventId UUID of {@link com.example.login.model.Event Event}
   * @return Returns the updated Event
   */
  public void deleteHostedEventInGroup(UUID groupId, UUID hostedEventId) {
    if (! userGroupRepository.existsById(groupId)) {
      throw new ResourceNotFoundException("Group with id [" + groupId + "] not found");
    }
    if(! eventRepository.existsById(hostedEventId)){
      throw new ResourceNotFoundException("Event with id [" + hostedEventId + "] not found");
    }
    UserGroup userGroup = userGroupRepository.findUserGroupById(groupId);
    Event event = eventRepository.findById(hostedEventId).get();
    if(event.getGroupId().equals(userGroup)) {
      deleteChildEvents(event);
    }else{
      throw new ResourceNotFoundException("Event with id [" + hostedEventId + "] not found in Group [" + groupId + "]");
    }
  }

  private void deleteChildEvents(Event event) {
    if (event.getChildEvents().isEmpty()) {
      deleteEvent(event);
    }
    else {
      for(Event e: event.getChildEvents()) {
        deleteChildEvents(e);
        deleteEvent(e);
      }
      deleteEvent(event);
    }
  }

  public void deleteEvent(Event event){
    if(eventRepository.existsById(event.getId())) {
      event.setGroupId(null);
      event.setParentEvent(null);
      eventRepository.deleteById(event.getId());
    }
  }

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
      throw new ResourceNotFoundException("Group with id [" + groupName + "] not found");
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
