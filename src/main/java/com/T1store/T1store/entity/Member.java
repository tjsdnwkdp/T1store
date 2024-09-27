package com.T1store.T1store.entity;

import com.T1store.T1store.constant.Provider;
import com.T1store.T1store.constant.Role;
import com.T1store.T1store.dto.MemberFormDto;
import jakarta.mail.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table
@Getter
@Setter
@ToString
public class Member extends BaseEntity{
    //기본키
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = true)
    private String password;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String nickname;

    private String picture;

    private Address address;

    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Provider provider; // "google", "facebook" 등. 일반 로그인은 null


    @Column(nullable = false)
    private boolean enabled; // 계정 활성화 여부

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member() {

    }

    @Getter
    @Setter
    @Embeddable
    public static class Address{
        private String zipcode;
        private String streetAdr;
        private String detailAdr;

        protected Address(){

        }

        public Address(String zipcode, String streetAdr, String detailAdr){
            this.zipcode = zipcode;
            this.streetAdr = streetAdr;
            this.detailAdr = detailAdr;
        }
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();

        Member.Address address = new Member.Address(memberFormDto.getZipcode(), memberFormDto.getStreetAdr(), memberFormDto.getDetailAdr());

        member.setUserId(memberFormDto.getUserId());
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(address);
        member.setTel(memberFormDto.getTel());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN); // 기본 역할을 ADMIN으로 설정

        // 일반 로그인의 경우
        member.setProvider(Provider.LOCAL);

        return member;
    }
    //소셜멤버회원가입시에만 사용되는 생성자
    public Member(String name, String email, String picture, Provider provider, String providerId, String tel, Address address){

        address = new Member.Address("없음","없음","없음");

        this.userId = providerId; // userId를 생성
        this.nickname = name;
        this.email = email;
        this.picture = picture;
        this.provider = provider;
        this.enabled = true; // 계정 활성화 여부
        this.role = Role.USER; // 기본 역할 설정
        this.tel = "없음";
        this.address = address;
    }

    public Member update(String name, String email, String picture, Provider provider){
        this.nickname = name;
        this.email = email;
        this.picture = picture;
        this.provider = provider;
        return this;
    }

}
