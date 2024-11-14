package com.prochord.server.dto.comment.response;

import com.prochord.server.domain.post.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {
    private final Long id;
    private final String content;
    private final String studentName;

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .studentName(comment.getStudent().getName())
                .build();
    }
}

