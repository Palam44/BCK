package com.example.bck.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;


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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public @NotNull(message = "Day of week is mandatory") DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(
      @NotNull(message = "Day of week is mandatory") DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public @NotNull(message = "Lesson time is mandatory") @FutureOrPresent(message = "Lesson time must be in the future or present") LocalTime getTime() {
    return time;
  }

  public void setTime(
      @NotNull(message = "Lesson time is mandatory") @FutureOrPresent(message = "Lesson time must be in the future or present") LocalTime time) {
    this.time = time;
  }

  public @NotNull(message = "Discipline is mandatory") DisciplineDTO getDiscipline() {
    return discipline;
  }

  public void setDiscipline(
      @NotNull(message = "Discipline is mandatory") DisciplineDTO discipline) {
    this.discipline = discipline;
  }

  public @NotNull(message = "Teacher is mandatory") TeacherDTO getTeacher() {
    return teacher;
  }

  public void setTeacher(
      @NotNull(message = "Teacher is mandatory") TeacherDTO teacher) {
    this.teacher = teacher;
  }

  public @NotNull(message = "Group is mandatory") GroupDTO getGroup() {
    return group;
  }

  public void setGroup(
      @NotNull(message = "Group is mandatory") GroupDTO group) {
    this.group = group;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }
}
