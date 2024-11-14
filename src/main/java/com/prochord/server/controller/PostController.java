package com.prochord.server.controller;

import com.prochord.server.dto.comment.request.CommentRequest;
import com.prochord.server.dto.comment.response.CommentResponse;
import com.prochord.server.dto.likes.request.LikesRequest;
import com.prochord.server.dto.post.request.PostCreateRequest;
import com.prochord.server.dto.post.request.PostUpdateRequest;
import com.prochord.server.dto.post.response.PostResponse;
import com.prochord.server.dto.scrap.ScrapRequest;
import com.prochord.server.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

/**
 * 교수님이 포스트를 작성, 수정, 삭제할 수 있는 PostController
 * 학생은 포스트를 Scrap하거나 Like(공감)하거나, Comment를 달 수 있는 기능을 제공하는 컨트롤러와 서비스 코드입니다.
 */

@RestController
@RequestMapping("/api/post")
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 모든 게시글 조회
    @GetMapping("/all")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> postResponses = postService.getAllPosts();
        return ResponseEntity.ok(postResponses);
    }

    //특정 교수님의 게시글 조회
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<PostResponse>> getPostsByProfessor(@PathVariable Long professorId) {
        List<PostResponse> postResponses = postService.getPostsByProfessor(professorId);
        return ResponseEntity.ok(postResponses);
    }

    //특정 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse postResponse = postService.getPost(postId);
        return ResponseEntity.ok(postResponse);
    }

    // 게시글 작성
    @PostMapping("")
    public ResponseEntity<PostResponse> createPost(@RequestBody PostCreateRequest postCreateRequest) {
        PostResponse postResponse = postService.createPost(postCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest) {
        PostResponse postResponse = postService.updatePost(postId, postUpdateRequest);
        return ResponseEntity.ok(postResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("Post deleted successfully");
    }

    // 게시글 좋아요(Like) 추가
    @PostMapping("/likes/{postId}")
    public ResponseEntity<String> likesPost(@PathVariable Long postId, @RequestBody LikesRequest likesRequest) {
        log.info("postId : " + postId);
        postService.likesPost(postId, likesRequest);
        return ResponseEntity.ok("Post liked successfully");
    }

    // 게시글 스크랩(Scrap) 추가
    @PostMapping("/scrap/{postId}")
    public ResponseEntity<String> scrapPost(@PathVariable Long postId, @RequestBody ScrapRequest scrapRequest) {
        postService.scrapPost(postId, scrapRequest);
        return ResponseEntity.ok("Post scrapped successfully");
    }

    // 게시글에 댓글 추가
    @PostMapping("/comment/{postId}")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = postService.addComment(postId, commentRequest);
        return ResponseEntity.ok(commentResponse);
    }

    //댓글 수정
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, @RequestHeader("Authorization") String token, @RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = postService.updateComment(commentId, token, commentRequest);
        return ResponseEntity.ok(commentResponse);
    }

    //댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @RequestHeader("Authorization") String token) {
        postService.deleteComment(commentId, token);
        return "Comment deleted successfully";
    }
}
