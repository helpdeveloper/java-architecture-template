package br.com.helpdev.sample.adapters.input.rest.mapper;

import br.com.helpdev.sample.adapters.input.rest.dto.AddressResponseDto;
import br.com.helpdev.sample.adapters.input.rest.dto.UserRequestDto;
import br.com.helpdev.sample.adapters.input.rest.dto.UserResponseDto;
import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.vo.Email;

public class UserRestMapper {

   private UserRestMapper() {
   }

   public static User toDomain(final UserRequestDto userRequestDto) {
      return User.of(userRequestDto.name(), Email.of(userRequestDto.email()), userRequestDto.birthDate());
   }

   public static UserResponseDto toDto(final User user) {
      return new UserResponseDto(user.name(), user.email().value(), user.birthDate().toString(),
            user.address() == null ? null : new AddressResponseDto(
                  user.address().streetAddress(),
                  user.address().city(),
                  user.address().state(),
                  user.address().country(),
                  user.address().zipCode()
            ));
   }
}
