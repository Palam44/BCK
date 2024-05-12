package com.example.bck.mapper;

import com.example.bck.dto.TeacherDTO;
import com.example.bck.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

  TeacherDTO teacherToTeacherDTO(Teacher teacher);

  Teacher teacherDTOToTeacher(TeacherDTO teacherDTO);

  void updateTeacherFromDTO(TeacherDTO dto, @MappingTarget Teacher entity);
}