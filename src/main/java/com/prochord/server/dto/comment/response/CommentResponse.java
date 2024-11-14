package com.prochord.server.dto.comment.response;

import com.prochord.server.domain.post.Comment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CommentResponse {
    private final Long id;
    private final String content;
    private final String studentName;
    private final String createdDate;

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .studentName(comment.getStudent().getName())
                .createdDate(comment.getCreatedDate().toString())
                .build();
    }
    public static List<CommentResponse> fromEntities(List<Comment> comments){
        return comments.stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
    }

}

