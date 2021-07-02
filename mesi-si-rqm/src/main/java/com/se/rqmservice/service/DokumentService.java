package com.se.rqmservice.service;

import com.se.rqmservice.exception.ConflictException;
import com.se.rqmservice.model.DDocument;
import com.se.rqmservice.model.Rqm;
import com.se.rqmservice.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DokumentService {

    @Autowired
    DocumentRepository documentRepository;


    public DDocument saveDocument(DDocument document){


        if(document.getPreviousVersionId()==null){
            document.setPreviousVersionId("");
            return documentRepository.save(document);
        }

        String currentDocId = document.getId();
        String currentDocPreviousId = document.getPreviousVersionId();
        DDocument databaseDocument = new DDocument();
        // DDocument previousDocument = new DDocument();

        String databaseCurrentDocPreviousId = "";
        if( documentRepository.findById(currentDocId).isPresent() &&
                documentRepository.findById(currentDocPreviousId).isPresent()) {
            databaseCurrentDocPreviousId = documentRepository.findById(currentDocId).get().getPreviousVersionId();
            databaseDocument = documentRepository.findById(currentDocId).get();
            //previousDocument = documentRepository.findById(currentDocPreviousId).get();

        }
        System.out.println("currentDocId:                 " + document.getId());
        System.out.println("currentDocPreviousId:         " + document.getPreviousVersionId());
        System.out.println("databaseCurrentDocPreviousId: " + databaseCurrentDocPreviousId);


        if(currentDocPreviousId.equals(databaseCurrentDocPreviousId) || currentDocPreviousId.equals("")) {
            System.out.println("Sve je okej.");
        }
        else {
            System.out.println("Dolazi do konflikta.");
            conflictAlgorithm(document, databaseDocument);
            //conflictAlgorithm(document, databaseDocument, previousDocument);
        }

        document.setPreviousVersionId(savePreviousDocument(document));

        return documentRepository.save(document);

    }
    //public DDocument conflictAlgorithm(DDocument firstDocument, DDocument secondDocument) {
    public void conflictAlgorithm(DDocument firstDocument, DDocument secondDocument) {
        // public void conflictAlgorithm(DDocument firstDocument, DDocument secondDocument, DDocument oldDocument) {
        DDocument mergedDocument = new DDocument();

//        List<Rqm> mergedRqms = new ArrayList<>(firstDocument.getRqms());

        // Imas 2 liste rqm'ova. firstDocument.getRqms i secondDocument.getRqms.
        // FirstDocument dokument koji si poslednji poslala.
        // SecondDocument je dokument koji je vec na bazi sa istim ID.

        // Treba da prodje kroz sve RQMove i da ih sve lepo zapise u mergedRqms.
//        for(Rqm mergedRqm: secondDocument.getRqms())
//            for (Rqm innerMergedRqm : mergedRqm.getRqms())
//                if (!(mergedRqm.getRqms().contains(innerMergedRqm)))
//                    mergedRqm.getRqms().add(innerMergedRqm);


        Set<Rqm> mergedRqms = new LinkedHashSet<>();
        mergedRqms.addAll(secondDocument.getRqms());
        System.out.println("merged rqms: " + mergedRqms);
        for(int i = 0; i < firstDocument.getRqms().size(); i++){
            for (int j = 0; j < firstDocument.getRqms().get(j).getRqms().size(); j++){
                if (!mergedRqms.contains(firstDocument.getRqms().get(j))){
                    mergedRqms.add(firstDocument.getRqms().get(j));
                }
            }
            if (!mergedRqms.contains(firstDocument.getRqms().get(i))){
                mergedRqms.add(secondDocument.getRqms().get(i));
            }
        }

        List<Rqm> merged = new ArrayList<>(mergedRqms);

        mergedDocument.setRqms(merged);

        mergedDocument.setLastModifiedBy(firstDocument.getLastModifiedBy());

        mergedDocument.setId(firstDocument.getId());
        mergedDocument.setPreviousVersionId(secondDocument.getPreviousVersionId());
        mergedDocument.setNextVersionId(firstDocument.getNextVersionId());

        throw new ConflictException(mergedDocument);
    }
    private String savePreviousDocument(DDocument document) {
        Optional<DDocument> oldDoc;
        DDocument newOldDoc = new DDocument();

        oldDoc = documentRepository.findById(document.getId());

        newOldDoc.setNextVersionId(document.getId());
        oldDoc.ifPresent(dDocument -> newOldDoc.setRqms(dDocument.getRqms()));
        oldDoc.ifPresent(dDocument -> newOldDoc.setPreviousVersionId(dDocument.getPreviousVersionId()));
        oldDoc.ifPresent(dDocument -> newOldDoc.setLastModifiedBy(document.getLastModifiedBy()));

        System.out.println(newOldDoc);

        return documentRepository.save(newOldDoc).getId();
    }

    public DDocument saveGenerated(DDocument document){
        return documentRepository.save(document);
    }

    public List<DDocument> retrieveAllDocuments(){
        return documentRepository.findAll();
    }

    public Optional<DDocument> getDocumentByID(String id){
        return documentRepository.findById(id);
    }

    public boolean deleteById(String id) {
        return documentRepository.deleteDocumentById(id) > 0;
    }

}
