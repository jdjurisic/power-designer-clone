package com.broker.prototype.service.impl;

import com.broker.prototype.entities.Method;
import com.broker.prototype.entities.SService;
import com.broker.prototype.repository.MethodRepository;
import com.broker.prototype.repository.ServiceRepository;
import com.broker.prototype.service.MethodService;
import com.broker.prototype.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MethodServiceImpl implements MethodService {

    private final MethodRepository repository;

    @Override
    public Method save(Method method) {

        return repository.save(method);
    }

    @Override
    public Method findByName(String name) {
        return repository.findById(name).orElse(null);
    }

    @Override
    public List<Method> findAll() {
        return repository.findAll();
    }



}
