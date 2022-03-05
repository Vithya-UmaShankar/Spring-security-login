package com.example.login.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

/**
 * The Account entity extends {@link com.example.login.model.BaseModel BaseModel} and is used to
 * link to the "account" table
 *
 * <p>
 * <b>Note:</b>
 * <p>
 * Repository: {@link com.example.login.repository.AccountRepository AccountRepository}
 * <p>
 * Service: {@link com.example.login.service.AccountService AccountService}
 * <p>
 * Controller: {@link com.example.login.controller.AccountController AccountController}
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account extends BaseModel implements Serializable {

  @Id
  @Type(type = "uuid-char")
  private UUID id = UUID.randomUUID();

  @Enumerated(EnumType.STRING)
  @Column(name = "account_type", columnDefinition = "varchar(400)")
  private AccountType accountType;

  @Enumerated(EnumType.STRING)
  @Column(name = "account_status", columnDefinition = "varchar(400)")
  private Status accountStatus;

  @Column(name = "is_internal", columnDefinition = "char")
  private String isInternal;

  @Column(name = "time_active_from")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
  private Date timeActiveFrom;

  @Column(name = "time_active_until")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
  private Date timeActiveUntil;

  @Column(name = "time_access_restricted_from")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
  private Date timeAccessRestrictedFrom;

  @Column(name = "time_access_restricted_until")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
  private Date timeAccessRestrictedUntil;

}