package com.se.cmservice.exception;


import com.se.cmservice.model.DDocument;

public class ConflictException extends RuntimeException{


    private final DDocument document;

    public ConflictException(DDocument document){

        this.document = document;
    }

    public DDocument document() {
        return document;
    }
}
