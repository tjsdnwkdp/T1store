package com.T1store.T1store.dto;

import com.T1store.T1store.constant.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberFormDto {
    @NotBlank(message = "ID는 필수 입력 값입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 20, message = "비밀번호는 8자 이상, 20자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String confirmPassword;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String email;

    @NotEmpty (message = "우편번호는 필수 입력 값입니다.")
    private String zipcode;

    @NotEmpty (message ="도로명 주소는 필수 입력 값입니다.")
    private String streetAdr;

    @NotEmpty(message = "상세 주소는 필수 입력 값입니다.")
    private String detailAdr;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String tel;

    private Provider provider; // 소셜 로그인 공급자
    private String providerId; // 소셜 로그인 사용자 식별자
}
