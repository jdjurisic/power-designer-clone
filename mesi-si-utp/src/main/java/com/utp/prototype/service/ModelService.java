package com.utp.prototype.service;

import com.utp.prototype.entities.Model;

import java.util.List;

public interface ModelService {
    Model save(Model model);

    Model update(Long id, Model model);

    void deleteById(Long id);

    Model findById(Long id);

    List<Model> findAll();
}
