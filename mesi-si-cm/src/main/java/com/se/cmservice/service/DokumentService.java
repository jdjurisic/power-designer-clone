package com.se.cmservice.service;

import com.se.cmservice.exception.ConflictException;
import com.se.cmservice.exception.NasException;
import com.se.cmservice.model.DDocument;
import com.se.cmservice.model.LinkData;
import com.se.cmservice.model.ModelData;
import com.se.cmservice.model.NodeData;
import com.se.cmservice.repository.DocumentRepository;
import com.se.cmservice.rules.Rules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DokumentService {

    @Autowired
    DocumentRepository documentRepository;

    String validateUrl = "http://localhost:8086/validate/class";
    String helpUrl = "http://localhost:8086/validate/help/class";

    List<String> restResponse;
    Rules helpResponse;


    public DDocument saveDocument(DDocument document){
        if(document.getNodeDataArray()==null) {
            document.setPreviousVersionId("");
            return documentRepository.save(document);
        }

        if(validate(document)){

            if(document.getPreviousVersionId()==null){
                document.setPreviousVersionId("");
                return documentRepository.save(document);
            }


            String currentDocId = document.getId();
            String currentDocPreviousId = document.getPreviousVersionId();
            DDocument databaseDocument = new DDocument();
            DDocument previousDocument = new DDocument();

            String databaseCurrentDocPreviousId = "";
            if( documentRepository.findById(currentDocId).isPresent() &&
                    documentRepository.findById(currentDocPreviousId).isPresent()) {
                databaseCurrentDocPreviousId = documentRepository.findById(currentDocId).get().getPreviousVersionId();
                databaseDocument = documentRepository.findById(currentDocId).get();
                previousDocument = documentRepository.findById(currentDocPreviousId).get();

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
        else
            throw new NasException(restResponse.toString());
    }

    //public DDocument conflictAlgorithm(DDocument firstDocument, DDocument secondDocument) {
    public void conflictAlgorithm(DDocument firstDocument, DDocument secondDocument) {
   // public void conflictAlgorithm(DDocument firstDocument, DDocument secondDocument, DDocument oldDocument) {
        DDocument mergedDocument = new DDocument();

        List<NodeData> mergedNodeData = new ArrayList<>(firstDocument.getNodeDataArray());
        List<LinkData> mergedLinkData = new ArrayList<>(firstDocument.getLinkDataArray());

        for(NodeData mergedNode: secondDocument.getNodeDataArray() ) {
           if(!(mergedNodeData.contains(mergedNode))) {
                mergedNode.setLoc("");
                mergedNodeData.add(mergedNode);
           }
        }
        for(LinkData mergedLink: secondDocument.getLinkDataArray() ) {
            if(!(mergedLinkData.contains(mergedLink)))
                mergedLinkData.add(mergedLink);
        }

        mergedDocument.setId(firstDocument.getId());
        mergedDocument.setLinkKeyProperty(firstDocument.getLinkKeyProperty());
        mergedDocument.setLinkFromPortIdProperty(firstDocument.getLinkFromPortIdProperty());
        mergedDocument.setLinkToPortIdProperty(firstDocument.getLinkToPortIdProperty());
        mergedDocument.setModelData(firstDocument.getModelData());

        mergedDocument.setNodeDataArray(mergedNodeData);
        mergedDocument.setLinkDataArray(mergedLinkData);

        mergedDocument.setPreviousVersionId(secondDocument.getPreviousVersionId());


        mergedDocument.setNextVersionId(firstDocument.getNextVersionId());
        mergedDocument.setLastModifiedBy(firstDocument.getLastModifiedBy());

//         return mergedDocument;
        throw new ConflictException(mergedDocument);
    }

    private String savePreviousDocument(DDocument document) {
        Optional<DDocument> oldDoc;
        DDocument newOldDoc = new DDocument();

        oldDoc = documentRepository.findById(document.getId());

        newOldDoc.setNextVersionId(document.getId());
        oldDoc.ifPresent(dDocument -> newOldDoc.setLinkDataArray(dDocument.getLinkDataArray()));
        oldDoc.ifPresent(dDocument -> newOldDoc.setNodeDataArray(dDocument.getNodeDataArray()));
        oldDoc.ifPresent(dDocument -> newOldDoc.setModelData(dDocument.getModelData()));
        oldDoc.ifPresent(dDocument -> newOldDoc.setLinkKeyProperty(dDocument.getLinkKeyProperty()));
        oldDoc.ifPresent(dDocument -> newOldDoc.setLinkFromPortIdProperty(dDocument.getLinkFromPortIdProperty()));
        oldDoc.ifPresent(dDocument -> newOldDoc.setLinkToPortIdProperty(dDocument.getLinkToPortIdProperty()));
        oldDoc.ifPresent(dDocument -> newOldDoc.setPreviousVersionId(dDocument.getPreviousVersionId()));
        oldDoc.ifPresent(dDocument -> newOldDoc.setLastModifiedBy(document.getLastModifiedBy()));

        System.out.println(newOldDoc);

        return documentRepository.save(newOldDoc).getId();
    }

    private boolean validate(DDocument document) {

        RestTemplate restTemplate = new RestTemplate();

       restResponse = restTemplate.postForObject(validateUrl, new HttpEntity<>(document), List.class);
        //stringRestResponse = restTemplate.postForObject(validateUrl, new HttpEntity<>(document), String.class);
//
       assert restResponse != null;

        System.out.println("RestResponse: " + restResponse);
       return restResponse.isEmpty();

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

    public Rules getHelp() {
        RestTemplate getHelp = new RestTemplate();

        helpResponse = getHelp.getForObject(helpUrl, Rules.class);

        return helpResponse;
    }
}
