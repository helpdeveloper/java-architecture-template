package br.com.helpdev.sample.core.domain.exceptions;

public class UserIsNotMajorException extends RuntimeException {

    public UserIsNotMajorException() {
        super("User is not major");
    }

}
