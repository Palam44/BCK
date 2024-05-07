package com.example.bck.repository;

import com.example.bck.model.Group;
import com.example.bck.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

  @Query("SELECT l FROM Lesson l WHERE l.group = :group AND (:dayOfWeek IS NULL OR l.dayOfWeek = :dayOfWeek) ORDER BY l.time")
  List<Lesson> findByGroupAndDayOfWeekOrderByTime(@Param("group") Group group, @Param("dayOfWeek") DayOfWeek dayOfWeek);
}
