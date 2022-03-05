package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.repository.UserRepository;
import com.example.login.service.UserService;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller of {@link com.example.login.model.User User} entity
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@RestController
@RequestMapping("api/v1/user")
public class UserController {

  @Autowired
  UserService userService;
  @Autowired
  UserRepository userRepository;

  /**
   * This method is used to create a new User
   *
   * @param user        Instance of User that has to be inserted.
   * @param accountName Name of an {@link com.example.login.model.Account Account}.
   * @return Returns the instance of User after it has been inserted.
   */
  @PostMapping("/{accountName}")
  public User createUser(@RequestBody @Valid User user,
      @PathVariable String accountName) {
    return userService.createUser(user, accountName);
  }

  /**
   * This method is used to return all the users in User table
   *
   * @return Returns the {@link List} of Users.
   */
  @GetMapping("/")
  public List<User> findAllUser() {
    return userService.findAllUser();
  }

  /**
   * This method is used to find a User using id
   *
   * @param userId UUID of a {@link com.example.login.model.User User}.
   * @return Returns the User.
   */
  @GetMapping("/{userId}")
  public User getUserByUserId(@PathVariable @NotBlank UUID userId) {
    return userService.getUserByUserId(userId);
  }

  /**
   * This method is used to delete a User
   *
   * @param userId UUID of a {@link com.example.login.model.User User}.
   */
  @Transactional
  @DeleteMapping("/{contactEmail}")
  public void deleteUser(@PathVariable @NotBlank String contactEmail) {
    userService.deleteUser(contactEmail);
  }

  /**
   * This method is used to update a new User
   *
   * @param user Instance of User that has to be updated.
   * @return Returns the instance of User after it has been updated.
   */
  @PutMapping("/")
  public User updateUser(@RequestBody @Valid User user) {
    return userService.updateUser(user);
  }
}