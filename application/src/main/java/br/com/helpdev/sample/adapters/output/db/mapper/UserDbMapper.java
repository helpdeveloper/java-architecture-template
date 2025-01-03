package br.com.helpdev.sample.adapters.output.db.mapper;

import java.util.UUID;

import br.com.helpdev.sample.adapters.output.db.jpa.entities.UserEntity;
import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.vo.Email;

public class UserDbMapper {

   private UserDbMapper() {
   }

   public static UserEntity toEntity(final User user) {
      return new UserEntity(user.id(), user.uuid().toString(), user.name(), user.email().value(), user.birthDate(),
            user.address() == null ? null : AddressDbMapper.toEntity(user.id(), user.address()));
   }

   public static User toDomain(UserEntity userEntity) {
      return User.of(userEntity.getId(), UUID.fromString(userEntity.getUuid()), userEntity.getName(), Email.of(userEntity.getEmail()),
            userEntity.getBirthDate(), userEntity.getAddress() == null ? null : AddressDbMapper.toDomain(userEntity.getAddress()));
   }
}
