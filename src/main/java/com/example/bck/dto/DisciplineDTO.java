package com.example.bck.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class DisciplineDTO {

  private Long id;
  @NotBlank(message = "Name is mandatory")
  @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
  private String name;

  public DisciplineDTO() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public @NotBlank(message = "Name is mandatory") @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters") String getName() {
    return name;
  }

  public void setName(
      @NotBlank(message = "Name is mandatory") @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters") String name) {
    this.name = name;
  }
}