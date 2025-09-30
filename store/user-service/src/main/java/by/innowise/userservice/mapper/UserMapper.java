package by.innowise.userservice.mapper;

import by.innowise.userservice.model.dto.UserCreationDto;
import by.innowise.userservice.model.dto.UserDto;
import by.innowise.userservice.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

  @Mapping(target = "deleted", ignore = true)
  UserDto toUserDto(User user);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  @Mapping(target = "cardInfos", ignore = true)
  User toUser(UserCreationDto userCreationDto);

  @Mapping(target = "deleted", ignore = true)
  User toUser(UserDto userDto);
}
