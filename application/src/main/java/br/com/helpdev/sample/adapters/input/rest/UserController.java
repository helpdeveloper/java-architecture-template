package br.com.helpdev.sample.adapters.input.rest;

import java.net.URI;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.helpdev.sample.adapters.input.rest.dto.UserRequestDto;
import br.com.helpdev.sample.adapters.input.rest.dto.UserResponseDto;
import br.com.helpdev.sample.adapters.input.rest.mapper.UserRestMapper;
import br.com.helpdev.sample.core.ports.input.UserCreatorPort;
import br.com.helpdev.sample.core.ports.input.UserGetterPort;
import jakarta.validation.Valid;

@RestController
class UserController {

   private final Logger logger = LoggerFactory.getLogger(UserController.class);

   private final UserCreatorPort userCreatorPort;

   private final UserGetterPort userGetterPort;

   UserController(final UserCreatorPort userCreatorPort, final UserGetterPort userGetterPort) {
      this.userCreatorPort = userCreatorPort;
      this.userGetterPort = userGetterPort;
   }

   @PostMapping("/user")
   public ResponseEntity<?> createUser(@RequestBody @Valid final UserRequestDto userRequestDto) {
      final var user = userCreatorPort.createUser(UserRestMapper.toDomain(userRequestDto));
      logger.info("User created: {}", user.uuid());
      return ResponseEntity.created(URI.create("/user/" + user.uuid())).build();
   }

   @GetMapping("/user/{uuid}")
   public ResponseEntity<UserResponseDto> getUser(@PathVariable final UUID uuid) {
      final var user = userGetterPort.getUser(uuid);
      return ResponseEntity.ok(UserRestMapper.toDto(user));
   }

}
