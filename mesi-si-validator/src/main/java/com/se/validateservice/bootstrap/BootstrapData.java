package com.se.validateservice.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.se.validateservice.model.broker.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Pokrenuto.");

        /* Slanje servisa na broker */
        //registerValidateService();
    }

    private void registerValidateService(){
        SService service = new SService();
        service.setName("validate");
        service.setPort(8086);
        service.setUrl("http://localhost:");


        List<Method> methodList = new ArrayList<>();
        methodList.add(new Method(
                "validateUseCase",
                "/usecase",
                HttpMethod.GET,
                Collections.singletonList(new Parameter("document", "DocumentUseCaseModel", ParameterType.BODY_PARAM)),
                Collections.singletonList(new Parameter("document", "DocumentUseCaseModel", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "validateClassModel",
                "/class",
                HttpMethod.GET,
                Collections.singletonList(new Parameter("document", "DocumentClassModel", ParameterType.BODY_PARAM)),
                Collections.singletonList(new Parameter("document", "DocumentClassModel", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "helpUseCase",
                "/help/usecase",
                HttpMethod.GET,
                Collections.emptyList(),
                Collections.singletonList(new Parameter("rules", "Rules", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "helpClassModel",
                "/help/class",
                HttpMethod.GET,
                Collections.emptyList(),
                Collections.singletonList(new Parameter("rules", "Rules", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.postForObject("http://localhost:8081/service/save", service, SService.class);

        for(Method m : methodList){
            restTemplate.postForObject("http://localhost:8081/method/save",m,Method.class);
        }

        System.out.println("RestTemplate za Validaciju dokumenta(modela) je poslat brokeru!");

    }

}
