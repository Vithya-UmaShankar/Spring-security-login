package com.example.login.controller;

import com.example.login.model.CustomOAuth2User;
import com.example.login.model.Registration;
import com.example.login.model.UserGroup;
import com.example.login.repository.UserGroupRepository;
import com.example.login.repository.UserRepository;
import com.example.login.service.ConfirmationTokenService;
import com.example.login.service.RegistrationService;
import com.example.login.service.UserGroupService;
import com.example.login.service.UserService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Uses Thymeleaf to return views to the userList
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Controller
@AllArgsConstructor
public class MainController {

  @Autowired
  UserService userService;

  @Autowired
  UserGroupService userGroupService;

  @Autowired
  UserGroupRepository userGroupRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RegistrationService registrationService;

  @Autowired
  ConfirmationTokenService confirmationTokenService;

  /**
   * Used to bind the sign-up form to java object
   *
   * @param dataBinder Object of {@link WebDataBinder}. Binds request parameters in forms to
   *                   JavaBean object
   */
  @InitBinder
  public void initBinder(WebDataBinder dataBinder) {
    StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
    dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
  }

  /**
   * Used to display the login webpage
   *
   * @param model Object of {@link Model}
   */
  @GetMapping(value = {"/login"})
  public String login(Model model) {
    return "loginPage";
  }

  /**
   * Used to display the welcome webpage. Will be displayed only if userList is logged in.
   *
   * @param model     Object of {@link Model}
   * @param principal Object of {@link Principal}
   */
  @GetMapping(value = {"/"})
  public String welcome(Model model, Principal principal,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

    String userName = principal.getName(); //Email is passed. User signed in locally

    if (!userName.contains("@")) {//Name is passed. User used alternate providers
      userName = customOAuth2User.getEmail();
    }
    model.addAttribute("Name", userService.getScreenNameByContactEmail(userName));
    UUID userId = userService.getIdByContactEmail(userName);

    List<UserGroup> user_usergroups = userGroupService.getAllUserGroupsByUserId(userId);
    List<String> user_userGroupsList = new ArrayList<>();
    for (UserGroup userGroup : user_usergroups) {
      user_userGroupsList.add(userGroup.getGroupName());
    }

    List<UserGroup> all_usergroups = userGroupRepository.findAll();
    List<String> all_UserGroupsList = new ArrayList<>();
    for (UserGroup userGroup : all_usergroups) {
      if (!user_userGroupsList.contains(userGroup.getGroupName())) {
        all_UserGroupsList.add(userGroup.getGroupName());
      }
    }

    model.addAttribute("all_UserGroups", all_UserGroupsList);
    model.addAttribute("user_userGroups", user_userGroupsList);
    return "welcomePage";
  }

  /**
   * Used to display the sign-up webpage
   *
   * @param model Object of {@link Model}
   */
  @GetMapping(value = {"/signup"})
  public String signup(Model model) {
    Registration registration = new Registration();
    model.addAttribute("registrationRequestForm", registration);

    return "signupPage";
  }

  /**
   * Used to display the sign-up webpage
   *
   * @param registration  Object of {@link Model}
   * @param bindingResult Object of {@link BindingResult}. Holds the errors (if any) during
   *                      validation.
   */
  @PostMapping(value = {"/signup"})
  public String signupSave(
      @Valid @ModelAttribute("registrationRequestForm") Registration registration,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return "signupPage";
    }

    String registerUser = registrationService.register(registration);

    if (registerUser.equals("success")) {
      return "redirect:/login?tokenRegenerated=true";
    } else {
      return "redirect:/login?emailExists=true";
    }
  }

  /**
   * This method is used to activate a User
   *
   * @param token UUID that is used to validate a User.
   * @return Returns a {@link Model}
   */
  @GetMapping("/api/v1/confirmationToken/confirm/{token}")
  public String confirm(@PathVariable @NotBlank String token, Model model) {
    String validationResult = registrationService.confirmToken(token);
    model.addAttribute("message", validationResult);
    return "validatePage";
  }

  /**
   * Used to add a user to a UserGroup
   *
   * @param groupName Name of the UserGroup
   * @param principal Object of {@link Principal}
   * @param model     Object of {@link Model}
   * @return Returns to user page.
   */
  @GetMapping("/api/v1/userGroup/addUser/{groupName}")
  public String addUserToUserGroup(@PathVariable @NotBlank String groupName, Principal principal,
      Model model,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    String userName = principal.getName(); //Email is passed. User signed in locally

    if (!userName.contains("@")) {//Name is passed. User used alternate providers
      userName = customOAuth2User.getEmail();
    }
    UserGroup userGroup = userGroupService.addUserToUserGroup(
        userName,
        groupName);
    return "redirect:/";
  }

  /**
   * Removes a UserGroup from the list of UserGroups that the user belongs to
   *
   * @param groupName Name of {@link com.example.login.model.UserGroup UserGroup}
   */
  @Transactional
  @GetMapping("/api/v1/userGroup/removeUser/{groupName}")
  public String deleteUserGroupFromUser(@PathVariable @NotBlank String groupName,
      Principal principal,
      Model model,
      @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    String userName = principal.getName(); //Email is passed. User signed in locally

    if (!userName.contains("@")) {//Name is passed. User used alternate providers
      userName = customOAuth2User.getEmail();
    }
    userGroupService.deleteUserGroupFromUser(userName, groupName);
    return "redirect:/";
  }

  /**
   * Deletes a UserGroup
   *
   * @param groupName Name of {@link com.example.login.model.UserGroup UserGroup}
   */
  @Transactional
  @GetMapping("/api/v1/userGroup/removeGroup/{groupName}")
  public String deleteUserGroup(@PathVariable @NotBlank String groupName, Model model) {
    userGroupService.deleteUserGroup(groupName);
    return "redirect:/";
  }
}

//Principal=CustomOAuth2User(oAuth2User=Name: [116296753982357099713], Granted Authorities: [[ROLE_USER, SCOPE_https://www.googleapis.com/auth/userinfo.email, SCOPE_https://www.googleapis.com/auth/userinfo.profile, SCOPE_openid]], User Attributes: [{sub=116296753982357099713, name=Rani Kapur, given_name=Rani, family_name=Kapur, picture=https://lh3.googleusercontent.com/a/AATXAJzUGJbuLr5KGL96Q5gpB16GPpzzOcqdwtaNYFyD=s96-c, email=rani.kapur.rani6@gmail.com, email_verified=true, locale=en}], oauth2ClientName=Google), Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=7BE766B1F8BEDFFDC24CA4168F7DCEC1], Granted Authorities=[USER]]