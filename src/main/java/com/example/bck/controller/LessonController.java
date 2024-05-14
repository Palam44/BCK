package com.example.bck.controller;

import com.example.bck.dto.LessonDTO;
import com.example.bck.service.LessonService;
import java.time.DayOfWeek;
import javax.validation.Valid;
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
  public ResponseEntity<LessonDTO> createLesson(@Valid @RequestBody LessonDTO lessonDTO) {
    if (lessonDTO.getDuration() <= 0) {
      throw new IllegalArgumentException("Продолжительность должна быть положительной и больше нуля.");
    }
    LessonDTO createdLessonDTO = lessonService.save(lessonDTO);
    return new ResponseEntity<>(createdLessonDTO, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<LessonDTO> getLessonById(@PathVariable Long id) {
    LessonDTO lessonDTO = lessonService.findById(id);
    return ResponseEntity.ok(lessonDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<LessonDTO> updateLesson(@PathVariable Long id, @Valid @RequestBody LessonDTO lessonDTO) {
    if (lessonDTO.getDuration() <= 0) {
      throw new IllegalArgumentException("Продолжительность должна быть положительной и больше нуля.");
    }
    LessonDTO updatedLessonDTO = lessonService.update(id, lessonDTO);
    return ResponseEntity.ok(updatedLessonDTO);
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
    List<String> formattedLessons = lessonService.getFormattedLessonsByGroupAndDay(groupId, dayOfWeek);
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