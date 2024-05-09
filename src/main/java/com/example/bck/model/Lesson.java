package com.example.bck.model;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "lesson")
public class Lesson {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Enumerated(EnumType.STRING)
  private DayOfWeek dayOfWeek; // День недели
  @Column(name = "start_time")
  private LocalTime time; // Время начала
  @ManyToOne
  private Discipline discipline;

  @ManyToOne
  private Teacher teacher;

  @ManyToOne
  @JoinColumn(name = "group_id", referencedColumnName = "id")
  private Group group;

}
