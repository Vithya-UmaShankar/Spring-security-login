package com.example.login.service;

import com.example.login.model.CustomOAuth2User;
import com.example.login.model.User;
import com.example.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * Service that helps to get details about {@link oAuth2User}
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  @Autowired
  UserRepository userRepository;

  /**
   * This method is used to convert the oAuth2 Api results to {@link OAuth2User}. This method also
   * checks to make sure the email id used with oAuth2 is unique
   *
   * @param userRequest Object of {@link OAuth2UserRequest}.
   * @return Returns the object of {@link OAuth2User}.
   */
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    String clientName = userRequest.getClientRegistration().getClientName();
    OAuth2User user = super.loadUser(userRequest);

    CustomOAuth2User customOAuth2User = new CustomOAuth2User(user, clientName);
    String contactEmail = customOAuth2User.getEmail();

    if (userRepository.existsUserByContactEmail(contactEmail)) {
      User _user = userRepository.findByContactEmail(contactEmail);
      if (_user.getProvider().equals("LOCAL")) {
        throw new BadCredentialsException("Email exists");
      }
    }

    return customOAuth2User;
  }
}
