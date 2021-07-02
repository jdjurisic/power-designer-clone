package com.utp.prototype.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"users", "projects"})
    @NotNull
    private Team team;

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties({"project"})
    private List<Model> models;

    public void addRequirement(Model model) {
        if (!models.contains(model))
            models.add(model);

    }
}
