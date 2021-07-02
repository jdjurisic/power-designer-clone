package com.se.ucservice.model.rqm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rqm {

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

    private String users;
}
