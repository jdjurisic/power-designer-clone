package com.utp.prototype.service.impl;

import com.utp.prototype.entities.Project;
import com.utp.prototype.entities.Team;
import com.utp.prototype.entities.User;
import com.utp.prototype.repository.ProjectRepository;
import com.utp.prototype.repository.TeamRepository;
import com.utp.prototype.repository.UserRepository;
import com.utp.prototype.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository repository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TeamServiceImpl(TeamRepository repository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }


    @Override
    public Team save(Team team) {
        return repository.save(team);
    }
    @Override
    public Team update(Long id, Team team) {
        if(findById(id)==null)
            return null;
        return repository.save(team);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Team findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Team findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Team> findAll() {
        return repository.findAll();
    }

    @Override
    public void addUserToTeam(Long userId, Long teamId) {
        if(repository.findById(teamId).isPresent() && userRepository.findById(userId).isPresent()) {

            System.out.println("AddUserToTeam: Usao u IF");

            Team team = repository.findById(teamId).get();
            System.out.println("AddUserToTeam: " + team.getName());
            User user = userRepository.findById(userId).get();
            System.out.println("AddUserToTeam: " + user.getUsername());
            team.addUser(user);
            user.setTeam(team);
            repository.save(team);
            userRepository.save(user);
        }

    }

    @Override
    public void addProjectToTeam(Long projectId, Long teamId) {
        if(repository.findById(teamId).isPresent() && projectRepository.findById(projectId).isPresent()) {

            System.out.println("AddProjectToTeam: Usao u IF");

            Team team = repository.findById(teamId).get();
            System.out.println("AddProjectToTeam: " + team.getName());
            Project project = projectRepository.findById(projectId).get();
            System.out.println("AddProjectToTeam: " + project.getName());
            team.addProject(project);
            project.setTeam(team);
            repository.save(team);
            projectRepository.save(project);
        }
    }

    @Override
    public Team findTeamByUser(Long userId) {
        if(userRepository.findById(userId).isPresent()) {
            Optional<User> user = userRepository.findById(userId);
            if(user.get().getTeam()!=null) {
                return user.get().getTeam();
            }
        }
        return null;
    }

}
