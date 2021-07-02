package com.broker.prototype.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "service")
public class SService {


    @Id
    private String name;

    @Column(nullable = false)
    private int port;

    @Column(nullable = false)
    private String url;

    @OneToMany(mappedBy = "service", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("service")
    //@JsonIgnore
    private List<Method> methods;



}
