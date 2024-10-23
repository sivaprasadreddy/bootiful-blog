package com.sivalabs.blog.domain.mappers;

import com.sivalabs.blog.domain.entities.PostEntity;
import com.sivalabs.blog.domain.models.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "createdByUserName", source = "createdBy.name")
    Post toPost(PostEntity entity);
}
