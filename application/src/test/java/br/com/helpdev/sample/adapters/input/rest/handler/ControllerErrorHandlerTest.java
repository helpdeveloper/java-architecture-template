package br.com.helpdev.sample.adapters.input.rest.handler;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import br.com.helpdev.sample.core.domain.exceptions.UserIsNotMajorException;
import br.com.helpdev.sample.core.domain.exceptions.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class ControllerErrorHandlerTest {

   @Test
   void handlerResourceIsNotMajor_shouldReturnProblemDetail() {
      final var controllerErrorHandler = new ControllerErrorHandler();

      final var response = controllerErrorHandler.handlerResourceIsNotMajor(new UserIsNotMajorException());

      assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
      assertEquals("User is not major", response.getDetail());
   }

   @Test
   void handlerIllegalArgumentException_shouldReturnProblemDetail() {
      final var controllerErrorHandler = new ControllerErrorHandler();

      final var response = controllerErrorHandler.handlerIllegalArgumentException(new IllegalArgumentException("Email invalid"));

      assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
      assertEquals("Email invalid", response.getDetail());
   }

   @Test
   void handlerUserNotFoundException_shouldReturnProblemDetail() {
      final var controllerErrorHandler = new ControllerErrorHandler();
      final var userNotFound = new UserNotFoundException(UUID.randomUUID());

      final var response = controllerErrorHandler.handlerUserNotFoundException(userNotFound);

      assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
      assertEquals(userNotFound.getMessage(), response.getDetail());
   }

}