package com.utp.prototype.service.impl;

import com.utp.prototype.entities.Model;
import com.utp.prototype.repository.ModelRepository;
import com.utp.prototype.service.ModelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    private final ModelRepository repository;

    public ModelServiceImpl(ModelRepository repository) {
        this.repository = repository;
    }

    @Override
    public Model save(Model model) {
        return repository.save(model);
    }

    @Override
    public Model update(Long id, Model model) {
        if(findById(id)==null)
            return null;
        return repository.save(model);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Model findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Model> findAll() {
        return repository.findAll();
    }
}
