package com.se.rqmservice.controller;

import com.se.rqmservice.model.DDocument;
import com.se.rqmservice.service.DokumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rqm")
@CrossOrigin
public class DocumentController {

    @Autowired
    DokumentService documentService;

    @PostMapping("/save")
    public DDocument saveDocument(@Valid @RequestBody DDocument document){
        return documentService.saveDocument(document);
    }
    @PostMapping("/saveGenerated")
    public DDocument saveGenerated(@Valid @RequestBody DDocument document){
        return documentService.saveGenerated(document);
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



}
