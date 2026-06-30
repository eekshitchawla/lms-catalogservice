package com.eeki.catalogservice.dto;

import lombok.Builder;

@Builder
public record EnrollRequest(Long userId) {
}
