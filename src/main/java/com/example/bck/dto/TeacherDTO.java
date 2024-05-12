package com.example.bck.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class TeacherDTO {
  private Long id;

  @NotBlank(message = "First name is mandatory")
  @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
  private String firstName;

  @NotBlank(message = "Last name is mandatory")
  @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
  private String lastName;

  public TeacherDTO() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public @NotBlank(message = "First name is mandatory") @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters") String getFirstName() {
    return firstName;
  }

  public void setFirstName(
      @NotBlank(message = "First name is mandatory") @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters") String firstName) {
    this.firstName = firstName;
  }

  public @NotBlank(message = "Last name is mandatory") @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters") String getLastName() {
    return lastName;
  }

  public void setLastName(
      @NotBlank(message = "Last name is mandatory") @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters") String lastName) {
    this.lastName = lastName;
  }
}