package com.se.rqmservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Data
public class Rqm {

    @Id
    private int id;
    @NotNull
    private String indentation;
    @NotNull
    private String title;
    private String description;
    private RqmType type;
    private int priority;
    private RqmRisk risk;

    private List<Rqm> rqms;

    private List<String> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rqm rqm = (Rqm) o;
        return priority == rqm.priority && Objects.equals(title, rqm.title) && Objects.equals(description, rqm.description) && type == rqm.type && risk == rqm.risk;
    }
}
