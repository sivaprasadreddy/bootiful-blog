package com.sivalabs.blog.domain;

import java.util.List;
import org.springframework.data.domain.Page;

public record PagedResult<T>(
        List<T> data,
        int currentPageNo,
        int totalPages,
        long totalElements,
        boolean hasNextPage,
        boolean hasPreviousPage) {

    public static <T> PagedResult<T> from(Page<T> page) {
        return new PagedResult<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getTotalPages(),
                page.getTotalElements(),
                page.hasNext(),
                page.hasPrevious());
    }
}
