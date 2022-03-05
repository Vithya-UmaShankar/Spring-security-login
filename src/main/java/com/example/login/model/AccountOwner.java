package com.example.login.model;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

/**
 * The AccountOwner entity extends {@link com.example.login.model.BaseModel BaseModel} It is used to
 * denote the owner of an {@link com.example.login.model.Account Account}
 *
 * <p>
 * <b>Note:</b>
 * <p>
 * Repository: {@link com.example.login.repository.AccountOwnerRepository AccountOwnerRepository}
 * <p>
 * Service: {@link com.example.login.service.AccountOwnerService AccountOwnerService}
 * <p>
 * Controller: {@link com.example.login.controller.AccountOwnerController AccountOwnerController}
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "account_owner")
public class AccountOwner implements Serializable {

  @Id
  @Type(type = "uuid-char")
  private UUID id = UUID.randomUUID();

  @NotBlank
  @Column(name = "name", columnDefinition = "varchar(100)")
  private String name;

  //@Size(min = 8, max = 16, message = "Password must be between 8 and 16")
  @Column(name = "password", columnDefinition = "varchar(100)")
  private String password;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "account_id")
  private Account account;

}
