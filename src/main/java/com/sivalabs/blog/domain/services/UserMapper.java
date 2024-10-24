package com.sivalabs.blog.domain.services;

import com.sivalabs.blog.domain.entities.UserEntity;
import com.sivalabs.blog.domain.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserEntity entity);
}
