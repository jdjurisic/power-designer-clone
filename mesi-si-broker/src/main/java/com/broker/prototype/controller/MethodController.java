package com.broker.prototype.controller;


import com.broker.prototype.entities.Method;
import com.broker.prototype.service.MethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("method")
@RequiredArgsConstructor
public class MethodController {

    private final MethodService service;

    @PostMapping(value = "/save")
    public ResponseEntity<Method> save(@Valid @RequestBody Method method){
        return new ResponseEntity<>(service.save(method), HttpStatus.CREATED);
    }

    @GetMapping("/get/{methodName}")
    public ResponseEntity<Method> findByName( @PathVariable String methodName){
        return new ResponseEntity<>(service.findByName(methodName), HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<Method> findAll(){
        return service.findAll();
    }
}
