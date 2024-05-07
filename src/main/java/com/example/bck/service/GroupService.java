package com.example.bck.service;

import com.example.bck.dto.GroupDTO;
import com.example.bck.model.Group;
import com.example.bck.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

  private final GroupRepository groupRepository;

  @Autowired
  public GroupService(GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
  }

  public List<GroupDTO> findAll() {
    return groupRepository.findAll().stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  public GroupDTO findById(Long id) {
    Group group = groupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Group not found with id " + id));
    return convertToDTO(group);
  }

  public GroupDTO save(GroupDTO groupDTO) {
    Group group = convertToEntity(groupDTO);
    Group savedGroup = groupRepository.save(group);
    return convertToDTO(savedGroup);
  }

  public GroupDTO update(Long id, GroupDTO groupDTO) {
    Group group = groupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Group not found with id " + id));
    group.setName(groupDTO.getName());
    Group updatedGroup = groupRepository.save(group);
    return convertToDTO(updatedGroup);
  }

  public void delete(Long id) {
    Group group = groupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Group not found with id " + id));
    groupRepository.delete(group);
  }

  private GroupDTO convertToDTO(Group group) {
    GroupDTO dto = new GroupDTO();
    dto.setId(group.getId());
    dto.setName(group.getName());
    return dto;
  }

  private Group convertToEntity(GroupDTO dto) {
    Group group = new Group();
    if (dto.getId() != null) {
      group.setId(dto.getId());
    }
    group.setName(dto.getName());
    return group;
  }
}