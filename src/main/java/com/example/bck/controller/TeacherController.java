package com.example.bck.controller;

import com.example.bck.model.Teacher;
import com.example.bck.service.TeacherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {
  private final TeacherService teacherService;

  public TeacherController(TeacherService teacherService) {
    this.teacherService = teacherService;
  }

  @GetMapping
  public List<Teacher> getAllTeachers() {
    return teacherService.findAll();
  }

  // Добавьте остальные CRUD-методы
}