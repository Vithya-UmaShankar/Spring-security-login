package com.example.login.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.login.model.Account;
import com.example.login.model.AccountType;
import com.example.login.model.ConfirmationToken;
import com.example.login.model.User;
import com.example.login.model.UserRole;
import com.example.login.repository.ConfirmationTokenRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConfirmationTokenServiceTest {

  @Mock
  ConfirmationTokenRepository confirmationTokenRepository;

  @InjectMocks
  ConfirmationTokenService confirmationTokenService;

  ConfirmationToken confirmationToken = new ConfirmationToken();
  User user = new User();
  Account account = new Account();

  @BeforeEach
  void setUp() {
    account.setAccountType(AccountType.BASIC);
    account.setName("Name");
    account.setDescription("Description");

    user.setUserType(UserRole.USER);
    user.setIsAdmin("y");
    user.setContactEmail("abc@gmail.com");
    user.setScreenName("Abc");
    user.setName("Name");
    user.setDescription("Description");
    user.setAccountId(account);

    confirmationToken.setToken(UUID.randomUUID().toString());
    confirmationToken.setCreatedAt(LocalDateTime.now());
    confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
    confirmationToken.setUserId(user);
  }

  @Test
  void saveConfirmationToken() {
    when(
        confirmationTokenRepository.save(ArgumentMatchers.any(ConfirmationToken.class))).thenReturn(
        confirmationToken);
    when(confirmationTokenRepository.save(confirmationToken)).thenReturn(confirmationToken);
    ConfirmationToken confirmationTokenFetch = confirmationTokenService.saveConfirmationToken(
        confirmationToken);

    assertThat(confirmationTokenFetch).isEqualTo(confirmationToken);
  }

  @Test
  void getToken() {
    when(confirmationTokenRepository.existsConfirmationTokenByToken(
        confirmationToken.getToken())).thenReturn(true);
    when(confirmationTokenRepository.findByToken(confirmationToken.getToken())).thenReturn(
        confirmationToken);
    ConfirmationToken confirmationTokenFetch = confirmationTokenService.getToken(
        confirmationToken.getToken());

    assertThat(confirmationTokenFetch).isEqualTo(confirmationToken);
  }

  @Test
  void updateTokenConfirmedAt() {
    LocalDateTime time = LocalDateTime.now();
    when(confirmationTokenRepository.existsConfirmationTokenByToken(
        confirmationToken.getToken())).thenReturn(true);
    doNothing().when(confirmationTokenRepository)
        .updateConfirmedAt(confirmationToken.getToken(), time);
    confirmationTokenService.updateTokenConfirmedAt(confirmationToken.getToken(), time);
    ConfirmationToken confirmationTokenFetch = confirmationTokenService.getToken(
        confirmationToken.getToken());

    //assertThat(confirmationTokenFetch.getConfirmedAt()).isEqualTo(time);
  }
}