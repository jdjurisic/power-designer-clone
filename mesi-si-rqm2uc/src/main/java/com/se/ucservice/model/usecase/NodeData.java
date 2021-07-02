package com.se.ucservice.model.usecase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeData
{
    private String key;
    private String text;
    private String color;
    private String category;
    //private String loc;
}

//    key:string;
//            text:string;
//            color:string;
//            category:string;
//            loc:string;
