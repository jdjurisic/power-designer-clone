package com.se.validateservice.service;

import com.se.validateservice.model.classmodel.*;
import com.se.validateservice.model.rules.CConstraint;
import com.se.validateservice.model.rules.ModelType;
import com.se.validateservice.model.rules.Rules;
import com.se.validateservice.model.usecase.DocumentUseCaseModel;
import com.se.validateservice.model.usecase.UseCaseLinkData;
import com.se.validateservice.model.usecase.UseCaseNodeData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class ValidationService {

    List<String> errorMessages;

    public ResponseEntity<?> validateUseCaseModel(DocumentUseCaseModel documentUseCaseModel){
        errorMessages = new ArrayList<>();

        if(documentUseCaseModel.getLinkDataArray()==null || documentUseCaseModel.getNodeDataArray()==null)
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Dokument ne sadrzi linkove ili nodove.");

        if(validateUseCase(documentUseCaseModel))
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(errorMessages);
        else
            return ResponseEntity
                    .status(HttpStatus.OK) // (HttpStatus.BAD_REQUEST)
                    .body(errorMessages);
    }

    private boolean validateUseCase(DocumentUseCaseModel documentUseCaseModel) {

        proveraDvosmernost(documentUseCaseModel);
        proveraAktera(documentUseCaseModel);
        proveraAsocijacije(documentUseCaseModel);
        proveraUnikatnihImena(documentUseCaseModel);

        return errorMessages.isEmpty();
    }

    public ResponseEntity<?> getUseCaseHelp() {
        Rules useCaseRules = new Rules();
        useCaseRules.setModelType(ModelType.USE_CASE);
        CConstraint constraint01 = new CConstraint();
        constraint01.setName("proveraDvosmernost");
        constraint01.setMessage("Ne sme da postoji dvosmerna veza izmedju dva entiteta.");
        constraint01.setCheck("if linkA.getFrom == linkB.getTo && linkA.getTo == linkB.getFrom");

        CConstraint constraint02 = new CConstraint();
        constraint02.setName("proveraAktera");
        constraint02.setMessage("Veza izmedju vise aktora moze da bude samo generalizacija.");
        constraint02.setCheck("");

        CConstraint constraint03 = new CConstraint();
        constraint03.setName("proveraUnikatnihImena");
        constraint03.setMessage("Ime ne sme da se ponavlja vise puta.");
        constraint03.setCheck("");

        CConstraint constraint04 = new CConstraint();
        constraint04.setName("proveraAsocijacije");
        constraint04.setMessage("Veza izmedju usecase i actora moze da bude samo asociacija.");
        constraint04.setCheck("");


        List<CConstraint> constraints = new ArrayList<>();
        constraints.add(constraint01);
        constraints.add(constraint02);
        constraints.add(constraint03);

        useCaseRules.setConstraints(constraints);

        List<String> rules = new ArrayList<>();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(useCaseRules);
    }

    private void proveraDvosmernost(DocumentUseCaseModel documentUseCaseModel){
        List<UseCaseLinkData> veze = documentUseCaseModel.getLinkDataArray();
        for(UseCaseLinkData useCaseLinkData : veze){
            for(UseCaseLinkData useCaseLinkData1 : veze){
                if(useCaseLinkData.getFrom().equals(useCaseLinkData1.getTo())
                    && useCaseLinkData.getTo().equals(useCaseLinkData1.getFrom()))
                    errorMessages.add("Ne sme dvosmerna veza " + useCaseLinkData.getFrom() + ", " + useCaseLinkData1.getFrom());
            }
        }
    }

    private void proveraAktera(DocumentUseCaseModel document){
        List<UseCaseLinkData> veze  = document.getLinkDataArray();
        List<UseCaseNodeData> nodovi = document.getNodeDataArray();
        for(UseCaseLinkData veza : veze){
            for(UseCaseNodeData n : nodovi){
                for(UseCaseNodeData m : nodovi){
                    if(n.getCategory().equals("actor") && m.getCategory().equals("actor")){
                        if((veza.getFrom().equals(n.getKey()) && veza.getTo().equals(m.getKey())) || (veza.getFrom().equals(m.getKey()) && veza.getTo().equals(n.getKey()))){
                            if(!veza.getCategory().equals("generalization")){
                                errorMessages.add("Veza izmedju aktora " + n.getKey() + " i " + m.getKey() + " moze da bude samo generalizacija.");
                            }
                        }
                    }
                }
            }
        }
    }
//    private void proveraAsocijacije(DocumentUseCaseModel document){
//        List<UseCaseLinkData> veze  = document.getLinkDataArray();
//        List<UseCaseNodeData> nodovi = document.getNodeDataArray();
//        for(UseCaseLinkData veza : veze){
//            for(UseCaseNodeData n : nodovi){
//                for(UseCaseNodeData m : nodovi){
//                    if((n.getCategory().equals("actor") && m.getCategory().equals("usecase")) || (n.getCategory().equals("usecase") && m.getCategory().equals("actor"))){
//                        if(!veza.getCategory().equals("association")){
//                            errorMessages.add("Veza izmedju "+n.getKey()+" i "+m.getKey()+" moze da bude samo asociacija");
//                        }
//                    }
//                }
//            }
//        }
//    }
    private void proveraAsocijacije(DocumentUseCaseModel document){
        List<UseCaseLinkData> veze  = document.getLinkDataArray();
        List<UseCaseNodeData> nodovi = document.getNodeDataArray();
        for(UseCaseLinkData veza : veze){
            for(UseCaseNodeData n : nodovi){
                for(UseCaseNodeData m : nodovi){
                    if((n.getKey().equals(veza.getFrom()) && m.getKey().equals(veza.getTo())
                            || m.getKey().equals(veza.getFrom()) && n.getKey().equals(veza.getTo()))){
                        if((n.getCategory().equals("actor") && m.getCategory().equals("usecase")) || (n.getCategory().equals("usecase") && m.getCategory().equals("actor"))){
                            if(!veza.getCategory().equals("association")){
                                errorMessages.add("Veza izmedju "+n.getText()+" i "+m.getText()+" moze da bude samo asociacija");
                            }
                        }
                    }
                }
            }
        }
    }

    public void proveraUnikatnihImena(DocumentUseCaseModel documentUseCaseModel){
        ArrayList<String> imena = new ArrayList<>();
        List<UseCaseNodeData> nodovi = documentUseCaseModel.getNodeDataArray();
        for(UseCaseNodeData nodeData : nodovi){
            if(imena.contains(nodeData.getText())){
                errorMessages.add("Ime " + nodeData.getText() + " se ponavlja vise puta.");
            }else
                imena.add(nodeData.getText());
        }
    }

    /*
        Sledi validacija klasnog modela.

     */


    public ResponseEntity<?> validateClassModel(DDocument document) {
        errorMessages = new ArrayList<>();
        System.out.println("Validiram klasni model.");
        if(document.getLinkDataArray()==null || document.getNodeDataArray()==null)
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Dokument ne sadrzi linkove ili nodove.");

        if(validateClass(document))
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(errorMessages);

        else
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(errorMessages);

    }

    private boolean validateClass(DDocument documentClassModel) {

//         Ovde idu pozivi novih metoda za validaciju ClassModela.

        proveraDvosmernost(documentClassModel);  //radi
        proveraImplementacija(documentClassModel); // sta je ovo - proradilo 100% :)
        proveraUnikatnaImenaKlasa(documentClassModel); // radi
        proveraUnikatnaImenaAtributa(documentClassModel);// kaze jovica da radi 100%
        proveraUnikatnaImenaMetoda(documentClassModel);// kaze jovica da radi 100%
        proveraInterfaceAtribut(documentClassModel); // kaze jovica da radi 100%
        proveraInterfaceMetode(documentClassModel); // radi
        proveraInterfaceEkstenzija(documentClassModel);  // sta je ovo
        proveraViseRoditelja(documentClassModel);// kaze iva da radi 100%
        proveraTipova(documentClassModel);
        return errorMessages.isEmpty();
    }


    public ResponseEntity<?> getClassModelHelp() {
        Rules classModelRules = new Rules();
        classModelRules.setModelType(ModelType.CLASS);

        CConstraint constraint01 = new CConstraint();
        constraint01.setName("proveraDvosmernost");
        constraint01.setMessage("Ne sme da postoji dvosmerna veza izmedju dva entiteta.");
        constraint01.setCheck("if linkA.getFrom == linkB.getTo && linkA.getTo == linkB.getFrom");

        CConstraint constraint02 = new CConstraint();
        constraint02.setName("proveraImplementacija");
        constraint02.setMessage("");
        constraint02.setCheck("");

        CConstraint constraint03 = new CConstraint();
        constraint03.setName("proveraUnikatnaImenaKlasa");
        constraint03.setMessage("");
        constraint03.setCheck("");

        CConstraint constraint04 = new CConstraint();
        constraint04.setName("proveraUnikatnaImenaKlasa");
        constraint04.setMessage("");
        constraint04.setCheck("");

        CConstraint constraint05 = new CConstraint();
        constraint05.setName("proveraUnikatnaImenaKlasa");
        constraint05.setMessage("");
        constraint05.setCheck("");

        CConstraint constraint06 = new CConstraint();
        constraint06.setName("proveraUnikatnaImenaKlasa");
        constraint06.setMessage("");
        constraint06.setCheck("");

        CConstraint constraint07 = new CConstraint();
        constraint07.setName("proveraUnikatnaImenaKlasa");
        constraint07.setMessage("");
        constraint07.setCheck("");

        CConstraint constraint08 = new CConstraint();
        constraint08.setName("proveraUnikatnaImenaKlasa");
        constraint08.setMessage("");
        constraint08.setCheck("");

        CConstraint constraint09 = new CConstraint();
        constraint09.setName("proveraUnikatnaImenaKlasa");
        constraint09.setMessage("");
        constraint09.setCheck("");


        List<CConstraint> constraints = new ArrayList<>();
        constraints.add(constraint01);
        constraints.add(constraint02);
        constraints.add(constraint03);
        constraints.add(constraint04);
        constraints.add(constraint05);
        constraints.add(constraint06);
        constraints.add(constraint07);
        constraints.add(constraint08);
        constraints.add(constraint09);

        classModelRules.setConstraints(constraints);

        List<String> rules = new ArrayList<>();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(classModelRules);
    }

    private void proveraDvosmernost(DDocument documentUseCaseModel){
        List<LinkData> veze = documentUseCaseModel.getLinkDataArray();
        for(LinkData useCaseLinkData : veze){
            for(LinkData useCaseLinkData1 : veze){
                if(useCaseLinkData.getFrom().equals(useCaseLinkData1.getTo())
                        && useCaseLinkData.getTo().equals(useCaseLinkData1.getFrom())
                ||
                        ( useCaseLinkData.getFrom().equals(useCaseLinkData1.getFrom())
                                && useCaseLinkData.getTo().equals(useCaseLinkData1.getTo())
                                && !(useCaseLinkData.getKey() == useCaseLinkData1.getKey())
                        )
                )
                    errorMessages.add("Ne sme dvosmerna veza " + useCaseLinkData.getFrom() + ", " + useCaseLinkData1.getFrom());
            }
        }
    }
    private void proveraImplementacija(DDocument document) {
        List<LinkData> veze = document.getLinkDataArray();
        List<NodeData> nodovi = document.getNodeDataArray();

        // realizacija moze izmedju interfejsa ili izmedju klase/absklase i interfejsa
        for (LinkData veza : veze) {
            for (NodeData n : nodovi) {
                for (NodeData m : nodovi) {
                    if (!n.getCategory().equals("interface") && !m.getCategory().equals("interface")) {
                        if ((veza.getFrom().equals(n.getKey()) && veza.getTo().equals(m.getKey()))) {
                            if (veza.getCategory().equals("realization")) {
                                errorMessages.add("Veza izmedju " + n.getName() + " i " + m.getName() + " ne sme da bude implementacija");
                            }
                        }
                    }
                }
            }
        }
    }

    private void proveraInterfaceMetode(DDocument document){
        List<NodeData> klase  = document.getNodeDataArray();
        for(NodeData a : klase) {
            if (a.getCategory().equals("interface")) {
                List<Method> metode = a.getMethods();
                for (Method metoda : metode) {
                    if (!metoda.getVisibility().equals("public")) {
                        errorMessages.add("Metoda " + metoda.getName() + " u " + a.getName() + " mora da bude public");
                    }
                }
            }
        }
    }
    private void proveraInterfaceAtribut(DDocument document){
        List<NodeData> klase  = document.getNodeDataArray();
        for(NodeData a : klase){
            if(a.getCategory().equals("interface")){
                List<Property> atributi = a.getProperties();
                for(Property atribut : atributi){
                    if(!atribut.getVisibility().equals("public")){
                        errorMessages.add("Atribut "+atribut.getName()+" u "+a.getName()+" mora da bude public");
                    }
                }
            }
        }
    }
    private void proveraUnikatnaImenaMetoda(DDocument document){
        List<NodeData> klase  = document.getNodeDataArray();
        for(NodeData a : klase){
            List<Method> metode = a.getMethods();
            List<String> imena = new ArrayList<>();
            for(Method metoda : metode){
                if(imena.contains(metoda.getName())){
                    errorMessages.add("Metoda "+metoda.getName()+" se ponavlja vise puta");
                }else imena.add(metoda.getName());
            }
        }
    }
    private void proveraUnikatnaImenaAtributa(DDocument document){
        List<NodeData> klase  = document.getNodeDataArray();
        for(NodeData a : klase){
            List<Property> atributi = a.getProperties();
            List<String> imena = new ArrayList<>();
            for(Property atribut : atributi){
                if(imena.contains(atribut.getName())){
                    errorMessages.add("Atribut "+atribut.getName()+" se ponavlja vise puta u "+a.getName());
                }else imena.add(atribut.getName());
            }
        }
    }
    private void proveraUnikatnaImenaKlasa(DDocument document){
        List<NodeData> klase  = document.getNodeDataArray();
        List<String> imena = new ArrayList<>();
        for(NodeData a : klase) {
            if (imena.contains(a.getName())) {
                errorMessages.add("Klasa " + a.getName() + " se ponavlja vise puta");
            } else imena.add(a.getName());
        }
    }
    private void proveraInterfaceEkstenzija(DDocument document){
        List<LinkData> veze  = document.getLinkDataArray();
        List<NodeData> nodovi = document.getNodeDataArray();

        NodeData od_cvor = null, ka_cvor = null;
        for(LinkData veza : veze){
            for(NodeData n : nodovi){
                if(veza.getFrom().equals(n.getKey())) od_cvor = n;
                if(veza.getTo().equals(n.getKey())) ka_cvor = n;
                for(NodeData m : nodovi){
                    if(n.getCategory().equals("interface") && m.getCategory().equals("interface")){
                        if((veza.getFrom().equals(n.getKey()) && veza.getTo().equals(m.getKey()))
                                || (veza.getFrom().equals(m.getName()) && veza.getTo().equals(n.getName()))){
                            if(!veza.getCategory().equals("generalization")){
                                errorMessages.add("Veza izmedju "+n.getName()+" i "+m.getName()+" mora da bude ekstenzija");
                            }
                        }
                    }
                }
            }

            //dopuna interfejs ne sme da realizuje klasu
            if(veza.getCategory().equals("realization") || veza.getCategory().equals("association")){
                if(od_cvor.getCategory().equals("interface") && !ka_cvor.getCategory().equals("interface")){
                    errorMessages.add("Interfejs " + od_cvor.getName() + " moze samo da nasledjuje samo druge interfejse.");
                }
            }
        }
    }
    private void proveraViseRoditelja(DDocument document){
        HashSet<String> kljucevi = new HashSet<String>();

        List<LinkData> veze = document.getLinkDataArray();
        for(LinkData veza : veze){
            if(veza.getCategory().equals("generalization")){
                if(kljucevi.contains(veza.getFrom())){
                    errorMessages.add("Klasa " + veza.getFrom() + " ima vise roditelja.");
                }else kljucevi.add(veza.getFrom());
            }
        }
        /*
        List<LinkData> veze = document.getLinkDataArray();
        List<String> deca = new ArrayList<>();
        for(LinkData veza : veze){
            if(!deca.contains(veza.getFrom()) && veza.getCategory().equals("generalization")){
                errorMessages.add("Klasa "+veza.getFrom()+" ima vise od jednog roditelja");
            }else deca.add(veza.getFrom());
        }*/
    }


    private void proveraTipova(DDocument document){
        List<String> tipovi =  new ArrayList<>(Arrays.asList("int","String","char","boolean","Long","short","long","double","float","ptica"));
        List<NodeData> klase  = document.getNodeDataArray();
        for(NodeData a : klase){
            tipovi.add(a.getName());
        }
        for(NodeData a : klase){
            List<Property> atributi = a.getProperties();
            for(Property atribut : atributi){
                if(!tipovi.contains(atribut.getType())){
//                    errorMessages.add("Atribut "+atribut.getName()+" nema klasu "+atribut.getType());
                    errorMessages.add("Klasa "+atribut.getType() + " kod atributa "+atribut.getName() +" nije definisana.");
                }
            }
        }
        for(NodeData x : klase){
            List<Method> metode = x.getMethods();
            for(Method metoda : metode){
                if(metoda.getType()!=null && !tipovi.contains(metoda.getType())) {
                    errorMessages.add("Klasa "+metoda.getType() + " kod metode "+metoda.getName() +" nije definisana.");
                }
                List<Parameter> parametri = metoda.getParameters();
                for(Parameter parametar : parametri){
                    if(!tipovi.contains(parametar.getType())){
                        errorMessages.add("Klasa "+parametar.getType() + " kod parametra "+parametar.getName() +" nije definisana.");
//                        errorMessages.add("Atribut "+parametar.getName()+" nema klasu "+parametar.getType());
                    }
                }
            }
        }

    }



}

