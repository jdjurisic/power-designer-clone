package com.se.ucservice.controller;

import com.se.ucservice.model.usecase.DDocument;
import com.se.ucservice.rules.Rules;
import com.se.ucservice.service.TransformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/rqm2uc")
@CrossOrigin
public class TransformController {

    @Autowired
    TransformService transformService;


    @PostMapping("/transform")
    public DDocument saveDocument(@Valid @RequestBody com.se.ucservice.model.rqm.DDocument document){
        return transformService.transformDocument(document);
    }

}
