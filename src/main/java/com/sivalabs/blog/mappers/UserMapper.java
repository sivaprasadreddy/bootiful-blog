package com.sivalabs.blog.mappers;

import com.sivalabs.blog.domain.User;
import com.sivalabs.blog.dtos.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUser(User entity);
}
