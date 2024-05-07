package com.example.bck.service;

import com.example.bck.dto.DisciplineDTO;
import com.example.bck.model.Discipline;
import com.example.bck.repository.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisciplineService {

  private final DisciplineRepository disciplineRepository;

  @Autowired
  public DisciplineService(DisciplineRepository disciplineRepository) {
    this.disciplineRepository = disciplineRepository;
  }

  public List<DisciplineDTO> findAll() {
    return disciplineRepository.findAll().stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public DisciplineDTO findById(Long id) {
    Discipline discipline = disciplineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Discipline not found with id " + id));
    return convertToDTO(discipline);
  }

  public DisciplineDTO save(DisciplineDTO disciplineDTO) {
    Discipline discipline = convertToEntity(disciplineDTO);
    Discipline savedDiscipline = disciplineRepository.save(discipline);
    return convertToDTO(savedDiscipline);
  }

  public DisciplineDTO update(Long id, DisciplineDTO disciplineDTO) {
    Discipline discipline = disciplineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Discipline not found with id " + id));
    discipline.setName(disciplineDTO.getName());
    Discipline updatedDiscipline = disciplineRepository.save(discipline);
    return convertToDTO(updatedDiscipline);
  }

  public void delete(Long id) {
    Discipline discipline = disciplineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Discipline not found with id " + id));
    disciplineRepository.delete(discipline);
  }

  private DisciplineDTO convertToDTO(Discipline discipline) {
    DisciplineDTO dto = new DisciplineDTO();
    dto.setId(discipline.getId());
    dto.setName(discipline.getName());
    return dto;
  }

  private Discipline convertToEntity(DisciplineDTO dto) {
    Discipline discipline = new Discipline();
    discipline.setId(dto.getId());
    discipline.setName(dto.getName());
    return discipline;
  }
}