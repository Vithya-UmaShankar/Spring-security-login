package com.example.login.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.Type;

/**
 * The UserGroup entity is used to club users into groups
 *
 * <p>
 * <b>Note:</b>
 * <p>
 * Repository: {@link com.example.login.repository.UserGroupRepository UserGroupRepository}
 * <p>
 * Service: {@link com.example.login.service.UserGroupService UserGroupService}
 * <p>
 * Controller: {@link com.example.login.controller.UserGroupController UserGroupController}
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Entity
@Table(name = "user_group")
public class UserGroup implements Serializable {

  @Id
  @Type(type = "uuid-char")
  private UUID id = UUID.randomUUID();

  @NotBlank
  @Column(name = "group_name", columnDefinition = "varchar(100)")
  private String groupName;

  @ManyToMany(fetch = FetchType.EAGER,
      cascade = {
          CascadeType.PERSIST,
          CascadeType.MERGE,
          CascadeType.REMOVE
      },
      mappedBy = "userGroups")
  @JsonIgnore
  private Set<User> users = new HashSet<>();

  public UUID getId() {
    return id;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

}