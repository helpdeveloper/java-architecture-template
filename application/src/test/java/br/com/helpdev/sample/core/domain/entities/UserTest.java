package br.com.helpdev.sample.core.domain.entities;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.helpdev.sample.core.domain.vo.Email;

@ExtendWith(MockitoExtension.class)
class UserTest {

   @Test
   void shouldCreateUser_whenAllFieldsAreValid() {
      assertDoesNotThrow(() -> new User(1L, UUID.randomUUID(), "John Doe", Email.of("john.doe@example.com"), LocalDate.of(2000, 1, 1), null));
   }

   @Test
   void shouldThrowException_whenEmailIsNull() {
      assertThrows(NullPointerException.class, () -> new User(1L, UUID.randomUUID(), "John Doe", null, LocalDate.of(2000, 1, 1), null));
   }

   @Test
   void shouldThrowException_whenBirthDateIsNull() {
      assertThrows(NullPointerException.class, () -> new User(1L, UUID.randomUUID(), "John Doe", Email.of("john.doe@example.com"), null, null));
   }

   @Test
   void shouldThrowException_whenUUIDIsNull() {
      assertThrows(NullPointerException.class,
            () -> new User(1L, null, "John Doe", Email.of("john.doe@example.com"), LocalDate.of(2000, 1, 1), null));
   }

   @Test
   void shouldThrowException_whenNameIsNull() {
      assertThrows(IllegalArgumentException.class,
            () -> new User(1L, UUID.randomUUID(), null, Email.of("john.doe@example.com"), LocalDate.of(2000, 1, 1), null));
   }

   @Test
   void shouldThrowException_whenNameIsBlank() {
      assertThrows(IllegalArgumentException.class,
            () -> new User(1L, UUID.randomUUID(), "", Email.of("john.doe@example.com"), LocalDate.of(2000, 1, 1), null));
   }

   @Test
   void shouldThrowException_whenBirthDateIsInFuture() {
      assertThrows(IllegalArgumentException.class,
            () -> new User(1L, UUID.randomUUID(), "John Doe", Email.of("john.doe@example.com"), LocalDate.now().plusDays(1), null));
   }

   @Test
   void shouldReturnTrue_whenUserIsMajor() {
      User user = new User(1L, UUID.randomUUID(), "John Doe", Email.of("john.doe@example.com"), LocalDate.of(2000, 1, 1), null);
      assertTrue(user::isMajor);
   }

   @Test
   void shouldReturnFalse_whenUserIsNotMajor() {
      User user = new User(1L, UUID.randomUUID(), "John Doe", Email.of("john.doe@example.com"), LocalDate.now().minusYears(User.AGE_OF_MAJORITY - 1),
            null);
      assertFalse(user::isMajor);
   }
}