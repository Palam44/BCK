package com.example.bck.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GroupDTO {
  private Long id;
  @NotBlank(message = "Group name is mandatory")
  @Size(min = 1, max = 100, message = "Group name must be between 1 and 100 characters")
  private String name;

  public GroupDTO() {
  }
}