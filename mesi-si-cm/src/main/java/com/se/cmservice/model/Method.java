package com.se.cmservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Method {

    private String name;
    private String type;
    private List<Parameter> parameters;
    private String visibility;
}



//        name: "deposit",
//        parameters:
//              [{ name: "amount", type: "Currency" }],
//        visibility: "public" },
