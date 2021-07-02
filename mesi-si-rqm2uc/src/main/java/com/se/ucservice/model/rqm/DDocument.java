package com.se.ucservice.model.rqm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DDocument {

    private String id;

    @Valid
    private List<Rqm> rqms;
}
