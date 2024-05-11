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
import java.time.Duration;
import java.time.LocalTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonService {

  private final LessonRepository lessonRepository;
  private GroupRepository groupRepository;
  private final DisciplineRepository disciplineRepository;
  private final TeacherRepository teacherRepository;
  private static final Duration BREAK_DURATION = Duration.ofMinutes(10);

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
    Lesson savedLesson = lessonRepository.save(lesson);
    return convertToDTO(savedLesson);
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
    return convertToDTO(updatedLesson);
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


  public LessonDTO scheduleLesson(LessonDTO lessonDTO) {
    LocalTime proposedStartTime = lessonDTO.getTime();
    Duration lessonDuration = Duration.ofMinutes(lessonDTO.getDuration());
    LocalTime proposedEndTime = proposedStartTime.plus(lessonDuration);

    // Проверяем, что занятие и перерыв после него не пересекаются с другими занятиями
    if (isTimeSlotAvailable(proposedStartTime, proposedEndTime, lessonDTO.getGroup().getId(),
        lessonDTO.getDayOfWeek())) {
      // Назначаем время начала занятия с учетом перерыва
      Lesson lesson = convertToEntity(lessonDTO);
      lesson.setTime(proposedStartTime);
      Lesson savedLesson = lessonRepository.save(lesson);
      return convertToDTO(savedLesson);
    } else {
      throw new RuntimeException(
          "The time slot from " + proposedStartTime + " to " + proposedEndTime.plus(BREAK_DURATION)
              + " is not available.");
    }
  }

  private boolean isTimeSlotAvailable(LocalTime startTime, LocalTime endTime, Long groupId,
      DayOfWeek dayOfWeek) {
    // Получаем все занятия для группы в этот день недели
    List<Lesson> lessons = lessonRepository.findByGroupAndDayOfWeekOrderByTime(groupId, dayOfWeek);

    // Проверяем, что новое занятие не пересекается с существующими занятиями и перерывами
    for (Lesson existingLesson : lessons) {
      LocalTime existingLessonStartTime = existingLesson.getTime();
      LocalTime existingLessonEndTime = existingLessonStartTime.plusMinutes(
          existingLesson.getDuration());

      // Проверяем пересечение с существующим занятием и перерывом после него
      if (!startTime.isAfter(existingLessonEndTime.plus(BREAK_DURATION)) && !endTime.plus(
          BREAK_DURATION).isBefore(existingLessonStartTime)) {
        return false;
      }
    }
    return true;
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
    lesson.setDuration(dto.getDuration());
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