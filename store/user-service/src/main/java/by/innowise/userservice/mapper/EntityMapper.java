package by.innowise.userservice.mapper;

import by.innowise.userservice.model.dto.CardInfoDto;
import by.innowise.userservice.model.dto.UserDto;
import by.innowise.userservice.model.entity.CardInfo;
import by.innowise.userservice.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntityMapper {
  User toUser(UserDto userDto);

  @Mapping(source = "userDto", target = "user")
  CardInfo toCardInfo(CardInfoDto cardInfoDto);
}
