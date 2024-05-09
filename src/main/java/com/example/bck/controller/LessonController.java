package com.example.bck.controller;

import com.example.bck.dto.LessonDTO;
import com.example.bck.service.LessonService;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<List<LessonDTO>> getAllLessons() {
    List<LessonDTO> lessonDTOs = lessonService.findAll();
    return ResponseEntity.ok(lessonDTOs);
  }

  @PostMapping
  public ResponseEntity<LessonDTO> createLesson(@RequestBody LessonDTO lessonDTO) {
    try {
      LessonDTO createdLessonDTO = lessonService.save(lessonDTO);
      return new ResponseEntity<>(createdLessonDTO, HttpStatus.CREATED);
    } catch (RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<LessonDTO> getLessonById(@PathVariable Long id) {
    try {
      LessonDTO lessonDTO = lessonService.findById(id);
      return ResponseEntity.ok(lessonDTO);
    } catch (RuntimeException ex) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<LessonDTO> updateLesson(@PathVariable Long id, @RequestBody LessonDTO lessonDTO) {
    try {
      LessonDTO updatedLessonDTO = lessonService.update(id, lessonDTO);
      return ResponseEntity.ok(updatedLessonDTO);
    } catch (RuntimeException ex) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
    boolean isDeleted = lessonService.delete(id);
    if (isDeleted) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/by-group")
  public ResponseEntity<List<String>> getLessonsByGroupAndOptionallyDay(
      @RequestParam Long groupId,
      @RequestParam(required = false) DayOfWeek dayOfWeek) {
    List<LessonDTO> lessonDTOs = lessonService.findLessonsByGroupAndDay(groupId, dayOfWeek);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    List<String> formattedLessons = lessonDTOs.stream()
        .map(lessonDTO -> String.format("%s - %s (%s)",
            lessonDTO.getTime().format(timeFormatter),
            lessonDTO.getDiscipline().getName(),
            lessonDTO.getTeacher().getLastName()))
        .collect(Collectors.toList());
    return ResponseEntity.ok(formattedLessons);
  }

  @PostMapping("/{lessonId}/assign-teacher")
  public ResponseEntity<LessonDTO> assignTeacherToLesson(
      @PathVariable Long lessonId,
      @RequestParam Long teacherId,
      @RequestParam Long disciplineId) {
    LessonDTO updatedLessonDTO = lessonService.assignTeacherToLesson(lessonId, teacherId, disciplineId);
    return ResponseEntity.ok(updatedLessonDTO);
  }
}