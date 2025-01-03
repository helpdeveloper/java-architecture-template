package br.com.helpdev.sample.core.domain.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

   public UserNotFoundException(UUID uuid) {
      super("User not found with uuid: " + uuid.toString());
   }

}
