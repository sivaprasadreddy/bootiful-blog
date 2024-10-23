package com.sivalabs.blog.domain.mappers;

import com.sivalabs.blog.domain.entities.CommentEntity;
import com.sivalabs.blog.domain.models.CreateCommentCmd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CommentEntity toCommentEntity(CreateCommentCmd cmd);
}
