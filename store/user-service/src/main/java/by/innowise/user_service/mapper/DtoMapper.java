package by.innowise.user_service.mapper;

import by.innowise.user_service.model.dto.CardInfoDto;
import by.innowise.user_service.model.dto.UserDto;
import by.innowise.user_service.model.entity.CardInfo;
import by.innowise.user_service.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DtoMapper {
  UserDto toUserDto(User user);
  CardInfoDto toCardInfoDto(CardInfo cardInfo);
}
