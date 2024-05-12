package com.example.bck.service;

import com.example.bck.dto.GroupDTO;
import com.example.bck.mapper.GroupMapper;
import com.example.bck.model.Group;
import com.example.bck.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

  private final GroupRepository groupRepository;
  private final GroupMapper groupMapper;

  @Autowired
  public GroupService(GroupRepository groupRepository, GroupMapper groupMapper) {
    this.groupRepository = groupRepository;
    this.groupMapper = groupMapper;
  }

  public List<GroupDTO> findAll() {
    return groupRepository.findAll().stream()
        .map(groupMapper::groupToGroupDTO)
        .toList();
  }

  public GroupDTO findById(Long id) {
    Group group = groupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Group not found with id " + id));
    return groupMapper.groupToGroupDTO(group);
  }

  public GroupDTO save(GroupDTO groupDTO) {
    Group group = groupMapper.groupDTOToGroup(groupDTO);
    Group savedGroup = groupRepository.save(group);
    return groupMapper.groupToGroupDTO(savedGroup);
  }

  public GroupDTO update(Long id, GroupDTO groupDTO) {
    Group group = groupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Group not found with id " + id));
    group.setName(groupDTO.getName());
    Group updatedGroup = groupRepository.save(group);
    return groupMapper.groupToGroupDTO(updatedGroup);
  }

  public void delete(Long id) {
    Group group = groupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Group not found with id " + id));
    groupRepository.delete(group);
  }

}