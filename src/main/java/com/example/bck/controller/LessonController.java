package com.example.bck.controller;

import com.example.bck.dto.LessonDTO;
import com.example.bck.service.LessonService;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Slf4j
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
  public ResponseEntity<?> createLesson(@Valid @RequestBody LessonDTO lessonDTO) {
    try {
      if (lessonDTO.getDuration() <= 0) {

       // log.warning("Attempt to create a lesson with non-positive duration");
        return ResponseEntity.badRequest().body("Duration must be positive and greater than zero.");
      }
      LessonDTO createdLessonDTO = lessonService.save(lessonDTO);
    //  log.info("Created a new lesson with id: {}", createdLessonDTO.getId());
      return new ResponseEntity<>(createdLessonDTO, HttpStatus.CREATED);
    } catch (RuntimeException ex) {
    //  log.error("Error creating lesson: {}", ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
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
  public ResponseEntity<?> updateLesson(@PathVariable Long id, @Valid @RequestBody LessonDTO lessonDTO) {
    try {
      if (lessonDTO.getDuration() <= 0) {
        //log.warn("Attempt to update a lesson with non-positive duration");
        return ResponseEntity.badRequest().body("Duration must be positive and greater than zero.");
      }
      LessonDTO updatedLessonDTO = lessonService.update(id, lessonDTO);
    //  log.info("Updated lesson with id: {}", id);
      return ResponseEntity.ok(updatedLessonDTO);
    } catch (RuntimeException ex) {
     // log.error("Error updating lesson with id {}: {}", id, ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
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
        .toList();
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