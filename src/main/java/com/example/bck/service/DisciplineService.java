package com.example.bck.service;

import com.example.bck.model.Discipline;
import com.example.bck.repository.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineService {

  private final DisciplineRepository disciplineRepository;

  @Autowired
  public DisciplineService(DisciplineRepository disciplineRepository) {
    this.disciplineRepository = disciplineRepository;
  }

  public List<Discipline> findAll() {
    return disciplineRepository.findAll();
  }

  public Discipline findById(Long id) {
    return disciplineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Discipline not found with id " + id));
  }

  public Discipline save(Discipline discipline) {
    return disciplineRepository.save(discipline);
  }

  public Discipline update(Long id, Discipline disciplineDetails) {
    Discipline discipline = disciplineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Discipline not found with id " + id));

    discipline.setName(disciplineDetails.getName());

    return disciplineRepository.save(discipline);
  }

  public void delete(Long id) {
    Discipline discipline = findById(id); // Reuse findById to handle 'not found' case
    disciplineRepository.delete(discipline);
  }
}