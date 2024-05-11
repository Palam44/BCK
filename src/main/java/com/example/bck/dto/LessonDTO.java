package com.example.bck.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LessonDTO {

  private Long id;
  @NotNull(message = "Day of week is mandatory")
  private DayOfWeek dayOfWeek;
  @NotNull(message = "Lesson time is mandatory")
  @FutureOrPresent(message = "Lesson time must be in the future or present")
  private LocalTime time;
  @NotNull(message = "Discipline is mandatory")
  private DisciplineDTO discipline;
  @NotNull(message = "Teacher is mandatory")
  private TeacherDTO teacher;
  @NotNull(message = "Group is mandatory")
  private GroupDTO group;
  private int duration; // Продолжительность занятия в минутах

  public LessonDTO() {
  }
}
