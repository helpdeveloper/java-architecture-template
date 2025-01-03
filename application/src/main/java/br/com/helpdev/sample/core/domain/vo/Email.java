package br.com.helpdev.sample.core.domain.vo;

public record Email(String value) {

   public Email {
      if (value == null || value.isBlank()) {
         throw new IllegalArgumentException("Email is required");
      }
      if (!value.contains("@")) {
         throw new IllegalArgumentException("Email is invalid");
      }
   }

   public static Email of(String value) {
      return new Email(value);
   }
}
