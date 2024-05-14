package com.example.bck.service;


import com.example.bck.dto.LessonDTO;
import com.example.bck.mapper.LessonMapper;
import com.example.bck.model.Discipline;
import com.example.bck.model.Group;
import com.example.bck.model.Lesson;
import com.example.bck.model.Teacher;
import com.example.bck.repository.DisciplineRepository;
import com.example.bck.repository.GroupRepository;
import com.example.bck.repository.LessonRepository;
import com.example.bck.repository.TeacherRepository;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

  private final LessonRepository lessonRepository;
  private GroupRepository groupRepository;
  private final DisciplineRepository disciplineRepository;
  private final TeacherRepository teacherRepository;
  private final LessonMapper lessonMapper;
  private static final Duration BREAK_DURATION = Duration.ofMinutes(10);

  @Autowired
  public LessonService(LessonRepository lessonRepository, GroupRepository groupRepository,
      DisciplineRepository disciplineRepository, TeacherRepository teacherRepository,
      LessonMapper lessonMapper) {
    this.lessonRepository = lessonRepository;
    this.groupRepository = groupRepository;
    this.disciplineRepository = disciplineRepository;
    this.teacherRepository = teacherRepository;
    this.lessonMapper = lessonMapper;
  }

  @Autowired
  public void setGroupRepository(GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
  }

  public List<LessonDTO> findAll() {
    return lessonRepository.findAll().stream()
        .map(lessonMapper::lessonToLessonDTO)
        .toList();
  }

  public LessonDTO findById(Long id) {
    Lesson lesson = lessonRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Lesson not found with id " + id));
    return lessonMapper.lessonToLessonDTO(lesson);
  }

  public LessonDTO save(LessonDTO lessonDTO) {
    Lesson lesson = lessonMapper.lessonDTOToLesson(lessonDTO);

    if (lesson.getId() != null && lessonRepository.existsById(lesson.getId())) {
      throw new RuntimeException("Lesson with id " + lesson.getId() + " already exists");
    }

    if (lesson.getGroup() != null && lesson.getGroup().getId() != null) {
      Long groupId = lessonDTO.getGroup().getId();
      Group group = groupRepository.findById(groupId)
          .orElseThrow(() -> new RuntimeException("Group not found with id " + groupId));
      lesson.setGroup(group);
    }

    Discipline discipline = null;
    if (lesson.getDiscipline() != null && lesson.getDiscipline().getId() != null) {
      Long disciplineId = lessonDTO.getDiscipline().getId();
      discipline = disciplineRepository.findById(disciplineId)
          .orElseThrow(() -> new RuntimeException("Discipline not found with id " + disciplineId));
      lesson.setDiscipline(discipline);
    }

    Teacher teacher = null;
    if (lesson.getTeacher() != null && lesson.getTeacher().getId() != null) {
      Long teacherId = lessonDTO.getTeacher().getId();
      teacher = teacherRepository.findById(teacherId)
          .orElseThrow(() -> new RuntimeException("Teacher not found with id " + teacherId));
      lesson.setTeacher(teacher);
    }

    // Проверка, что преподаватель может преподавать дисциплину
    if (discipline != null && !teacher.getDisciplines().contains(discipline)) {
      throw new RuntimeException(
          "Teacher with id " + teacher.getId() + " does not teach the discipline with id "
              + discipline.getId());
    }

    lesson.setTime(LocalTime.now());
    setLessonRelations(lesson, lessonDTO);
    Lesson savedLesson = lessonRepository.save(lesson);
    return lessonMapper.lessonToLessonDTO(savedLesson);
  }

  public LessonDTO assignTeacherToLesson(Long lessonId, Long teacherId, Long disciplineId) {
    Lesson lesson = lessonRepository.findById(lessonId)
        .orElseThrow(() -> new RuntimeException("Lesson not found with id " + lessonId));
    Teacher teacher = teacherRepository.findById(teacherId)
        .orElseThrow(() -> new RuntimeException("Teacher not found with id " + teacherId));
    Discipline discipline = disciplineRepository.findById(disciplineId)
        .orElseThrow(() -> new RuntimeException("Discipline not found with id " + disciplineId));

    // Проверка, что преподаватель может преподавать дисциплину
    if (!teacher.getDisciplines().contains(discipline)) {
      throw new RuntimeException(
          "Teacher with id " + teacher.getId() + " does not teach the discipline with id "
              + discipline.getId());
    }

    lesson.setTeacher(teacher);
    lesson.setDiscipline(discipline);
    Lesson updatedLesson = lessonRepository.save(lesson);
    return lessonMapper.lessonToLessonDTO(updatedLesson);
  }

  public LessonDTO update(Long id, LessonDTO lessonDTO) {
    Lesson lesson = lessonRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Lesson not found with id " + id));
    lessonMapper.updateLessonFromDTO(lessonDTO, lesson);
    setLessonRelations(lesson, lessonDTO);
    Lesson updatedLesson = lessonRepository.save(lesson);
    return lessonMapper.lessonToLessonDTO(updatedLesson);
  }

  public boolean delete(Long id) {
    lessonRepository.deleteById(id);
    return true;
  }

  public List<LessonDTO> findLessonsByGroupAndDay(Long groupId, DayOfWeek dayOfWeek) {
   return lessonRepository.findByGroupAndDayOfWeekOrderByTime(groupId, dayOfWeek).stream()
        .map(lessonMapper::lessonToLessonDTO)
        .toList();
  }

  public List<String> getFormattedLessonsByGroupAndDay(Long groupId, DayOfWeek dayOfWeek) {
    List<LessonDTO> lessonDTOs = findLessonsByGroupAndDay(groupId, dayOfWeek);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    return lessonDTOs.stream()
        .map(lessonDTO -> String.format("%s - %s (%s)",
            lessonDTO.getTime().format(timeFormatter),
            lessonDTO.getDiscipline().getName(),
            lessonDTO.getTeacher().getLastName()))
        .collect(Collectors.toList());
  }


  public LessonDTO scheduleLesson(LessonDTO lessonDTO) {
    LocalTime proposedStartTime = lessonDTO.getTime();
    Duration lessonDuration = Duration.ofMinutes(lessonDTO.getDuration());
    LocalTime proposedEndTime = proposedStartTime.plus(lessonDuration);

    if (isTimeSlotAvailable(proposedStartTime, proposedEndTime, lessonDTO.getGroup().getId(), lessonDTO.getDayOfWeek())) {
      Lesson lesson = lessonMapper.lessonDTOToLesson(lessonDTO);
      setLessonRelations(lesson, lessonDTO);
      lesson.setTime(proposedStartTime);
      Lesson savedLesson = lessonRepository.save(lesson);
      return lessonMapper.lessonToLessonDTO(savedLesson);
    } else {
      throw new RuntimeException("The time slot from " + proposedStartTime + " to " + proposedEndTime.plus(BREAK_DURATION) + " is not available.");
    }
  }

  private boolean isTimeSlotAvailable(LocalTime startTime, LocalTime endTime, Long groupId, DayOfWeek dayOfWeek) {
    List<Lesson> lessons = lessonRepository.findByGroupAndDayOfWeekOrderByTime(groupId, dayOfWeek);
    for (Lesson existingLesson : lessons) {
      LocalTime existingLessonStartTime = existingLesson.getTime();
      LocalTime existingLessonEndTime = existingLessonStartTime.plusMinutes(existingLesson.getDuration());
      if (!startTime.isAfter(existingLessonEndTime.plus(BREAK_DURATION)) && !endTime.plus(BREAK_DURATION).isBefore(existingLessonStartTime)) {
        return false;
      }
    }
    return true;
  }


  private void setLessonRelations(Lesson lesson, LessonDTO lessonDTO) {
    if (lessonDTO.getGroup() != null && lessonDTO.getGroup().getId() != null) {
      Group group = groupRepository.findById(lessonDTO.getGroup().getId())
          .orElseThrow(() -> new RuntimeException("Group not found with id " + lessonDTO.getGroup().getId()));
      lesson.setGroup(group);
    }
    if (lessonDTO.getDiscipline() != null && lessonDTO.getDiscipline().getId() != null) {
      Discipline discipline = disciplineRepository.findById(lessonDTO.getDiscipline().getId())
          .orElseThrow(() -> new RuntimeException("Discipline not found with id " + lessonDTO.getDiscipline().getId()));
      lesson.setDiscipline(discipline);
    }
    if (lessonDTO.getTeacher() != null && lessonDTO.getTeacher().getId() != null) {
      Teacher teacher = teacherRepository.findById(lessonDTO.getTeacher().getId())
          .orElseThrow(() -> new RuntimeException("Teacher not found with id " + lessonDTO.getTeacher().getId()));
      lesson.setTeacher(teacher);
    }
  }


}