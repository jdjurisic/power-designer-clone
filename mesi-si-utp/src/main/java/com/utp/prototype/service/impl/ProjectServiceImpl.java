package com.utp.prototype.service.impl;

import com.utp.prototype.entities.Project;
import com.utp.prototype.entities.Model;
import com.utp.prototype.repository.ProjectRepository;
import com.utp.prototype.repository.ModelRepository;
import com.utp.prototype.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repository;
    private final ModelRepository modelRepository;


    public ProjectServiceImpl(ProjectRepository repository, ModelRepository modelRepository) {
        this.repository = repository;

        this.modelRepository = modelRepository;
    }


    @Override
    public Project save(Project project) {
        return repository.save(project);
    }
    @Override
    public Project update(Long id, Project project) {
        if(findById(id)==null)
            return null;
        return repository.save(project);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Project findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Project> findAll() {
        return repository.findAll();
    }

    @Override
    public void addModelToProject(Long requirementId, Long projectId) {
        if(repository.findById(projectId).isPresent() && modelRepository.findById(requirementId).isPresent()) {
            System.out.println("addRequirementToProject: Usao u IF");

            Project project = repository.findById(projectId).get();
            System.out.println("addRequirementToProject: " + project.getName());
            Model model = modelRepository.findById(requirementId).get();
            System.out.println("addRequirementToProject: " + model.getName());

            project.addRequirement(model);
            model.setProject(project);

            repository.save(project);
            modelRepository.save(model);
        }

    }


}
