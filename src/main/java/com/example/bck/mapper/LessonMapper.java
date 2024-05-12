package com.example.bck.mapper;

import com.example.bck.dto.LessonDTO;
import com.example.bck.model.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LessonMapper {
  LessonDTO lessonToLessonDTO(Lesson lesson);
  Lesson lessonDTOToLesson(LessonDTO lessonDTO);

  void updateLessonFromDTO(LessonDTO dto, @MappingTarget Lesson entity);
}
