package com.broker.prototype.controller;


import com.broker.prototype.entities.SService;
import com.broker.prototype.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService service;

    @PostMapping(value = "/save")
    public ResponseEntity<SService> save(@Valid @RequestBody SService sService){
        return new ResponseEntity<>(service.save(sService), HttpStatus.CREATED);
    }

    @GetMapping("/get/{serviceName}")
    public ResponseEntity<SService> findByName( @PathVariable String serviceName){
        return new ResponseEntity<>(service.findByName(serviceName), HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<SService> findAll(){
        return service.findAll();
    }

}
