package com.prochord.server.repository;

import com.prochord.server.domain.post.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByPostId(Long postId);
}
