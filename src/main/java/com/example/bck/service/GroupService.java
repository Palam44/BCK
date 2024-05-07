package com.example.bck.service;

import com.example.bck.model.Group;
import com.example.bck.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

  private final GroupRepository groupRepository;

  @Autowired
  public GroupService(GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
  }

  public List<Group> findAll() {
    return groupRepository.findAll();
  }

  public Group findById(Long id) {
    return groupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Group not found with id " + id));
  }

  public Group save(Group group) {
    return groupRepository.save(group);
  }

  public Group update(Long id, Group groupDetails) {
    Group group = groupRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Group not found with id " + id));

    group.setName(groupDetails.getName());

    return groupRepository.save(group);
  }

  public void delete(Long id) {
    Group group = findById(id);
    groupRepository.delete(group);
  }
}