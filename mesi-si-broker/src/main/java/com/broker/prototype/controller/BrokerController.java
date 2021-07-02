package com.broker.prototype.controller;


import com.broker.prototype.entities.SService;
import com.broker.prototype.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("broker")
@RequiredArgsConstructor
@CrossOrigin
public class BrokerController {

    private final ServiceService serviceService;

    // broker/project/save/1/2/3/4
    @RequestMapping(path = "/{serviceName}/**")
    public ResponseEntity<?> route(
            @PathVariable String serviceName,
            @RequestBody(required=false) String body,
            @RequestHeader MultiValueMap<String,String> headers,
            HttpMethod httpMethod,
            HttpServletRequest httpServletRequest
    ) {
        //System.out.println(getAuthorizationHeader(new HttpEntity<>(body,headers)));

        SService service = serviceService.findByName(serviceName);
        if (service == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        String method = methodRoute(httpServletRequest.getRequestURI());
        System.out.println("Method: " + method);
        String url = service.getUrl() + service.getPort() + '/' + service.getName() + method;
        System.out.println("URL: " + url);
        if (httpServletRequest.getQueryString() != null && !httpServletRequest.getQueryString().isEmpty()) //nebitno
            url += "?" + httpServletRequest.getQueryString();


        // preauthorize
        if(service.getName().equals("rqms")
           || service.getName().equals("usecases")
            || service.getName().equals("classes")){
            //if(!(service.getName().equals("auth")&&method.equals("/login"))){
            RestTemplate restTemplateAuth = new RestTemplate();
            String url2 = "http://localhost:8082/auth/validate/"+ getAuthorizationHeader(new HttpEntity<>(body, headers));
            System.out.println("URL2 "+ url2);
            boolean valid = false;
            try{
                valid = restTemplateAuth.getForObject(url2,Boolean.class);

            }catch (NullPointerException e){
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            //boolean valid = restTemplateAuth.getForObject(url2,Boolean.class);
            System.out.println("VALID TOKEN - "+ valid);
            if(!valid){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url, httpMethod, new HttpEntity<>(body, headers), String.class);
    }

    // Metoda za izvlacenje auth header (koji sadrzi token) iz http entiteta.
    public String getAuthorizationHeader(HttpEntity<?> httpEntity) {
        String authorizationHeader = String.valueOf(httpEntity.getHeaders().get("authorization"));
        return authorizationHeader.substring(8, authorizationHeader.length() - 1);
    }

    private String methodRoute(String methodUrl) {
        // number of '/' until we reach the endpoint route.
        // format: /broker/project/save/1
        // format: /broker/{serviceName}/**
        int numberOfDelimiters = 3;
        int start = 0, delimiterCounter = 0;
        while (delimiterCounter != numberOfDelimiters) {
            if (start == methodUrl.length())
                throw new IllegalStateException(
                        "Method route not correct!"
                );
            if (methodUrl.charAt(start) == '/')
                delimiterCounter++;
            start++;
        }
        return methodUrl.substring(start - 1);
    }
}
