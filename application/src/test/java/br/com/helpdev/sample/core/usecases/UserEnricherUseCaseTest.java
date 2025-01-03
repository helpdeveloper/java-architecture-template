package br.com.helpdev.sample.core.usecases;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.helpdev.sample.core.domain.entities.Address;
import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.exceptions.UserNotFoundException;
import br.com.helpdev.sample.core.domain.vo.Email;
import br.com.helpdev.sample.core.ports.output.AddressClientPort;
import br.com.helpdev.sample.core.ports.output.AddressRepositoryPort;
import br.com.helpdev.sample.core.ports.output.UserEventDispatcherPort;
import br.com.helpdev.sample.core.ports.output.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
class UserEnricherUseCaseTest {

   @Mock
   private UserRepositoryPort userRepositoryPort;

   @Mock
   private UserEventDispatcherPort userEventDispatcherPort;

   @Mock
   private AddressClientPort addressClientPort;

   @Mock
   private AddressRepositoryPort addressRepositoryPort;

   @InjectMocks
   private UserEnricherUseCase userEnricherUseCase;

   private UUID userUuid;

   private User user;

   private Address address;

   @BeforeEach
   void setUp() {
      userUuid = UUID.randomUUID();
      user = new User(1L, userUuid, "John Doe", Email.of("john.doe@example.com"), LocalDate.of(2000, 1, 1), null);
      address = new Address(1L, "Country", "State", "City", "12345", "Street Address");
   }

   @Test
   void enrichUser_shouldThrowException_whenUserNotFound() {
      when(userRepositoryPort.findByUuid(userUuid)).thenReturn(Optional.empty());

      assertThrows(UserNotFoundException.class, () -> userEnricherUseCase.enrichUser(userUuid));
   }

   @Test
   void enrichUser_shouldEnrichUser_whenUserFound() {
      when(userRepositoryPort.findByUuid(userUuid)).thenReturn(Optional.of(user));
      when(addressClientPort.findUserAddress(user)).thenReturn(address);
      when(addressRepositoryPort.save(user, address)).thenReturn(address);

      userEnricherUseCase.enrichUser(userUuid);

      verify(userRepositoryPort).findByUuid(userUuid);
      verify(addressClientPort).findUserAddress(user);
      verify(addressRepositoryPort).save(user, address);
      verify(userEventDispatcherPort).sendUserAddressUpdatedEvent(any(User.class));
   }
}