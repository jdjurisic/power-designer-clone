package com.utp.prototype.controller;


import com.utp.prototype.entities.Project;
import com.utp.prototype.service.MyUserDetailsService;
import com.utp.prototype.service.ProjectService;
import com.utp.prototype.service.ModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("project")
@CrossOrigin
public class ProjectController {

    private final ProjectService service;
    private final ModelService modelService;
    private final MyUserDetailsService myUserDetailsService;

    public ProjectController(ProjectService service, ModelService modelService, MyUserDetailsService myUserDetailsService){
        this.service = service;
        this.modelService = modelService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> save(@Valid @RequestBody Project project){
        return new ResponseEntity<>(service.save(project), HttpStatus.CREATED);
    }
    @PutMapping("/update/{projectId}")
    public ResponseEntity<Project> update(@PathVariable Long projectId, @Valid @RequestBody Project project){
        if(service.findById(projectId)==null)
            return null;
        return new ResponseEntity<>(service.update(projectId, project), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<?> deleteById(@PathVariable Long projectId){
        service.deleteById(projectId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get/{projectId}")
    public ResponseEntity<Project> findById(@PathVariable Long projectId){
        return new ResponseEntity<>(service.findById(projectId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<Project> findAll(){     //(@RequestHeader("Authorization") String authorization){
        return service.findAll();
    }

    @PostMapping("/addModelToProject/{modelId}/{projectId}")
    public ResponseEntity<?> addModelToProject(@PathVariable Long modelId, @PathVariable Long projectId){
        if(service.findById(projectId)==null || modelService.findById(modelId)==null)
            return null;
        service.addModelToProject(modelId, projectId);
        return new ResponseEntity<>(HttpStatus.OK);
        //return new ResponseEntity<>(service.addUserToTeam(userId,teamId),HttpStatus.CREATED);
    }

}
