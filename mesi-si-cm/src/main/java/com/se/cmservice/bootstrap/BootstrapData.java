package com.se.cmservice.bootstrap;

import com.se.cmservice.servicemodel.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Pokrenuto.");

        registerClassModelService();
    }

    private void registerClassModelService(){
        SService service = new SService();
        service.setName("class"); // classModel
        service.setPort(8084);
        service.setUrl("http://localhost:");


        List<BrokerMethod> methodList = new ArrayList<>();
        methodList.add(new BrokerMethod(
                "saveClassModelMongo",
                "/save",
                HttpMethod.POST,
                Collections.singletonList(new BrokerParameter("document", "Document", ParameterType.BODY_PARAM)),
                Collections.singletonList(new BrokerParameter("document", "Document", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new BrokerMethod(
                "allClassModelMongo",
                "/all",
                HttpMethod.GET,
                Collections.emptyList(),
                Arrays.asList(new BrokerParameter("document","Document", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN),
                service
        ));
        methodList.add(new BrokerMethod(
                "getClassModelMongo",
                "/get",
                HttpMethod.GET,
                Collections.singletonList(new BrokerParameter("documentId", "String", ParameterType.URL_PARAM)),
                Collections.singletonList(new BrokerParameter("document", "Document", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new BrokerMethod(
                "deleteClassModelMongo",
                "/delete",
                HttpMethod.DELETE,
                Collections.singletonList(new BrokerParameter("documentId", "String", ParameterType.URL_PARAM)),
                Collections.emptyList(),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));



        //service.setMethods(methodList);
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.postForObject("http://localhost:8081/service/save", service, SService.class);

        for(BrokerMethod m : methodList){
            restTemplate.postForObject("http://localhost:8081/method/save",m,BrokerMethod.class);
        }

        System.out.println("RestTemplate za Dokument(ClassModel) deo servisa je poslat brokeru!");

    }

}
