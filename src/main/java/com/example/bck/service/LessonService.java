package com.example.bck.service;

import com.example.bck.dto.DisciplineDTO;
import com.example.bck.dto.GroupDTO;
import com.example.bck.dto.LessonDTO;
import com.example.bck.dto.TeacherDTO;
import com.example.bck.model.Discipline;
import com.example.bck.model.Group;
import com.example.bck.model.Lesson;
import com.example.bck.model.Teacher;
import com.example.bck.repository.DisciplineRepository;
import com.example.bck.repository.GroupRepository;
import com.example.bck.repository.LessonRepository;
import com.example.bck.repository.TeacherRepository;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonService {

  private final LessonRepository lessonRepository;
  private  GroupRepository groupRepository;
  private final DisciplineRepository disciplineRepository;
  private final TeacherRepository teacherRepository;

  @Autowired
  public LessonService(LessonRepository lessonRepository, GroupRepository groupRepository,
      DisciplineRepository disciplineRepository, TeacherRepository teacherRepository) {
    this.lessonRepository = lessonRepository;
    this.groupRepository = groupRepository;
    this.disciplineRepository = disciplineRepository;
    this.teacherRepository = teacherRepository;
  }

  @Autowired
  public void setGroupRepository(GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
  }

  public List<LessonDTO> findAll() {
    return lessonRepository.findAll().stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public LessonDTO findById(Long id) {
    Lesson lesson = lessonRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Lesson not found with id " + id));
    return convertToDTO(lesson);
  }

  public LessonDTO save(LessonDTO lessonDTO) {
    Lesson lesson = convertToEntity(lessonDTO);

    if (lesson.getId() != null && lessonRepository.existsById(lesson.getId())) {
      throw new RuntimeException("Lesson with id " + lesson.getId() + " already exists");
    }

    if (lesson.getGroup() != null && lesson.getGroup().getId() != null) {
      Long groupId = lessonDTO.getGroup().getId();
      Group group = groupRepository.findById(groupId)
          .orElseThrow(() -> new RuntimeException("Group not found with id " + groupId));
      lesson.setGroup(group);
    }

    if (lesson.getDiscipline() != null && lesson.getDiscipline().getId() != null) {
      Long disciplineId = lessonDTO.getDiscipline().getId();
      Discipline discipline = disciplineRepository.findById(disciplineId)
          .orElseThrow(() -> new RuntimeException("Discipline not found with id " + disciplineId));
      lesson.setDiscipline(discipline);
    }

    if (lesson.getTeacher() != null && lesson.getTeacher().getId() != null) {
      Long teacherId = lessonDTO.getTeacher().getId();
      Teacher teacher = teacherRepository.findById(teacherId)
          .orElseThrow(() -> new RuntimeException("Teacher not found with id " + teacherId));
      lesson.setTeacher(teacher);
    }

    lesson.setTime(LocalTime.now());
    Lesson savedLesson = lessonRepository.save(lesson);
    return convertToDTO(savedLesson);
  }

  public LessonDTO update(Long id, LessonDTO lessonDTO) {
    Lesson lesson = lessonRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Lesson not found with id " + id));
    updateLessonFromDTO(lesson, lessonDTO);
    Lesson updatedLesson = lessonRepository.save(lesson);
    return convertToDTO(updatedLesson);
  }

  public boolean delete(Long id) {
    lessonRepository.deleteById(id);
    return true;
  }

  public List<LessonDTO> findLessonsByGroupAndDay(Long groupId, DayOfWeek dayOfWeek) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new RuntimeException("Group not found with id " + groupId));
    return lessonRepository.findByGroupAndDayOfWeekOrderByTime(groupId, dayOfWeek).stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }



  private LessonDTO convertToDTO(Lesson lesson) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(lesson, LessonDTO.class);
  }

  private Lesson convertToEntity(LessonDTO dto) {
    Lesson lesson = new Lesson();
    lesson.setId(dto.getId());
    lesson.setDayOfWeek(dto.getDayOfWeek());
    lesson.setTime(dto.getTime());
    lesson.setDiscipline(convertToDiscipline(dto.getDiscipline()));
    lesson.setTeacher(convertToTeacher(dto.getTeacher()));
    lesson.setGroup(convertToGroup(dto.getGroup()));
    return lesson;
  }

  private Discipline convertToDiscipline(DisciplineDTO dto) {
    Discipline discipline = new Discipline();
    discipline.setId(dto.getId());
    discipline.setName(dto.getName());
    return discipline;
  }

  private Teacher convertToTeacher(TeacherDTO dto) {
    Teacher teacher = new Teacher();
    teacher.setId(dto.getId());
    teacher.setFirstName(dto.getFirstName());
    teacher.setLastName(dto.getLastName());
    return teacher;
  }

  private Group convertToGroup(GroupDTO dto) {
    Group group = new Group();
    group.setId(dto.getId());
    group.setName(dto.getName());
    return group;
  }

  private void updateLessonFromDTO(Lesson lesson, LessonDTO dto) {
    lesson.setDayOfWeek(dto.getDayOfWeek());
    lesson.setTime(dto.getTime());
    lesson.setDiscipline(convertToDiscipline(dto.getDiscipline()));
    lesson.setTeacher(convertToTeacher(dto.getTeacher()));
    lesson.setGroup(convertToGroup(dto.getGroup()));
  }
}