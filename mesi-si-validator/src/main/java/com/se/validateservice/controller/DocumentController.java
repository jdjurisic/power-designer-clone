package com.se.validateservice.controller;

import com.se.validateservice.model.classmodel.DDocument;
import com.se.validateservice.model.usecase.DocumentUseCaseModel;
import com.se.validateservice.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/validate")
@CrossOrigin
public class DocumentController {

    @Autowired
    ValidationService validationService;

    @PostMapping("/usecase")
    public ResponseEntity<?> validateUseCase(@Valid @RequestBody DocumentUseCaseModel documentUseCaseModel){
        return validationService.validateUseCaseModel(documentUseCaseModel);
    }

    @PostMapping("/class")
    public ResponseEntity<?> validateClassModel(@Valid @RequestBody DDocument document){
        return validationService.validateClassModel(document);
    }

    @GetMapping("/help/usecase")
    public ResponseEntity<?> showUseCaseHelp(){
        return validationService.getUseCaseHelp();
    }

    @GetMapping("/help/class")
    public ResponseEntity<?> showClassModelHelp(){
        return validationService.getClassModelHelp();
    }
}
