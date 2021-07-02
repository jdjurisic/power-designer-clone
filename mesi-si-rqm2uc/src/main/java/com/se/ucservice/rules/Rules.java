package com.se.ucservice.rules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rules {

    ModelType modelType;

    List<CConstraint> constraints;

    List<Critique> critiques;

}
