package com.prochord.server.domain.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prochord.server.domain.member.Professor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "post")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "createdDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    // 좋아요 목록 추가
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();

    // 스크랩 목록 추가
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scrap> scraps = new ArrayList<>();

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public List<Likes> getLikes() {
        if (this.likes == null) {
            this.likes = new ArrayList<>();
        }
        return this.likes;
    }
    public List<Scrap> getScraps() {
        if (this.scraps == null) {
            this.scraps = new ArrayList<>();
        }
        return this.scraps;
    }

}