package com.example.bck.service;

import com.example.bck.model.Teacher;
import com.example.bck.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

  private final TeacherRepository teacherRepository;

  @Autowired
  public TeacherService(TeacherRepository teacherRepository) {
    this.teacherRepository = teacherRepository;
  }

  public List<Teacher> findAll() {
    return teacherRepository.findAll();
  }

  public Teacher findById(Long id) {
    return teacherRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Teacher not found with id " + id));
  }

  public Teacher save(Teacher teacher) {
    return teacherRepository.save(teacher);
  }

  public Teacher update(Long id, Teacher teacherDetails) {
    Teacher teacher = findById(id);
    teacher.setFirstName(teacherDetails.getFirstName());
    teacher.setLastName(teacherDetails.getLastName());
    return teacherRepository.save(teacher);
  }

  public void delete(Long id) {
    Teacher teacher = findById(id);
    teacherRepository.delete(teacher);
  }
}