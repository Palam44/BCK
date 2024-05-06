package com.example.bck.service;

import com.example.bck.model.Teacher;
import com.example.bck.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
  private final TeacherRepository teacherRepository;

  public TeacherService(TeacherRepository teacherRepository) {
    this.teacherRepository = teacherRepository;
  }

  public List<Teacher> findAll() {
    return teacherRepository.findAll();
  }

}
