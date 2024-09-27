package com.T1store.T1store.dto;

import com.T1store.T1store.entity.Member;
import com.T1store.T1store.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private Long memberId; // 작성자의 ID

    // DTO를 Entity로 변환
    public Post toEntity(Member author) {
        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author); // 작성자 정보는 서비스 계층에서 조회한 Member 엔티티를 사용
        return post;
    }
}
