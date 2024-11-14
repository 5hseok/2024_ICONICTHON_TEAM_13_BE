package com.prochord.server.domain.member;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Entity
@Table(name = "student")
public class Student extends Member {
    // 필요한 Student 특화 필드들을 여기에 추가할 수 있습니다.
}
