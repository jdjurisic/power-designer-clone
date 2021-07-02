package com.se.ucservice.exception;


import com.se.ucservice.model.DDocument;

public class ConflictException extends RuntimeException{


    private final DDocument document;

    public ConflictException(DDocument document){

        this.document = document;
    }

    public DDocument document() {
        return document;
    }
}
