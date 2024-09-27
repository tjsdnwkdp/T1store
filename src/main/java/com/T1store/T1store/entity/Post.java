package com.T1store.T1store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false, length = 255) // 현재 길이, 필요 시 수정
    private String title;

    @Lob // 큰 텍스트 데이터를 저장하기 위한 설정
    @Column(nullable = false, columnDefinition = "TEXT") // Hibernate에서 데이터베이스 타입을 명시적으로 설정
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member author;  // 작성자 정보

    @Column(nullable = false)
    private int views = 0; // 조회수, 기본값 0
}
