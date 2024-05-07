package com.example.bck.controller;

import com.example.bck.dto.GroupDTO;
import com.example.bck.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

  private final GroupService groupService;

  @Autowired
  public GroupController(GroupService groupService) {
    this.groupService = groupService;
  }

  @GetMapping
  public ResponseEntity<List<GroupDTO>> getAllGroups() {
    List<GroupDTO> groups = groupService.findAll();
    return ResponseEntity.ok(groups);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GroupDTO> getGroupById(@PathVariable Long id) {
    GroupDTO group = groupService.findById(id);
    return ResponseEntity.ok(group);
  }

  @PostMapping
  public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO) {
    GroupDTO createdGroup = groupService.save(groupDTO);
    return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<GroupDTO> updateGroup(@PathVariable Long id, @RequestBody GroupDTO groupDetails) {
    GroupDTO updatedGroup = groupService.update(id, groupDetails);
    return ResponseEntity.ok(updatedGroup);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
    groupService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
