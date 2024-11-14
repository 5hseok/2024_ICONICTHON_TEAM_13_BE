package com.prochord.server.dto.post.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostCreateRequest {
    private Long professorId;
    private String title;
    private String content;
}