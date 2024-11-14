package com.prochord.server.repository;

import com.prochord.server.domain.post.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    List<Scrap> findByPostId(Long postId);
}
