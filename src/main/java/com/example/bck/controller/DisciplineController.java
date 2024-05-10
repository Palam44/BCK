package com.example.bck.controller;

import com.example.bck.dto.DisciplineDTO;
import com.example.bck.service.DisciplineService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplines")
@Validated
public class DisciplineController {

  private final DisciplineService disciplineService;

  @Autowired
  public DisciplineController(DisciplineService disciplineService) {
    this.disciplineService = disciplineService;
  }

  @GetMapping
  public ResponseEntity<List<DisciplineDTO>> getAllDisciplines() {
    List<DisciplineDTO> disciplines = disciplineService.findAll();
    return ResponseEntity.ok(disciplines);
  }

  @GetMapping("/{id}")
  public ResponseEntity<DisciplineDTO> getDisciplineById(@PathVariable Long id) {
    DisciplineDTO discipline = disciplineService.findById(id);
    return ResponseEntity.ok(discipline);
  }

  @PostMapping
  public ResponseEntity<DisciplineDTO> createDiscipline(@Valid @RequestBody DisciplineDTO disciplineDTO) {
    DisciplineDTO createdDiscipline = disciplineService.save(disciplineDTO);
    return new ResponseEntity<>(createdDiscipline, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<DisciplineDTO> updateDiscipline(@PathVariable Long id, @RequestBody DisciplineDTO disciplineDTO) {
    DisciplineDTO updatedDiscipline = disciplineService.update(id, disciplineDTO);
    return ResponseEntity.ok(updatedDiscipline);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDiscipline(@PathVariable Long id) {
    disciplineService.delete(id);
    return ResponseEntity.noContent().build();
  }
}