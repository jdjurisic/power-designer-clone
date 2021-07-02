package com.se.rqmservice.exception;


import com.se.rqmservice.model.DDocument;

public class ConflictException extends RuntimeException{


    private final DDocument document;

    public ConflictException(DDocument document){

        this.document = document;
    }

    public DDocument document() {
        return document;
    }
}
