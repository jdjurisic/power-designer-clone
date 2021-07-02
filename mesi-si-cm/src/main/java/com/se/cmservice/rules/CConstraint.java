package com.se.cmservice.rules;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CConstraint {

    String name;

    String message;

    String check;

}
