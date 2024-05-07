package com.example.bck.controller;

import com.example.bck.model.Lesson;
import com.example.bck.service.LessonService;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lessons")
public class LessonController {
  private final LessonService lessonService;
  private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

  public LessonController(LessonService lessonService) {
    this.lessonService = lessonService;
  }

  @GetMapping
  public ResponseEntity<List<Lesson>> getAllLessons() {
    return ResponseEntity.ok(lessonService.findAll());
  }

  @PostMapping
  public ResponseEntity<Lesson> createLesson(@Valid @RequestBody Lesson lesson) {
    Lesson createdLesson = lessonService.save(lesson);
    return new ResponseEntity<>(createdLesson, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Lesson> getLessonById(@PathVariable Long id) {
    return lessonService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Lesson> updateLesson(@PathVariable Long id, @Valid @RequestBody Lesson lessonDetails) {
    return lessonService.update(id, lessonDetails)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
    boolean isDeleted = lessonService.delete(id);
    return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }

  @GetMapping("/by-group")
  public ResponseEntity<List<String>> getLessonsByGroupAndOptionallyDay(
      @RequestParam Long groupId,
      @RequestParam(required = false) DayOfWeek dayOfWeek) {
    List<Lesson> lessons = lessonService.findLessonsByGroupAndDay(groupId, dayOfWeek);
    List<String> formattedLessons = lessons.stream()
        .map(lesson -> String.format("%s - %s (%s)",
            lesson.getTime().format(timeFormatter),
            lesson.getDiscipline().getName(),
            lesson.getTeacher().getLastName()))
        .collect(Collectors.toList());
    return ResponseEntity.ok(formattedLessons);
  }
}