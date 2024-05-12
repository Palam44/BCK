package com.example.bck.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class GroupDTO {
  private Long id;
  @NotBlank(message = "Group name is mandatory")
  @Size(min = 1, max = 100, message = "Group name must be between 1 and 100 characters")
  private String name;

  public GroupDTO() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public @NotBlank(message = "Group name is mandatory") @Size(min = 1, max = 100, message = "Group name must be between 1 and 100 characters") String getName() {
    return name;
  }

  public void setName(
      @NotBlank(message = "Group name is mandatory") @Size(min = 1, max = 100, message = "Group name must be between 1 and 100 characters") String name) {
    this.name = name;
  }
}