package com.example.bck.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LessonDTO {

  private Long id;
  private DayOfWeek dayOfWeek;
  private LocalTime time;
  @Setter
  @Getter
  private DisciplineDTO discipline;
  @Setter
  @Getter
  private TeacherDTO teacher;
  @Setter
  @Getter
  private GroupDTO group;

  public LessonDTO() {
  }
}
