package com.broker.prototype.service.impl;

import com.broker.prototype.repository.MethodRepository;
import com.broker.prototype.repository.ServiceRepository;
import com.broker.prototype.service.MethodService;
import com.broker.prototype.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.broker.prototype.entities.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository repository;
    private final MethodService methodService;



    @Override
    public SService save(SService service) {
        if(service.getMethods()!=null) {
            service.getMethods().forEach(methodService::save);
            methodService.findAll()
                    .forEach(method -> {
                        if(service.getMethods().contains(method))
                            method.setService(service);
                    });
            System.out.println("Pronasao sam metode u servisu!");
        }
        // Metode bez servisa


        return repository.save(service);
    }

    @Override
    public SService findByName(String name) {
        return repository.findById(name).orElse(null);
    }

    @Override
    public List<SService> findAll() {
        return repository.findAll();
    }

}
