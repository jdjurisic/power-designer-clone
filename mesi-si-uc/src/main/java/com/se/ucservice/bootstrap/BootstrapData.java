package com.se.ucservice.bootstrap;

import com.se.ucservice.servicemodel.*;
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

        registerUseCaseService();
    }

    private void registerUseCaseService(){
        SService service = new SService();
        service.setName("usecase"); // useCase
        service.setPort(8085);
        service.setUrl("http://localhost:");


        List<Method> methodList = new ArrayList<>();
        methodList.add(new Method(
                "saveUseCaseMongo",
                "/save",
                HttpMethod.POST,
                Collections.singletonList(new Parameter("document", "Document", ParameterType.BODY_PARAM)),
                Collections.singletonList(new Parameter("document", "Document", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "allUseCaseMongo",
                "/all",
                HttpMethod.GET,
                Collections.emptyList(),
                Arrays.asList(new Parameter("document","Document", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN),
                service
        ));
        methodList.add(new Method(
                "getUseCaseMongo",
                "/get",
                HttpMethod.GET,
                Collections.singletonList(new Parameter("documentId", "String", ParameterType.URL_PARAM)),
                Collections.singletonList(new Parameter("document", "Document", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "deleteUseCaseMongo",
                "/delete",
                HttpMethod.DELETE,
                Collections.singletonList(new Parameter("documentId", "String", ParameterType.URL_PARAM)),
                Collections.emptyList(),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));



        //service.setMethods(methodList);
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.postForObject("http://localhost:8081/service/save", service, SService.class);

        for(Method m : methodList){
            restTemplate.postForObject("http://localhost:8081/method/save",m,Method.class);
        }

        System.out.println("RestTemplate za Dokument(use case) deo servisa je poslat brokeru!");

    }

}
