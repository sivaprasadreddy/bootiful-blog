package com.sivalabs.blog.domain.internal;

import com.sivalabs.blog.domain.models.CreateCommentCmd;
import com.sivalabs.blog.domain.models.Post;
import com.sivalabs.blog.domain.models.PostProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface BlogMapper {
    @Mapping(target = "createdByUserName", source = "createdBy.name")
    Post toPost(PostProjection p);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CommentEntity toCommentEntity(CreateCommentCmd cmd);
}
