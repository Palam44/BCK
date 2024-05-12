package com.example.bck.mapper;

import com.example.bck.dto.DisciplineDTO;
import com.example.bck.model.Discipline;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DisciplineMapper {
  DisciplineDTO disciplineToDisciplineDTO(Discipline discipline);
  Discipline disciplineDTOToDiscipline(DisciplineDTO disciplineDTO);
}
