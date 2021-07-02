package com.utp.prototype.controller;

import com.utp.prototype.entities.Model;
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
@RequestMapping("model")
@CrossOrigin
public class ModelController {

    private final ModelService service;
    private final ProjectService projectService;
    private final MyUserDetailsService myUserDetailsService;

    public ModelController(ModelService service, ProjectService projectService, MyUserDetailsService myUserDetailsService) {
        this.service = service;
        this.projectService = projectService;
        this.myUserDetailsService = myUserDetailsService;
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> save(@Valid @RequestBody Model model){
        return new ResponseEntity<>(service.save(model), HttpStatus.CREATED);
    }
    @PutMapping("/update/{reqId}")
    public ResponseEntity<Model> update(@PathVariable Long reqId, @Valid @RequestBody Model model){
        if(service.findById(reqId)==null)
            return null;
        return new ResponseEntity<>(service.update(reqId, model), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{reqId}")
    public ResponseEntity<?> deleteById(@PathVariable Long reqId){
        service.deleteById(reqId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get/{reqId}")
    public ResponseEntity<Model> findById(@PathVariable Long reqId){
        return new ResponseEntity<>(service.findById(reqId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<Model> findAll(){     //(@RequestHeader("Authorization") String authorization){
        return service.findAll();
    }


}
