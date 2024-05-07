package com.example.bck.controller;

import com.example.bck.model.Group;
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
  public ResponseEntity<List<Group>> getAllGroups() {
    List<Group> groups = groupService.findAll();
    return ResponseEntity.ok(groups);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
    Group group = groupService.findById(id);
    return ResponseEntity.ok(group);
  }

  @PostMapping
  public ResponseEntity<Group> createGroup(@RequestBody Group group) {
    Group createdGroup = groupService.save(group);
    return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody Group groupDetails) {
    Group updatedGroup = groupService.update(id, groupDetails);
    return ResponseEntity.ok(updatedGroup);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
    groupService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
