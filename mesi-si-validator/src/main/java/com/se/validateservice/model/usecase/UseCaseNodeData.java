package com.se.validateservice.model.usecase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UseCaseNodeData
{
    private String key;
    private String text;
    private String color;
    private String category;
    private String loc;
}
