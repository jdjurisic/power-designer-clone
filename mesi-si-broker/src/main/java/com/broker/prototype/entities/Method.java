package com.broker.prototype.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Method {

    @Id
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HttpMethod httpMethod;

    @ElementCollection
    private List<Parameter> request;

    @ElementCollection
    private List<Parameter> response;

    @ElementCollection
    private List<UserType> allowedTypes;

    @ManyToOne
    @JoinColumn(name = "service_name", referencedColumnName = "name")
    @JsonIgnoreProperties("methods")
    private SService service;




}
