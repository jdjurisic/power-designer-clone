package com.utp.prototype.service;


import com.utp.prototype.entities.Project;

import java.util.List;

public interface ProjectService {
    Project save(Project project);

    Project update(Long id, Project project);

    void deleteById(Long id);

    Project findById(Long id);

    List<Project> findAll();

    void addModelToProject(Long requirementId, Long projectId);
}
