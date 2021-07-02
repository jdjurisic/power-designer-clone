package com.utp.prototype.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Data
@Entity
@Table(name = "models")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @Column(unique = true)
    private String mongoKey;

    private ModelType modelType;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @JsonIgnoreProperties({ "team", "models" })
    private Project project;

    }
