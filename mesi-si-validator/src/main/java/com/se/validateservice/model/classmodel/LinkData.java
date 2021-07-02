package com.se.validateservice.model.classmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkData {

    private int key;
    private String from;
    private String to;
    private String fromPort;
    private String toPort;
    private String category;
}
