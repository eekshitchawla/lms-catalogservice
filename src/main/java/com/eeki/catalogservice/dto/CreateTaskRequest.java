package com.eeki.catalogservice.dto;

import lombok.Builder;

@Builder
public record CreateTaskRequest(
        String title,
        String description,
        Long userId,
        Long courseId
) {
}
