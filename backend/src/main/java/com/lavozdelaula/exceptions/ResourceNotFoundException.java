package com.lavozdelaula.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Integer id) {
        super(resourceName + " con id " + id + " no fue encontrado.");
    }
}
