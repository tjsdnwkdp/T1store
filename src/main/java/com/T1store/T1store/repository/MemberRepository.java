package com.T1store.T1store.repository;

import com.T1store.T1store.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    Member findByUserId(String userId);
    Member findByTel(String tel);
}
