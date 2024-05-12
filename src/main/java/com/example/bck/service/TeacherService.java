package com.example.bck.service;

import com.example.bck.dto.TeacherDTO;
import com.example.bck.mapper.TeacherMapper;
import com.example.bck.model.Teacher;
import com.example.bck.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

  private final TeacherRepository teacherRepository;
  private final TeacherMapper teacherMapper;

  @Autowired
  public TeacherService(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
    this.teacherRepository = teacherRepository;
    this.teacherMapper = teacherMapper;
  }

  public List<TeacherDTO> findAll() {
    return teacherRepository.findAll().stream()
        .map(teacherMapper::teacherToTeacherDTO)
        .toList();
  }

  public TeacherDTO findById(Long id) {
    Teacher teacher = teacherRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Teacher not found with id " + id));
    return teacherMapper.teacherToTeacherDTO(teacher);
  }

  public TeacherDTO save(TeacherDTO teacherDTO) {
    Teacher teacher = teacherMapper.teacherDTOToTeacher(teacherDTO);
    Teacher savedTeacher = teacherRepository.save(teacher);
    return teacherMapper.teacherToTeacherDTO(savedTeacher);
  }

  public TeacherDTO update(Long id, TeacherDTO teacherDTO) {
    Teacher teacher = teacherRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Teacher not found with id " + id));
    teacherMapper.updateTeacherFromDTO(teacherDTO, teacher);
    Teacher updatedTeacher = teacherRepository.save(teacher);
    return teacherMapper.teacherToTeacherDTO(updatedTeacher);
  }

  public void delete(Long id) {
    if (!teacherRepository.existsById(id)) {
      throw new RuntimeException("Teacher not found with id " + id);
    }
    teacherRepository.deleteById(id);
  }
}