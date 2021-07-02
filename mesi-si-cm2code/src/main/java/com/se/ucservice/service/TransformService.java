package com.se.ucservice.service;


import com.se.ucservice.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.Param;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class TransformService {

    public ResponseEntity<?> transformDocument(DDocument document){

//        UUID uuid = UUID.randomUUID();
//        System.out.println(uuid);

        for(NodeData clas : document.getNodeDataArray()){
            if(clas.getCategory().equals("class")) classGenerator(document,clas);
            if(clas.getCategory().equals("interface")) interfaceGenerator(document,clas);
            if(clas.getCategory().equals("abstractclass")) abstractClassGenerator(document, clas);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private void interfaceGenerator(DDocument document, NodeData clas) {

        ArrayList<NodeData> extendedInterfaces = new ArrayList<NodeData>();

        // check interface relations
        for(LinkData link : document.getLinkDataArray()) {
            if (link.getFrom().equals(clas.getKey())) {

                NodeData toNode = getNodeByKey(document.getNodeDataArray(), link.getTo());
                if (toNode.getCategory().equals("interface") && link.getCategory().equals("generalization")) {
                    extendedInterfaces.add(toNode);
                }
            }
        }

        StringBuilder interfaceBuilder = new StringBuilder();
        interfaceBuilder.append("package rs.raf.generator;\n\n");
        interfaceBuilder.append("import rs.raf.generator.*;\n\n");
        interfaceBuilder.append("public interface ");
        interfaceBuilder.append(clas.getName());

        if(extendedInterfaces.size() > 0){
            interfaceBuilder.append(" extends ");
            for(NodeData nd : extendedInterfaces){
                interfaceBuilder.append(nd.getName());
                if(extendedInterfaces.size() > 1) interfaceBuilder.append(", ");
            }
            if(extendedInterfaces.size() > 1) interfaceBuilder.deleteCharAt(interfaceBuilder.lastIndexOf(","));
        }

        interfaceBuilder.append(" { \n\n");

        for(Method meth : clas.getMethods()){
            interfaceBuilder.append("\t" + meth.getVisibility()+" ");
            interfaceBuilder.append(meth.getType()+" ");
            interfaceBuilder.append((meth.getName()));
            interfaceBuilder.append("(");
            for(Parameter param : meth.getParameters()){
                interfaceBuilder.append(param.getType() + " " + param.getName());
                if(meth.getParameters().size() > 1) interfaceBuilder.append(", ");
            }
            if(meth.getParameters().size() > 1) interfaceBuilder.deleteCharAt(interfaceBuilder.lastIndexOf(","));
            interfaceBuilder.append(");\n");
        }

        interfaceBuilder.append("\n}\n");
        //System.out.println(interfaceBuilder);

        try {
            FileWriter myWriter = new FileWriter("C:\\Users\\Djordje\\Desktop\\DesignerPower_GeneratedClasses\\"+clas.getName() + ".java");
            myWriter.write(interfaceBuilder.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file " + clas.getName() + ".java\n");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    private void classGenerator(DDocument document, NodeData clas) {

        NodeData inheritedClass = null;
        ArrayList<NodeData> implementedInterfaces = new ArrayList<NodeData>();

        // check class relations
        for(LinkData link : document.getLinkDataArray()) {
            if (link.getFrom().equals(clas.getKey())) {

                NodeData toNode = getNodeByKey(document.getNodeDataArray(), link.getTo());
                if (toNode.getCategory().equals("interface") && link.getCategory().equals("realization")) {
                    implementedInterfaces.add(toNode);
                } else if ((toNode.getCategory().equals("abstractclass") || toNode.getCategory().equals("class"))
                            && link.getCategory().equals("generalization")) {
                    inheritedClass = toNode;
                }
            }
        }

        // check if my implementedInterfaces extend other interfaces - TODO test!
        while(true){
            int recentlyAdded = 0;
            for(NodeData impInt : implementedInterfaces){
                for(LinkData lnkData : document.getLinkDataArray()){
                    if(lnkData.getFrom().equals(impInt.getKey()) && lnkData.getCategory().equals("extends")){
                        NodeData newNode = getNodeByKey(document.getNodeDataArray(),lnkData.getTo());
                        if(!implementedInterfaces.contains(newNode)){
                            implementedInterfaces.add(newNode);
                            recentlyAdded++;
                        }
                    }
                }
            }
            if(recentlyAdded == 0) break;
        }

        StringBuilder sourceBuilder = new StringBuilder();
        sourceBuilder.append("package rs.raf.generator;\n\n");
        sourceBuilder.append("import rs.raf.generator.*;\n\n");
        sourceBuilder.append("public class ");
        sourceBuilder.append(clas.getName());

        if(inheritedClass != null){
            sourceBuilder.append(" extends ");
            sourceBuilder.append(inheritedClass.getName());
        }

        if(implementedInterfaces.size() > 0){
            sourceBuilder.append(" implements ");
            for(NodeData nd : implementedInterfaces){
                sourceBuilder.append(nd.getName());
                if(implementedInterfaces.size() > 1) sourceBuilder.append(", ");
            }
            if(implementedInterfaces.size() > 1) sourceBuilder.deleteCharAt(sourceBuilder.lastIndexOf(","));
        }

        sourceBuilder.append(" { \n\n");

        for(Property prop : clas.getProperties()){
            sourceBuilder.append("\t" + prop.getVisibility() +" ");
            sourceBuilder.append(prop.getType()+" ");
            sourceBuilder.append(prop.getName());
            sourceBuilder.append(";\n");
        }

        sourceBuilder.append("\n");

        for(Method meth : clas.getMethods()){
            sourceBuilder.append("\t" + meth.getVisibility()+" ");
            sourceBuilder.append(meth.getType()+" ");
            sourceBuilder.append((meth.getName()));
            sourceBuilder.append("(");
            for(Parameter param : meth.getParameters()){
                sourceBuilder.append(param.getType() + " " + param.getName());
                if(meth.getParameters().size() > 1) sourceBuilder.append(", ");
            }
            if(meth.getParameters().size() > 1) sourceBuilder.deleteCharAt(sourceBuilder.lastIndexOf(","));
            sourceBuilder.append(") { } \n");
        }

        sourceBuilder.append("\n");

        // getters / setters
        for(Property prop : clas.getProperties()){
            sourceBuilder.append("\tpublic ");
            sourceBuilder.append(prop.getType()+" ");
            if(prop.getType().equals("boolean")) sourceBuilder.append("is"+ StringUtils.capitalize(prop.getName()));
            else sourceBuilder.append("get"+ StringUtils.capitalize(prop.getName()));

            sourceBuilder.append("() {\n\t\treturn " + prop.getName() +";\n\t}\n\n");

            sourceBuilder.append("\tpublic void ");
            sourceBuilder.append("set"+ StringUtils.capitalize(prop.getName()));
            sourceBuilder.append("("+ prop.getType() +" "+ prop.getName() +") {\n\t\tthis." +
                    prop.getName() + " = " + prop.getName() +";\n\t}\n\n");
        }

        // implemented interfaces
        // A class that implements an interface must implement all the methods declared in the interface.
        // The methods must have the exact same signature (name + parameters) as declared in the interface.
        // The class does not need to implement (declare) the variables of an interface. Only the methods.

        if(implementedInterfaces.size() > 0) sourceBuilder.append("\t// Methods from implemented interfaces\n");
        for(NodeData interfaceData : implementedInterfaces){
            for(Method meth : interfaceData.getMethods()){
                sourceBuilder.append("\t" + meth.getVisibility()+" ");
                sourceBuilder.append(meth.getType()+" ");
                sourceBuilder.append((meth.getName()));
                sourceBuilder.append("(");
                for(Parameter param : meth.getParameters()){
                    sourceBuilder.append(param.getType() + " " + param.getName());
                    if(meth.getParameters().size() > 1) sourceBuilder.append(", ");
                }
                if(meth.getParameters().size() > 1) sourceBuilder.deleteCharAt(sourceBuilder.lastIndexOf(","));
                sourceBuilder.append(") { } \n");
            }
        }

        sourceBuilder.append("\n}\n");
        //System.out.println(sourceBuilder);

        try {
            FileWriter myWriter = new FileWriter("C:\\Users\\Djordje\\Desktop\\DesignerPower_GeneratedClasses\\"+clas.getName() + ".java");
            myWriter.write(sourceBuilder.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file " + clas.getName() + ".java\n");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    private void abstractClassGenerator(DDocument document, NodeData clas) {

        NodeData inheritedClass = null;
        ArrayList<NodeData> implementedInterfaces = new ArrayList<NodeData>();

        // check class relations
        for(LinkData link : document.getLinkDataArray()) {
            if (link.getFrom().equals(clas.getKey())) {

                NodeData toNode = getNodeByKey(document.getNodeDataArray(), link.getTo());
                if (toNode.getCategory().equals("interface") && link.getCategory().equals("realization")) {
                    implementedInterfaces.add(toNode);
                } else if ((toNode.getCategory().equals("abstractclass") || toNode.getCategory().equals("class"))
                        && link.getCategory().equals("generalization")) {
                    inheritedClass = toNode;
                }
            }
        }

        // check if my implementedInterfaces extend other interfaces - TODO test!
        while(true){
            int recentlyAdded = 0;
            for(NodeData impInt : implementedInterfaces){
                for(LinkData lnkData : document.getLinkDataArray()){
                    if(lnkData.getFrom().equals(impInt.getKey()) && lnkData.getCategory().equals("extends")){
                        NodeData newNode = getNodeByKey(document.getNodeDataArray(),lnkData.getTo());
                        if(!implementedInterfaces.contains(newNode)){
                            implementedInterfaces.add(newNode);
                            recentlyAdded++;
                        }
                    }
                }
            }
            if(recentlyAdded == 0) break;
        }

        StringBuilder sourceBuilder = new StringBuilder();
        sourceBuilder.append("package rs.raf.generator;\n\n");
        sourceBuilder.append("import rs.raf.generator.*;\n\n");
        sourceBuilder.append("public abstract class ");
        sourceBuilder.append(clas.getName());

        if(inheritedClass != null){
            sourceBuilder.append(" extends ");
            sourceBuilder.append(inheritedClass.getName());
        }

        if(implementedInterfaces.size() > 0){
            sourceBuilder.append(" implements ");
            for(NodeData nd : implementedInterfaces){
                sourceBuilder.append(nd.getName());
                if(implementedInterfaces.size() > 1) sourceBuilder.append(", ");
            }
            if(implementedInterfaces.size() > 1) sourceBuilder.deleteCharAt(sourceBuilder.lastIndexOf(","));
        }

        sourceBuilder.append(" { \n\n");

        for(Property prop : clas.getProperties()){
            sourceBuilder.append("\t" + prop.getVisibility() +" ");
            sourceBuilder.append(prop.getType()+" ");
            sourceBuilder.append(prop.getName());
            sourceBuilder.append(";\n");
        }

        sourceBuilder.append("\n");

        for(Method meth : clas.getMethods()){
            sourceBuilder.append("\t" + meth.getVisibility()+" ");
            sourceBuilder.append(meth.getType()+" ");
            sourceBuilder.append((meth.getName()));
            sourceBuilder.append("(");
            for(Parameter param : meth.getParameters()){
                sourceBuilder.append(param.getType() + " " + param.getName());
                if(meth.getParameters().size() > 1) sourceBuilder.append(", ");
            }
            if(meth.getParameters().size() > 1) sourceBuilder.deleteCharAt(sourceBuilder.lastIndexOf(","));
            sourceBuilder.append(") { } \n");
        }

        sourceBuilder.append("\n");

        // getters / setters
        for(Property prop : clas.getProperties()){
            sourceBuilder.append("\tpublic ");
            sourceBuilder.append(prop.getType()+" ");
            if(prop.getType().equals("boolean")) sourceBuilder.append("is"+ StringUtils.capitalize(prop.getName()));
            else sourceBuilder.append("get"+ StringUtils.capitalize(prop.getName()));

            sourceBuilder.append("() {\n\t\treturn " + prop.getName() +";\n\t}\n\n");

            sourceBuilder.append("\tpublic void ");
            sourceBuilder.append("set"+ StringUtils.capitalize(prop.getName()));
            sourceBuilder.append("("+ prop.getType() +" "+ prop.getName() +") {\n\t\tthis." +
                    prop.getName() + " = " + prop.getName() +";\n\t}\n\n");
        }

        // implemented interfaces
        // A class that implements an interface must implement all the methods declared in the interface.
        // The methods must have the exact same signature (name + parameters) as declared in the interface.
        // The class does not need to implement (declare) the variables of an interface. Only the methods.

        if(implementedInterfaces.size() > 0) sourceBuilder.append("\t// Methods from implemented interfaces\n");
        for(NodeData interfaceData : implementedInterfaces){
            for(Method meth : interfaceData.getMethods()){
                sourceBuilder.append("\t" + meth.getVisibility()+" ");
                sourceBuilder.append(meth.getType()+" ");
                sourceBuilder.append((meth.getName()));
                sourceBuilder.append("(");
                for(Parameter param : meth.getParameters()){
                    sourceBuilder.append(param.getType() + " " + param.getName());
                    if(meth.getParameters().size() > 1) sourceBuilder.append(", ");
                }
                if(meth.getParameters().size() > 1) sourceBuilder.deleteCharAt(sourceBuilder.lastIndexOf(","));
                sourceBuilder.append(") { } \n");
            }
        }

        sourceBuilder.append("\n}\n");
        //System.out.println(sourceBuilder);

        try {
            FileWriter myWriter = new FileWriter("C:\\Users\\Djordje\\Desktop\\DesignerPower_GeneratedClasses\\"+clas.getName() + ".java");
            myWriter.write(sourceBuilder.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file " + clas.getName() + ".java\n");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }



    private NodeData getNodeByKey(List<NodeData> nodeDataArray, String to) {
        for(NodeData nd : nodeDataArray){
            if(nd.getKey().equals(to))return nd;
        }
        return null;
    }


    }

// {
//         key: 'Klasica',
//         name: "BankAccount",
//         properties: [
//         { name: "owner", type: "String", visibility: "public" },
//         { name: "balance", type: "Currency", visibility: "public"}
//         ],
//         methods: [
//         { name: "deposit", parameters: [{ name: "amount", type: "Currency" }], visibility: "public" },
//         { name: "withdraw", parameters: [{ name: "amount", type: "Currency" }], visibility: "public" }
//         ],
//         category:'class'
//         }

//public abstract class BankAccount {
//    public String owner;
//    public Currency balance;
//
//    public abstract rettype deposit(Currency amount){}
//    public rettype withdray(Currency amount){
//    }
//
//    // svi geteri i seteri
//}

//public class NekaKlasa extends BankAccount {
//
//}
