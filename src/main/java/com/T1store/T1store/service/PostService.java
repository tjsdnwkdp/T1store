package com.T1store.T1store.service;

import com.T1store.T1store.dto.PostDto;
import com.T1store.T1store.dto.PostSearchDto;
import com.T1store.T1store.entity.Member;
import com.T1store.T1store.entity.Post;
import com.T1store.T1store.repository.MemberRepository;
import com.T1store.T1store.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Long savePost(PostDto postDto) {
        if (postDto.getMemberId() == null) {
            throw new IllegalArgumentException("Member ID must not be null");
        }
        Member author = memberRepository.findById(postDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("No member found with ID: " + postDto.getMemberId()));

        Post post = postDto.toEntity(author);
        return postRepository.save(post).getId();
    }

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public Post findPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다. ID: " + id));
        incrementViews(id);  // 조회수 증가
        return post;
    }

    public void updatePost(Long id, PostDto postDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        postRepository.save(post);
    }

    private void incrementViews(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + postId));
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
    }

    public Page<Post> getAdminPostPage(PostSearchDto postSearchDto, Pageable pageable) {
        return postRepository.findPosts(postSearchDto, pageable);
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        postRepository.delete(post);
    }

}
