package com.example.bck.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeacherDTO {
  private Long id;
  private String firstName;
  private String lastName;

  public TeacherDTO() {
  }
}