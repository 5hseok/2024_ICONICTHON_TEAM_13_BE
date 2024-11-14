package com.prochord.server.repository;

import com.prochord.server.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByProfessorId(Long professorId);
}