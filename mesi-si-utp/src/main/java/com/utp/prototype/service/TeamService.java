package com.utp.prototype.service;


import com.utp.prototype.entities.Team;

import java.util.List;

public interface TeamService {
    Team save(Team team);

    Team update(Long id, Team team);

    void deleteById(Long id);

    Team findById(Long id);

    Team findByName(String name);

    List<Team> findAll();

    void addUserToTeam(Long userId, Long teamId);

    void addProjectToTeam(Long projectId, Long teamId);

    Team findTeamByUser(Long userId);

}
