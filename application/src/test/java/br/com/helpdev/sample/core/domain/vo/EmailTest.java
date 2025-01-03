package br.com.helpdev.sample.core.domain.vo;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailTest {

    @Test
    void shouldCreateEmail_whenValueIsValid() {
        assertDoesNotThrow(() -> new Email("test@example.com"));
    }

    @Test
    void shouldThrowException_whenValueIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Email(null));
    }

    @Test
    void shouldThrowException_whenValueIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Email(""));
    }

    @Test
    void shouldThrowException_whenValueDoesNotContainAtSymbol() {
        assertThrows(IllegalArgumentException.class, () -> new Email("invalidemail.com"));
    }
}