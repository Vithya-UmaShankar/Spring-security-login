package com.example.login.model;

import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Entity that change the return token's of oAuth apis into a much better readable format. Extends
 * {@link OAuth2User}
 *
 * <p>
 * <b>Note:</b>
 * <p>
 * Service: {@link com.example.login.service.CustomOAuth2UserService CustomOAuth2UserService}
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

  private OAuth2User oAuth2User;
  private String oauth2ClientName;

  @Override
  public Map<String, Object> getAttributes() {
    return oAuth2User.getAttributes();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return oAuth2User.getAuthorities();
  }

  @Override
  public String getName() {
    return oAuth2User.getAttribute("name");
  }

  /**
   * This method is used to return the email with which was used during oAuth sign-in
   *
   * @return Returns the email id.
   */
  public String getEmail() {
    return oAuth2User.<String>getAttribute("email");
  }
}