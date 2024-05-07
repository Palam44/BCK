package com.example.bck.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "teacher")
public class Teacher {

  @Setter
  @Getter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Getter
  private String firstName;

  @Setter
  @Getter
  private String lastName;

  @OneToMany(mappedBy = "teacher")
  @Setter
  @Getter
  private List<Lesson> lesson;

}