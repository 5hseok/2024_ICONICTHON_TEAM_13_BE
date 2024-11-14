package com.prochord.server.dto.post.response;

import com.prochord.server.domain.post.Comment;
import com.prochord.server.domain.post.Likes;
import com.prochord.server.domain.post.Post;
import com.prochord.server.domain.post.Scrap;
import com.prochord.server.dto.comment.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String professorName;
    private Integer likesCount;
    private Integer scrapCount;
    private List<CommentResponse> comments;

    public static PostResponse fromEntity(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .professorName(post.getProfessor().getName())
                .likesCount(post.getLikes().size())
                .scrapCount(post.getScraps().size())
                .build();
    }

    // List<Post>를 List<PostResponse>로 변환하는 메서드
    public static List<PostResponse> fromEntities(List<Post> posts) {
        return posts.stream()
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public static PostResponse fromEntity(Post post, List<Comment> comments, List<Likes> likes, List<Scrap> scraps) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .professorName(post.getProfessor().getName())
                .likesCount(likes.size())
                .scrapCount(scraps.size())
                .comments(CommentResponse.fromEntities(comments))
                .build();
    }
}
