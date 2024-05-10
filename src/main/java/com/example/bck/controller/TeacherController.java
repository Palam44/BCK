package com.example.bck.controller;

import com.example.bck.dto.TeacherDTO;
import com.example.bck.service.TeacherService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

  private final TeacherService teacherService;

  @Autowired
  public TeacherController(TeacherService teacherService) {
    this.teacherService = teacherService;
  }

  @GetMapping
  public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
    List<TeacherDTO> teachers = teacherService.findAll();
    return ResponseEntity.ok(teachers);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
    TeacherDTO teacher = teacherService.findById(id);
    return ResponseEntity.ok(teacher);
  }

  @PostMapping
  public ResponseEntity<TeacherDTO> createTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
    TeacherDTO createdTeacher = teacherService.save(teacherDTO);
    return new ResponseEntity<>(createdTeacher, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable Long id, @RequestBody TeacherDTO teacherDTO) {
    TeacherDTO updatedTeacher = teacherService.update(id, teacherDTO);
    return ResponseEntity.ok(updatedTeacher);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
    teacherService.delete(id);
    return ResponseEntity.noContent().build();
  }
}