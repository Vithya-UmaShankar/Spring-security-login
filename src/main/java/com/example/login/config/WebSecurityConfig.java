package com.example.login.config;

import com.example.login.model.CustomOAuth2User;
import com.example.login.model.User;
import com.example.login.service.CustomOAuth2UserService;
import com.example.login.service.UserService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Configuration file of Spring Security
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  UserService userService;

  @Autowired
  CustomOAuth2UserService customOAuth2UserService;

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/api/v*/**", "/signup", "/login", "/oauth/**", "/error")
        .permitAll()
        .anyRequest()
        .authenticated().and()
        .formLogin()
        .loginProcessingUrl("/loginCheck")
        .loginPage("/login")
        .usernameParameter("email")
        .passwordParameter("password")
        .defaultSuccessUrl("/")
        .successHandler(new AuthenticationSuccessHandler() {

          @Override
          public void onAuthenticationSuccess(HttpServletRequest request,
              HttpServletResponse response,
              Authentication authentication) throws IOException, ServletException {

            response.sendRedirect("/");
          }
        })
        .failureHandler(new AuthenticationFailureHandler() {

          @Override
          public void onAuthenticationFailure(HttpServletRequest request,
              HttpServletResponse response,
              AuthenticationException exception) throws IOException, ServletException {

            if (exception.getMessage().equals("Bad credentials")) {
              response.sendRedirect("/login?badCred=true");
            }

            if (exception.getMessage().equals("User is disabled")) {
              response.sendRedirect("/login?userDisabled=true");
            }
          }
        })
        .permitAll()
        .and()
        .oauth2Login()
        .loginPage("/login")
        .userInfoEndpoint()
        .userService(customOAuth2UserService)
        .userAuthoritiesMapper(this.userAuthoritiesMapper())
        .and()
        .successHandler(new AuthenticationSuccessHandler() {
          @Override
          public void onAuthenticationSuccess(HttpServletRequest request,
              HttpServletResponse response,
              Authentication authentication) throws IOException, ServletException {
            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

            String screenName = oauthUser.getName();
            String contactEmail = oauthUser.getEmail();
            String providerName = oauthUser.getOauth2ClientName();
            User user = userService.processOAuthPostLogin(screenName, contactEmail, providerName);

            response.sendRedirect("/");

          }
        })
        .failureHandler(new AuthenticationFailureHandler() {

          @Override
          public void onAuthenticationFailure(HttpServletRequest request,
              HttpServletResponse response,
              AuthenticationException exception) throws IOException, ServletException {

            if (exception.getMessage().equals("Email exists")) {
              response.sendRedirect("/login?emailExists=true");
            } else {
              response.sendRedirect("/login?error=true");
            }
          }
        })
        .and()
        .logout().logoutSuccessUrl("/login").permitAll();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(bCryptPasswordEncoder);
    provider.setUserDetailsService(userService);
    return provider;
  }

  private GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities) -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      mappedAuthorities.add(new SimpleGrantedAuthority("USER"));
      return mappedAuthorities;
    };
  }
}