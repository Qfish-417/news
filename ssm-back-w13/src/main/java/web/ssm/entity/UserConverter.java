package web.ssm.entity;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import web.ssm.dto.UserDTO;

@Mapper
public abstract class UserConverter {
    public static UserConverter INSTANT = Mappers.getMapper(UserConverter.class);

    public abstract UserEntity entityToDto(UserDTO userDTO);
}