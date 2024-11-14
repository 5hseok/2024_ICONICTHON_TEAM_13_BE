package com.prochord.server.service;


import com.prochord.server.domain.member.Professor;
import com.prochord.server.domain.member.Student;
import com.prochord.server.domain.post.Comment;
import com.prochord.server.domain.post.Likes;
import com.prochord.server.domain.post.Post;
import com.prochord.server.domain.post.Scrap;
import com.prochord.server.dto.comment.request.CommentRequest;
import com.prochord.server.dto.comment.response.CommentResponse;
import com.prochord.server.dto.likes.request.LikesRequest;
import com.prochord.server.dto.post.request.PostCreateRequest;
import com.prochord.server.dto.post.request.PostUpdateRequest;
import com.prochord.server.dto.post.response.PostResponse;
import com.prochord.server.dto.scrap.ScrapRequest;
import com.prochord.server.global.jwt.JwtTokenProvider;
import com.prochord.server.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;
    private final LikesRepository likesRepository;
    private final ScrapRepository scrapRepository;
    private final CommentRepository commentRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 게시글 작성
    @Transactional
    public PostResponse createPost(PostCreateRequest request) {
        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new IllegalArgumentException("Professor not found"));
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdDate(new Date())
                .professor(professor)
                .build();
        Post savedPost = postRepository.save(post);
        return PostResponse.fromEntity(savedPost);
    }

    // 모든 게시글 조회
    @Transactional
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return PostResponse.fromEntities(posts);
    }

    //특정 교수님의 게시글 조회
    @Transactional
    public List<PostResponse> getPostsByProfessor(Long professorId) {
        List<Post> posts = postRepository.findByProfessorId(professorId);
        return PostResponse.fromEntities(posts);
    }

    // 특정 게시글의 모든 정보 조회 (게시글, 댓글, 좋아요, 스크랩)
    @Transactional
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<Likes> likes = likesRepository.findByPostId(postId);
        List<Scrap> scraps = scrapRepository.findByPostId(postId);
        return PostResponse.fromEntity(post, comments, likes, scraps);
    }

    // 게시글 수정
    @Transactional
    public PostResponse updatePost(Long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.update(request.getTitle(), request.getContent());
        return PostResponse.fromEntity(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        postRepository.delete(post);
    }

    // 게시글 좋아요(Like) 추가s
    @Transactional
    public void likesPost(Long postId, LikesRequest likesRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Student student = studentRepository.findById(likesRequest.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Likes likes = Likes.builder()
                .post(post)
                .professor(post.getProfessor())
                .student(student)
                .build();
        likesRepository.save(likes);
    }

    // 게시글 스크랩(Scrap) 추가
    @Transactional
    public void scrapPost(Long postId, ScrapRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Scrap scrap = Scrap.builder()
                .post(post)
                .student(student)
                .professor(post.getProfessor())
                .build();
        scrapRepository.save(scrap);
    }

    // 댓글 추가
    @Transactional
    public CommentResponse addComment(Long postId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Comment comment = Comment.builder()
                .post(post)
                .content(request.getContent())
                .createdDate(new Date())
                .student(student)
                .professor(post.getProfessor())
                .build();
        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.fromEntity(savedComment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(Long commentId, String token, CommentRequest request) {
        Long userId = jwtTokenProvider.getUserFromJwt(token);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // 댓글 작성자와 요청한 사용자가 일치하는지 확인
        if (!comment.getStudent().getId().equals(userId)) {
            throw new SecurityException("You are not authorized to update this comment");
        }

        comment.update(request.getContent());
        return CommentResponse.fromEntity(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, String token) {

        // JWT에서 사용자 ID 추출
        Long userId = jwtTokenProvider.getUserFromJwt(token);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // 작성자 일치 여부 확인 (교수 또는 학생)
        if (comment.getStudent() != null && !comment.getStudent().getId().equals(userId) &&
                comment.getProfessor() != null && !comment.getProfessor().getId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this comment");
        }
        commentRepository.delete(comment);
    }

}