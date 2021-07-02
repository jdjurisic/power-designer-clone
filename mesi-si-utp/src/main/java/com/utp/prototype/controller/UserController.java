package com.utp.prototype.controller;


import com.utp.prototype.entities.User;
import com.utp.prototype.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    private final UserService service;


    public UserController(UserService service){
        this.service = service;

    }


    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> save(@Valid @RequestBody User user){
        return new ResponseEntity<>(service.save(user), HttpStatus.CREATED);
    }
    @PutMapping ("/update/{userId}")
    public ResponseEntity<User> update( @PathVariable Long userId, @Valid @RequestBody User user){
        if(service.findById(userId)==null)
            return null;
        return new ResponseEntity<>(service.update(userId, user), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteById(@PathVariable Long userId){
        service.deleteById(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<User> findById( @PathVariable Long userId){
        return new ResponseEntity<>(service.findById(userId), HttpStatus.OK);
    }
    @GetMapping("/getByUsername/{username}")
    public ResponseEntity<User> findByUsername( @PathVariable String username){
        return new ResponseEntity<>(service.findByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<User> findAll(){     //(@RequestHeader("Authorization") String authorization){
        return service.findAll();
    }

    @GetMapping(value = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getTypes(){
        return service.getTypes();

    }



}
