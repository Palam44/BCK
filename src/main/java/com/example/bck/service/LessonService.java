package com.example.bck.service;

import com.example.bck.model.Lesson;
import com.example.bck.repository.LessonRepository;
import java.time.DayOfWeek;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {
  private final LessonRepository lessonRepository;

  public LessonService(LessonRepository lessonRepository) {
    this.lessonRepository = lessonRepository;
  }

  public List<Lesson> findAll() {
    return lessonRepository.findAll();
  }

  public Optional<Lesson> findById(Long id) {
    return lessonRepository.findById(id);
  }

  @Transactional
  public Lesson save(Lesson lesson) {
    return lessonRepository.save(lesson);
  }

  @Transactional
  public Optional<Lesson> update(Long id, Lesson lessonDetails) {
    return lessonRepository.findById(id)
        .map(lesson -> {
          lesson.setDayOfWeek(lessonDetails.getDayOfWeek());
          lesson.setTime(lessonDetails.getTime());
          lesson.setDiscipline(lessonDetails.getDiscipline());
          lesson.setTeacher(lessonDetails.getTeacher());
          lesson.setGroup(lessonDetails.getGroup());
          return lessonRepository.save(lesson);
        });
  }

  @Transactional
  public void delete(Long id) {
    lessonRepository.deleteById(id);
  }

  public List<Lesson> findLessonsByGroupAndDay(Long groupId, DayOfWeek dayOfWeek) {
    return lessonRepository.findByGroupIdAndDayOfWeekOrderByTime(groupId, dayOfWeek);
  }
}
