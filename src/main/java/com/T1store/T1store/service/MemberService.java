package com.T1store.T1store.service;

import com.T1store.T1store.entity.Member;
import com.T1store.T1store.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository; //
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member); // 데이터베이스에 저장
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        Member findUserId = memberRepository.findByUserId(member.getUserId());
        Member findTel = memberRepository.findByTel(member.getTel());
        if (findMember != null){
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }
        if (findUserId != null){
            throw new IllegalStateException("이미 가입된 ID입니다.");
        }
        if (findTel != null){
            throw new IllegalStateException("이미 가입된 전화번호입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(userId);

        if(member == null){
            throw new UsernameNotFoundException(userId);
        }
        return User.builder()
                .username(member.getUserId())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }


    public Long findMemberIdByPrincipal(Principal principal) {
        if (principal == null) {
            return null; // or throw an exception based on your use case
        }

        String userId = principal.getName(); // Get the username (userId) from the logged in user
        Member member = memberRepository.findByUserId(userId);

        if (member == null) {
            return null; // or throw an exception if the member must exist
        }

        return member.getId();
    }
}
