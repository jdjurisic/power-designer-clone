package com.utp.prototype.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    private String name;


    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "team")
    @JsonIgnoreProperties("team")
    private List<Project> projects;

    public void addUser(User user){
        if (!users.contains(user))
            users.add(user);
    }

    public void addProject(Project project) {
        if (!projects.contains(project))
            projects.add(project);
    }
}
