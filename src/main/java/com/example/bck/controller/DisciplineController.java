package com.example.bck.controller;

import com.example.bck.model.Discipline;
import com.example.bck.service.DisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplines")
public class DisciplineController {

  private final DisciplineService disciplineService;

  @Autowired
  public DisciplineController(DisciplineService disciplineService) {
    this.disciplineService = disciplineService;
  }

  @GetMapping
  public ResponseEntity<List<Discipline>> getAllDisciplines() {
    List<Discipline> disciplines = disciplineService.findAll();
    return ResponseEntity.ok(disciplines);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Discipline> getDisciplineById(@PathVariable Long id) {
    Discipline discipline = disciplineService.findById(id);
    return ResponseEntity.ok(discipline);
  }

  @PostMapping
  public ResponseEntity<Discipline> createDiscipline(@RequestBody Discipline discipline) {
    Discipline createdDiscipline = disciplineService.save(discipline);
    return new ResponseEntity<>(createdDiscipline, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Discipline> updateDiscipline(@PathVariable Long id, @RequestBody Discipline discipline) {
    Discipline updatedDiscipline = disciplineService.update(id, discipline);
    return ResponseEntity.ok(updatedDiscipline);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDiscipline(@PathVariable Long id) {
    disciplineService.delete(id);
    return ResponseEntity.noContent().build();
  }
}