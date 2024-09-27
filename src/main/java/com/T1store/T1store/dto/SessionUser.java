package com.T1store.T1store.dto;

import com.T1store.T1store.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
// 용어는 참 쉽다
// 직렬화 자바 시스템에서 사용할 수 있도록 바이트 스트림 형태로 연속적인 데이터로
// 포멧 변환 기술
// Java Object, Data -> Byte Stream
// 역직렬화 바이트 스트림 -> 자바 Object Data
public class SessionUser implements Serializable {
    private String nickname;
    private String email;
    private String picture;

    public SessionUser(Member member){
        this.nickname = member.getName();
        this.email = member.getEmail();
        this.picture = member.getPicture();
    }
}
