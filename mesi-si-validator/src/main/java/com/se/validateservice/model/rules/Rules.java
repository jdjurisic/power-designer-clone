package com.se.validateservice.model.rules;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rules {

    ModelType modelType;

    List<CConstraint> constraints;

    List<Critique> critiques;

}
