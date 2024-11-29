package com.sivalabs.blog.domain.internal;

import com.sivalabs.blog.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface UserMapper {
    User toUser(UserEntity entity);
}
