package com.example.bck.service;

import com.example.bck.dto.DisciplineDTO;
import com.example.bck.mapper.DisciplineMapper;
import com.example.bck.model.Discipline;
import com.example.bck.repository.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineService {

  private final DisciplineRepository disciplineRepository;
  private final DisciplineMapper disciplineMapper;

  @Autowired
  public DisciplineService(DisciplineRepository disciplineRepository, DisciplineMapper disciplineMapper) {
    this.disciplineRepository = disciplineRepository;
    this.disciplineMapper = disciplineMapper;

  }

  public List<DisciplineDTO> findAll() {
    return disciplineRepository.findAll().stream()
        .map(disciplineMapper::disciplineToDisciplineDTO)
        .toList();
  }

  public DisciplineDTO findById(Long id) {
    Discipline discipline = disciplineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Discipline not found with id " + id));
    return disciplineMapper.disciplineToDisciplineDTO(discipline);
  }

  public DisciplineDTO save(DisciplineDTO disciplineDTO) {
    Discipline discipline = disciplineMapper.disciplineDTOToDiscipline(disciplineDTO);
    Discipline savedDiscipline = disciplineRepository.save(discipline);
    return disciplineMapper.disciplineToDisciplineDTO(savedDiscipline);
  }

  public DisciplineDTO update(Long id, DisciplineDTO disciplineDTO) {
    Discipline discipline = disciplineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Discipline not found with id " + id));
    discipline.setName(disciplineDTO.getName());
    Discipline updatedDiscipline = disciplineRepository.save(discipline);
    return disciplineMapper.disciplineToDisciplineDTO(updatedDiscipline);
  }

  public void delete(Long id) {
    Discipline discipline = disciplineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Discipline not found with id " + id));
    disciplineRepository.delete(discipline);
  }




}