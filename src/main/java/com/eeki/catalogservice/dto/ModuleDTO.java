package com.eeki.catalogservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleDTO {

    private Long id;
    private String title;
    private String contentUrl;
    private Integer orderIndex;
}
