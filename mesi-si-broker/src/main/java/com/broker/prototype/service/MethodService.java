package com.broker.prototype.service;


import com.broker.prototype.entities.Method;
import com.broker.prototype.entities.SService;

import java.util.List;

public interface MethodService {
    Method save(Method Method);

    Method findByName(String name);

    List<Method> findAll();


}
