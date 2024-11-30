package com.sivalabs.blog.mappers;

import com.sivalabs.blog.domain.Comment;
import com.sivalabs.blog.domain.PostProjection;
import com.sivalabs.blog.dtos.CommentDto;
import com.sivalabs.blog.dtos.PostDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    @Mapping(target = "createdByUserName", source = "createdBy.name")
    PostDto toPostDto(PostProjection p);

    CommentDto toCommentDto(Comment cmd);
}
