package com.project.onlybuns.model;

public class NotFoundPostException extends RuntimeException {
    public NotFoundPostException(String message) {
        super(message);}
}