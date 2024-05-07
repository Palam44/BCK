package com.example.bck.service;

import com.example.bck.dto.TeacherDTO;
import com.example.bck.model.Teacher;
import com.example.bck.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {

  private final TeacherRepository teacherRepository;

  @Autowired
  public TeacherService(TeacherRepository teacherRepository) {
    this.teacherRepository = teacherRepository;
  }

  public List<TeacherDTO> findAll() {
    return teacherRepository.findAll().stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public TeacherDTO findById(Long id) {
    Teacher teacher = teacherRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Teacher not found with id " + id));
    return convertToDTO(teacher);
  }

  public TeacherDTO save(TeacherDTO teacherDTO) {
    Teacher teacher = convertToEntity(teacherDTO);
    Teacher savedTeacher = teacherRepository.save(teacher);
    return convertToDTO(savedTeacher);
  }

  public TeacherDTO update(Long id, TeacherDTO teacherDTO) {
    Teacher teacher = teacherRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Teacher not found with id " + id));
    teacher.setFirstName(teacherDTO.getFirstName());
    teacher.setLastName(teacherDTO.getLastName());
    Teacher updatedTeacher = teacherRepository.save(teacher);
    return convertToDTO(updatedTeacher);
  }

  public void delete(Long id) {
    Teacher teacher = teacherRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Teacher not found with id " + id));
    teacherRepository.delete(teacher);
  }

  private TeacherDTO convertToDTO(Teacher teacher) {
    TeacherDTO dto = new TeacherDTO();
    dto.setId(teacher.getId());
    dto.setFirstName(teacher.getFirstName());
    dto.setLastName(teacher.getLastName());
    return dto;
  }

  private Teacher convertToEntity(TeacherDTO dto) {
    Teacher teacher = new Teacher();
    if (dto.getId() != null) {
      teacher.setId(dto.getId());
    }
    teacher.setFirstName(dto.getFirstName());
    teacher.setLastName(dto.getLastName());
    return teacher;
  }
}