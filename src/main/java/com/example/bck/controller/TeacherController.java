package com.example.bck.controller;

import com.example.bck.model.Teacher;
import com.example.bck.service.TeacherService;
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
  public ResponseEntity<List<Teacher>> getAllTeachers() {
    List<Teacher> teachers = teacherService.findAll();
    return ResponseEntity.ok(teachers);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
    Teacher teacher = teacherService.findById(id);
    return ResponseEntity.ok(teacher);
  }

  @PostMapping
  public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
    Teacher createdTeacher = teacherService.save(teacher);
    return new ResponseEntity<>(createdTeacher, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacherDetails) {
    Teacher updatedTeacher = teacherService.update(id, teacherDetails);
    return ResponseEntity.ok(updatedTeacher);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
    teacherService.delete(id);
    return ResponseEntity.noContent().build();
  }
}