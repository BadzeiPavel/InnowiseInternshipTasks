package by.innowise.userservice.mapper;

import by.innowise.userservice.model.dto.CardInfoDto;
import by.innowise.userservice.model.dto.UserDto;
import by.innowise.userservice.model.entity.CardInfo;
import by.innowise.userservice.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DtoMapper {
  UserDto toUserDto(User user);
  CardInfoDto toCardInfoDto(CardInfo cardInfo);
}
