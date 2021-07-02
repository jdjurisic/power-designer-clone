package com.se.ucservice.service;

import com.se.ucservice.model.rqm.Rqm;
import com.se.ucservice.model.rqm.RqmType;
import com.se.ucservice.model.usecase.DDocument;
import com.se.ucservice.model.usecase.LinkData;
import com.se.ucservice.model.usecase.ModelData;
import com.se.ucservice.model.usecase.NodeData;
import com.se.ucservice.rules.Rules;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransformService {

    ModelData modelData = new ModelData("value","0 0");

    NodeData userActorNode = new NodeData("user", "User","orange", "actor" );
    NodeData adminActorNode = new NodeData("admin", "Admin","orange", "actor" );

    DDocument useCase = new DDocument();

    private int linkUniqueId = 0;
    private int nodeUniqueId = 0;

    List<LinkData> linkDataList;
    List<NodeData> nodeDataList;

    public DDocument transformDocument(com.se.ucservice.model.rqm.DDocument document){
//        linkUniqueId = 0;
//        nodeUniqueId = 0;

        NodeData rqmNodeData;
        NodeData innerRqmNodeData;

        useCase.setLinkKeyProperty("key");
        useCase.setLinkFromPortIdProperty("fromPort");
        useCase.setLinkToPortIdProperty("toPort");

        // Mogli smo da stavimo da je HashSet da ne prima duplikate.
        linkDataList = new ArrayList<>();
        nodeDataList = new ArrayList<>();

        for(Rqm rqm : document.getRqms()){
            if(rqm.getType() == RqmType.FUNCTIONAL) {
                rqmNodeData = transformRQM(rqm);
                nodeDataList.add(rqmNodeData);
                linkDataList.add(linkWithActor(rqmNodeData, rqm));
                if (!rqm.getRqms().isEmpty()) {
                    for (Rqm innerRqm : rqm.getRqms()) {
                        innerRqmNodeData = transformRQM(innerRqm);
                        nodeDataList.add(innerRqmNodeData);//transformRQMFromParent(rqm, innerRqm));
                        linkDataList.add(linkWithUseCase(rqmNodeData, innerRqmNodeData));
                    }
                }
            }
        }
        useCase.setModelData(modelData);
        useCase.setLinkDataArray(linkDataList);
        useCase.setNodeDataArray(nodeDataList);

        return useCase;

//        if(document.getLinkDataArray()==null || document.getNodeDataArray()==null)
//            return ;
//
//        if(validate(document))
//           return documentRepository.save(document);
//       else
//           throw new MojException(restResponse.toString());
    }

    private LinkData linkWithUseCase(NodeData useCaseParent, NodeData useCaseChild) {
        LinkData linkData = new LinkData();
        linkData.setKey(getLinkUniqueId());
        linkData.setFrom(useCaseChild.getKey());
        linkData.setTo(useCaseParent.getKey());
        linkData.setFromPort("b");
        linkData.setToPort("t");
        linkData.setCategory("generalization");

        return linkData;
    }

    private LinkData linkWithActor(NodeData useCase, Rqm rqm) {
        LinkData linkData = new LinkData();
        linkData.setKey(getLinkUniqueId());
        if(rqm.getUsers()!=null && rqm.getUsers().contains("admin")) {
            linkData.setFrom(adminActorNode.getKey());
            if(!nodeDataList.contains(adminActorNode))
                nodeDataList.add(adminActorNode);
        }
        else {
            linkData.setFrom(userActorNode.getKey());
            if(!nodeDataList.contains(userActorNode))
                nodeDataList.add(userActorNode);
        }
        linkData.setTo(useCase.getKey());
        linkData.setFromPort("b");
        linkData.setToPort("t");
        linkData.setCategory("association");

        return linkData;
    }

    private NodeData transformRQM(Rqm rqm) {
        // Create Model Data
        NodeData nodeData = new NodeData();

        nodeData.setKey("UseCase - " + getNodeUniqueId());
        nodeData.setText(rqm.getDescription());
        nodeData.setColor("lightblue");
        nodeData.setCategory("usecase");
        //nodeData.setLoc("0 0"); // Mozda mozemo bez pa da ih sam generise jer je pametan! edit: ne moze.

        return nodeData;
    }

    private synchronized int getLinkUniqueId() {
        return linkUniqueId++;
    }
    private synchronized int getNodeUniqueId() {
        return nodeUniqueId++;
    }
}
