package com.T1store.T1store.controller;

import com.T1store.T1store.dto.ItemSearchDto;
import com.T1store.T1store.dto.PostDto;
import com.T1store.T1store.dto.PostSearchDto;
import com.T1store.T1store.entity.Item;
import com.T1store.T1store.entity.Post;
import com.T1store.T1store.service.MemberService;
import com.T1store.T1store.service.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       PostSearchDto postSearchDto) {
        Pageable pageable = PageRequest.of(page, 10); // 기본값 페이지 0, 페이지당 10개
        Page<Post> posts = postService.getAdminPostPage(postSearchDto, pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("postSearchDto", postSearchDto);
        model.addAttribute("maxPage", 5); // 한 번에 표시할 최대 페이지 수
        model.addAttribute("hideSearchBar", true);
        return "post/postList";
    }



    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("hideSearchBar", true);
        return "post/postForm";
    }

    @PostMapping("/add")
    public String savePost(PostDto postDto, Principal principal) {
        Long memberId = memberService.findMemberIdByPrincipal(principal);
        postDto.setMemberId(memberId);
        postService.savePost(postDto);

        return "redirect:/posts";
    }

    // 경로 수정
    @GetMapping("/{id}")
    public String getPostDetail(@PathVariable Long id, Model model) {
        Post post = postService.findPostById(id);
        if (post == null) {
            return "redirect:/error"; // 적절한 에러 페이지로 리다이렉션
        }
        model.addAttribute("hideSearchBar", true);
        model.addAttribute("post", post);
        return "post/postDetail";
    }

    @GetMapping("/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.findPostById(id);
        if (post == null) {
            return "redirect:/error"; // 적절한 에러 페이지로 리다이렉션
        }
        model.addAttribute("post", post);
        model.addAttribute("hideSearchBar", true);
        return "post/postEditForm"; // 게시물 수정 폼 페이지
    }

    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable Long id, PostDto postDto) {
        postService.updatePost(id, postDto);  // postService 내부에서 게시물 수정 로직 처리
        return "redirect:/posts";  // 수정 후 상세 페이지로 리다이렉션
    }


    // PostController.java
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);  // 게시물 삭제 로직 호출
        return ResponseEntity.ok().build();  // 삭제 성공 시 HTTP 200 응답
    }

}
