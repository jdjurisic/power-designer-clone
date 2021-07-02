package com.se.cmservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkData linkData = (LinkData) o;
        return Objects.equals(from, linkData.from) && Objects.equals(to, linkData.to) && Objects.equals(fromPort, linkData.fromPort) && Objects.equals(toPort, linkData.toPort) && Objects.equals(category, linkData.category);
    }

}
