package com.example.bck.repository;

import com.example.bck.model.Discipline;
import com.example.bck.model.Teacher;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

  // Метод для поиска преподавателей по дисциплине
  List<Teacher> findByDisciplinesContains(Discipline discipline);

}
