package com.example.bck.mapper;

import com.example.bck.dto.GroupDTO;
import com.example.bck.model.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {
  GroupDTO groupToGroupDTO(Group group);
  Group groupDTOToGroup(GroupDTO groupDTO);
}