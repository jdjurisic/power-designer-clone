package com.broker.prototype.service;


import com.broker.prototype.entities.SService;

import java.util.List;

public interface ServiceService {
    SService save(SService SService);

    SService findByName(String name);

    List<SService> findAll();


}
