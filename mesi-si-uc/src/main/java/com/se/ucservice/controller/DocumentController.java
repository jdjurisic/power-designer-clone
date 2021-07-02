package com.se.ucservice.controller;

import com.se.ucservice.model.DDocument;
import com.se.ucservice.rules.Rules;
import com.se.ucservice.service.DokumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usecase")
@CrossOrigin
public class DocumentController {

    @Autowired
    DokumentService documentService;

    @PostMapping("/save")
    public DDocument saveDocument(@Valid @RequestBody DDocument document){
        return documentService.saveDocument(document);
    }

    @PostMapping("/saveGeneratedFile")
    public DDocument saveGeneratedFile(@Valid @RequestBody DDocument document){
        return documentService.saveGeneratedFile(document);
    }

    @GetMapping("/all")
    public List<DDocument> getDocument(){
        return documentService.retrieveAllDocuments();
    }

    @GetMapping("/get/{documentId}")
    public Optional<DDocument> getDocumentById(@PathVariable String documentId){
        return documentService.getDocumentByID(documentId);
    }

    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> deleteById(@PathVariable String Id){
        if(documentService.deleteById(Id)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/help")
    public Rules getHelp(){
        return documentService.getHelp();
    }

}
