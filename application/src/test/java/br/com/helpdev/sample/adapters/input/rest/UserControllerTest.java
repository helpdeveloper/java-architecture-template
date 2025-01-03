package br.com.helpdev.sample.adapters.input.rest;

import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import br.com.helpdev.sample.adapters.input.rest.dto.UserRequestDto;
import br.com.helpdev.sample.adapters.input.rest.dto.UserResponseDto;
import br.com.helpdev.sample.adapters.input.rest.mapper.UserRestMapper;
import br.com.helpdev.sample.core.domain.entities.Address;
import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.vo.Email;
import br.com.helpdev.sample.core.ports.input.UserCreatorPort;
import br.com.helpdev.sample.core.ports.input.UserGetterPort;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

   @Mock
   private UserCreatorPort userCreatorPort;

   @Mock
   private UserGetterPort userGetterPort;

   @InjectMocks
   private UserController userController;

   @Test
   void createUser_success() {
      final var userRequestDto = new UserRequestDto("John Doe", "john.doe@example.com", LocalDate.of(1990, 1, 1));
      final var user = User.of(1L, UUID.randomUUID(), "John Doe", Email.of("john.doe@example.com"), LocalDate.of(1990, 1, 1), null);

      when(userCreatorPort.createUser(any(User.class))).thenReturn(user);

      ResponseEntity<?> response = userController.createUser(userRequestDto);

      assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
      assertEquals(URI.create("/user/" + user.uuid()), response.getHeaders().getLocation());
      verify(userCreatorPort).createUser(any(User.class));
   }

   @Test
   void getUser_withAddress() {
      final var uuid = UUID.randomUUID();
      final var user = User.of(1L, uuid, "John Doe", Email.of("john.doe@example.com"), LocalDate.of(1990, 1, 1),
            Address.of("Street", "City", "State", "Country", "ZipCode"));

      when(userGetterPort.getUser(uuid)).thenReturn(user);

      ResponseEntity<?> response = userController.getUser(uuid);

      assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
      assertEquals(UserRestMapper.toDto(user), response.getBody());
      assertEquals(user.address().streetAddress(), ((UserResponseDto) response.getBody()).address().street());
      verify(userGetterPort).getUser(uuid);
   }

   @Test
   void getUser_withoutAddress() {
      final var uuid = UUID.randomUUID();
      final var user = User.of(1L, uuid, "John Doe", Email.of("john.doe@example.com"), LocalDate.of(1990, 1, 1), null);

      when(userGetterPort.getUser(uuid)).thenReturn(user);

      ResponseEntity<?> response = userController.getUser(uuid);

      assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
      assertEquals(UserRestMapper.toDto(user), response.getBody());
      assertNull(((UserResponseDto) response.getBody()).address());
      verify(userGetterPort).getUser(uuid);
   }
}