package br.com.helpdev.sample.core.domain.entities;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.UUID;

import br.com.helpdev.sample.core.domain.vo.Email;

public record User(Long id, UUID uuid, String name, Email email, LocalDate birthDate, Address address) {

   public static final int AGE_OF_MAJORITY = 18;

   public User {
      requireNonNull(email, "Email is required");
      requireNonNull(birthDate, "Birth date is required");
      requireNonNull(uuid, "UUID is required");

      if (name == null || name.isBlank()) {
         throw new IllegalArgumentException("Name is required");
      }

      if (birthDate.isAfter(LocalDate.now())) {
         throw new IllegalArgumentException("Birth date is invalid");
      }
   }

   public boolean isMajor() {
      return birthDate.plusYears(AGE_OF_MAJORITY).isBefore(LocalDate.now());
   }

   public static User of(Long id, UUID uuid, String name, Email email, LocalDate birthDate, Address address) {
      return new User(id, uuid, name, email, birthDate, address);
   }

   public static User of(String name, Email email, LocalDate birthDate) {
      return new User(0L, UUID.randomUUID(), name, email, birthDate, null);
   }

   public User withAddress(Address address) {
      return new User(id, uuid, name, email, birthDate, address);
   }

   public User withId(Long id) {
      return new User(id, uuid, name, email, birthDate, address);
   }

}