package com.sedliarov.learningtable.controller;

import com.sedliarov.learningtable.model.dto.GroupDto;
import com.sedliarov.learningtable.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/groups")
public class GroupController {
    private final GroupService service;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public GroupDto createGroup(@RequestBody GroupDto groupDto) {
        return service.createGroup(groupDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GroupDto> getGroups() {
        return service.getGroups();
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public GroupDto updateGroup(@PathVariable UUID id, @RequestBody GroupDto groupDto) {
        return service.updateGroup(id, groupDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GroupDto getGroupById(@PathVariable UUID id) {
        return service.getGroupById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGroup(@PathVariable UUID id) {
        service.deleteGroup(id);
    }

}
