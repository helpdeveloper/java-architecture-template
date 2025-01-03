package br.com.helpdev.sample.adapters.output.db;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.helpdev.sample.adapters.output.db.jpa.UserJpaRepository;
import br.com.helpdev.sample.adapters.output.db.mapper.UserDbMapper;
import br.com.helpdev.sample.core.domain.entities.Address;
import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.vo.Email;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

   @Mock
   private UserJpaRepository userJpaRepository;

   @InjectMocks
   private UserRepository userRepository;

   @Test
   void testSave() {
      final var user = User.of(1L, UUID.randomUUID(), "John Doe", Email.of("john.doe@example.com"), LocalDate.of(1990, 1, 1), null);
      var userEntity = UserDbMapper.toEntity(user);
      when(userJpaRepository.save(any())).thenReturn(userEntity);

      User savedUser = userRepository.save(user);

      assertNotNull(savedUser);
      assertEquals(user.uuid(), savedUser.uuid());
      verify(userJpaRepository).save(any());
   }

   @Test
   void testFindByUuidWithoutAddress() {
      final var user = User.of(1L, UUID.randomUUID(), "John Doe", Email.of("john.doe@example.com"), LocalDate.of(1990, 1, 1), null);
      var userEntity = UserDbMapper.toEntity(user);
      when(userJpaRepository.findByUuid(user.uuid().toString())).thenReturn(Optional.of(userEntity));

      Optional<User> foundUser = userRepository.findByUuid(user.uuid());

      assertTrue(foundUser.isPresent());
      assertEquals(user.uuid(), foundUser.get().uuid());
      assertEquals(user.name(), foundUser.get().name());
      assertEquals(user.email(), foundUser.get().email());
      assertNull(foundUser.get().address());

      verify(userJpaRepository).findByUuid(user.uuid().toString());
   }

   @Test
   void testFindByUuidWithAddress() {
      final var address = Address.of(1L, "Country", "State", "City", "ZipCode", "StreetAddress");
      final var user = User.of(1L, UUID.randomUUID(), "John Doe", Email.of("john.doe@example.com"), LocalDate.of(1990, 1, 1), address);

      var userEntity = UserDbMapper.toEntity(user);
      when(userJpaRepository.findByUuid(user.uuid().toString())).thenReturn(Optional.of(userEntity));

      Optional<User> foundUser = userRepository.findByUuid(user.uuid());

      assertTrue(foundUser.isPresent());
      assertEquals(user.uuid(), foundUser.get().uuid());
      assertEquals(user.address().country(), foundUser.get().address().country());
      assertEquals(user.address().state(), foundUser.get().address().state());
      assertEquals(user.address().city(), foundUser.get().address().city());
      assertEquals(user.address().zipCode(), foundUser.get().address().zipCode());
      assertEquals(user.address().streetAddress(), foundUser.get().address().streetAddress());
      verify(userJpaRepository).findByUuid(user.uuid().toString());
   }
}