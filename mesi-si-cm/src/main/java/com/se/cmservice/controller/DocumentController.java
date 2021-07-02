package com.se.cmservice.controller;

import com.se.cmservice.model.DDocument;
import com.se.cmservice.rules.Rules;
import com.se.cmservice.service.DokumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/class")
@CrossOrigin
public class DocumentController {

    @Autowired
    DokumentService documentService;

    @PostMapping("/save")
    public DDocument saveDocument(@Valid @RequestBody DDocument document){
        return documentService.saveDocument(document);
    }

//    @PostMapping("/merge")
//    public DDocument mergeDocument(@Valid @RequestBody List<DDocument> documents){
//        return documentService.conflictAlgorithm(documents.get(0), documents.get(1));
//    }

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
