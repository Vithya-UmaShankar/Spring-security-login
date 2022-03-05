package com.example.login.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This class contains some of the common columns in different tables like name, description,
 * created time,...
 *
 * @author Vithya Uma Shankar
 * @version 1.0
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@MappedSuperclass
public class BaseModel {

  @NotBlank
  @Column(name = "name", columnDefinition = "varchar(255)")
  String name;

  @Column(name = "description", columnDefinition = "varchar(4000)")
  String description;

  @Column(name = "time_created", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
  Date timeCreated = new Date();

  @Column(name = "time_updated", nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
  Date timeUpdated = new Date();

  @Column(name = "internal_properties")
  String internalProperties;
}