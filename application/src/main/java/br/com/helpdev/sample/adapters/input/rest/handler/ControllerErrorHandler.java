package br.com.helpdev.sample.adapters.input.rest.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.helpdev.sample.core.domain.exceptions.UserIsNotMajorException;
import br.com.helpdev.sample.core.domain.exceptions.UserNotFoundException;

@RestControllerAdvice
class ControllerErrorHandler {

   @ExceptionHandler(UserIsNotMajorException.class)
   public ProblemDetail handlerResourceIsNotMajor(final UserIsNotMajorException ex) {
      return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
   }

   @ExceptionHandler(IllegalArgumentException.class)
   public ProblemDetail handlerIllegalArgumentException(final IllegalArgumentException ex) {
      return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
   }

   @ExceptionHandler(UserNotFoundException.class)
   public ProblemDetail handlerUserNotFoundException(final UserNotFoundException ex) {
      return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
   }

}
