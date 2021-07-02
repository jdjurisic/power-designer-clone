package com.utp.prototype.controller;


import com.utp.prototype.entities.Team;
import com.utp.prototype.entities.User;
import com.utp.prototype.model.MyUserDetails;
import com.utp.prototype.service.MyUserDetailsService;
import com.utp.prototype.service.ProjectService;
import com.utp.prototype.service.TeamService;
import com.utp.prototype.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("team")
@CrossOrigin
public class TeamController {

    private final TeamService service;
    private final MyUserDetailsService myUserDetailsService;
    private final UserService userService;
    private final ProjectService projectService;

    public TeamController(TeamService service, MyUserDetailsService myUserDetailsService,
                          UserService userService, ProjectService projectService){
        this.service = service;
        this.myUserDetailsService = myUserDetailsService;
        this.userService = userService;
        this.projectService = projectService;
    }


    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Team> save( @Valid @RequestBody Team team){
        return new ResponseEntity<>(service.save(team), HttpStatus.CREATED);
    }
    @PutMapping("/update/{teamId}")
    public ResponseEntity<Team> update(@PathVariable Long teamId, @Valid @RequestBody Team team){
        if(service.findById(teamId)==null)
            return null;
        return new ResponseEntity<>(service.update(teamId, team), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{teamId}")
    public ResponseEntity<?> deleteById(@PathVariable Long teamId){
        service.deleteById(teamId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get/{teamId}")
    public ResponseEntity<Team> findById( @PathVariable Long teamId){
        return new ResponseEntity<>(service.findById(teamId), HttpStatus.OK);
    }
    @GetMapping("/getByName/{teamName}")
    public ResponseEntity<Team> findById( @PathVariable String teamName){
        return new ResponseEntity<>(service.findByName(teamName), HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<Team> findAll(){     //(@RequestHeader("Authorization") String authorization){
        return service.findAll();
    }

    @PostMapping("/addUserToTeam/{userId}/{teamId}")
    public ResponseEntity<?> addUserToTeam(@PathVariable Long userId, @PathVariable Long teamId){
        if(service.findById(teamId)==null || userService.findById(userId)==null)
            return null;
        service.addUserToTeam(userId,teamId);
        return new ResponseEntity<>(HttpStatus.OK);
        //return new ResponseEntity<>(service.addUserToTeam(userId,teamId),HttpStatus.CREATED);
    }

    @PostMapping("/addProjectToTeam/{projectId}/{teamId}")
    public ResponseEntity<?> addProjectToTeam(@PathVariable Long projectId, @PathVariable Long teamId){
        if(service.findById(teamId)==null || projectService.findById(projectId)==null)
            return null;
        service.addProjectToTeam(projectId, teamId);
        return new ResponseEntity<>(HttpStatus.OK);
        //return new ResponseEntity<>(service.addUserToTeam(userId,teamId),HttpStatus.CREATED);
    }


    @GetMapping("/findTeamByUser/{userId}")
    public ResponseEntity<Team> findTeamByUser(@PathVariable Long userId) {
        return new ResponseEntity<>(service.findTeamByUser(userId), HttpStatus.OK);
    }
    @GetMapping("/findTeamByUser")
    public ResponseEntity<Team> findTeamByUser() {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(service.findTeamByUser(myUserDetails.getUser().getId()), HttpStatus.OK);
    }
}
