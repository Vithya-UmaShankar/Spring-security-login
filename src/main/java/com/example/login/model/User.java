package com.example.login.model;

import com.example.login.validator.CustomEmail;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The User entity extends {@link com.example.login.model.BaseModel BaseModel} and is used to link
 * to the "account_user" table
 *
 * <p>
 * <b>Note:</b>
 * <p>
 * Repository: {@link com.example.login.repository.UserRepository UserRepository}
 * <p>
 * Service: {@link com.example.login.service.UserService UserService}
 * <p>
 * Controller: {@link com.example.login.controller.UserController UserController}
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Entity
@Table(name = "user")
public class User extends BaseModel implements Serializable, UserDetails {

  @Id
  @Type(type = "uuid-char")
  private UUID id = UUID.randomUUID();

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "account_id")
  private Account accountId;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_type", columnDefinition = "varchar(100)")
  private UserRole userType;

  @Enumerated(EnumType.STRING)
  @Column(name = "provider", columnDefinition = "varchar(20)")
  private Provider provider;

  @Column(name = "is_admin", columnDefinition = "char")
  private String isAdmin;

  @CustomEmail
  @Column(name = "contact_email", columnDefinition = "varchar(100)")
  private String contactEmail;

  @Column(name = "profile_image", columnDefinition = "blob")
  private String profileImage;

  @Column(name = "is_primary", columnDefinition = "char")
  private String isPrimary;

  @Column(name = "cell_phone", columnDefinition = "varchar(36)")
  private String cellPhone;

  @NotBlank
  @Column(name = "screen_name", columnDefinition = "varchar(100)")
  private String screenName;

  @Column(name = "password", columnDefinition = "varchar(100)")
  private String password;

  @Column(name = "visibility_status", columnDefinition = "varchar(100)")
  private String visibilityStatus;

  @Column(name = "address", columnDefinition = "varchar(100)")
  private String address;

  @Column(name = "account_usercol", columnDefinition = "varchar(45)")
  private String accountUsercol;

  @Column(name = "enabled", nullable = false)
  private boolean enabled = false;

  @Column(name = "locked", nullable = false)
  private boolean locked = false;

  @ManyToMany(fetch = FetchType.EAGER,
      cascade = {
          CascadeType.PERSIST,
          CascadeType.MERGE,
          CascadeType.REMOVE
      })
  @JoinTable(name = "user_in_group",
      joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "group_id")})
  private Set<UserGroup> userGroups = new HashSet<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userType.name());
    return Collections.singletonList(authority);
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getUsername() {
    return contactEmail;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !locked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  //Getters and Setters

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void addUserGroups(UserGroup userGroup) {
    this.userGroups.add(userGroup);
    userGroup.getUsers().add(this);
  }

  public UUID getId() {
    return id;
  }

  public Account getAccountId() {
    return accountId;
  }

  public void setAccountId(Account accountId) {
    this.accountId = accountId;
  }

  public UserRole getUserType() {
    return userType;
  }

  public void setUserType(UserRole userType) {
    this.userType = userType;
  }

  public Provider getProvider() {
    return provider;
  }

  public void setProvider(Provider provider) {
    this.provider = provider;
  }

  public String getIsAdmin() {
    return isAdmin;
  }

  public void setIsAdmin(String isAdmin) {
    this.isAdmin = isAdmin;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public String getProfileImage() {
    return profileImage;
  }

  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }

  public String getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(String isPrimary) {
    this.isPrimary = isPrimary;
  }

  public String getCellPhone() {
    return cellPhone;
  }

  public void setCellPhone(String cellPhone) {
    this.cellPhone = cellPhone;
  }

  public String getScreenName() {
    return screenName;
  }

  public void setScreenName(String screenName) {
    this.screenName = screenName;
  }

  public String getVisibilityStatus() {
    return visibilityStatus;
  }

  public void setVisibilityStatus(String visibilityStatus) {
    this.visibilityStatus = visibilityStatus;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAccountUsercol() {
    return accountUsercol;
  }

  public void setAccountUsercol(String accountUsercol) {
    this.accountUsercol = accountUsercol;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  public Set<UserGroup> getUserGroups() {
    return userGroups;
  }

  public void setUserGroups(Set<UserGroup> userGroups) {
    this.userGroups = userGroups;
  }
}
