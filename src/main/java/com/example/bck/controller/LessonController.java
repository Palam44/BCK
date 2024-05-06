package com.example.bck.controller;

import com.example.bck.model.Lesson;
import com.example.bck.service.LessonService;
import java.time.DayOfWeek;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonController {
  private final LessonService lessonService;

  public LessonController(LessonService lessonService) {
    this.lessonService = lessonService;
  }

  @GetMapping
  public List<Lesson> getAllLessons() {
    return lessonService.findAll();
  }

  @PostMapping
  public Lesson createLesson(@RequestBody Lesson lesson) {
    return lessonService.save(lesson);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Lesson> getLessonById(@PathVariable Long id) {
    return lessonService.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Lesson> updateLesson(@PathVariable Long id, @RequestBody Lesson lessonDetails) {
    return lessonService.update(id, lessonDetails)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
    lessonService.delete(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/by-group")
  public ResponseEntity<List<String>> getLessonsByGroupAndOptionallyDay(
      @RequestParam Long groupId,
      @RequestParam(required = false) DayOfWeek dayOfWeek) {
    List<Lesson> lessons = lessonService.findLessonsByGroupAndDay(groupId, dayOfWeek);
    List<String> formattedLessons = lessons.stream()
        .map(lesson -> String.format("%s - %s (%s)",
            lesson.getTime().toString(),
            lesson.getDiscipline().getName(),
            lesson.getTeacher().getLastName()))
        .collect(Collectors.toList());
    return ResponseEntity.ok(formattedLessons);
  }
}