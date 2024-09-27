package com.T1store.T1store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IamportResponse<T> {
    private int code;           // 응답 코드 (0: 성공, 그 외: 오류 코드)
    private String message;     // 응답 메시지 (성공 또는 오류 메시지)
    private T response;         // 실제 데이터 (예: Payment 객체)

    // 성공 응답 생성
    public static <T> IamportResponse<T> success(T response) {
        return new IamportResponse<>(0, "Success", response);
    }

    // 실패 응답 생성
    public static <T> IamportResponse<T> failure(int code, String message) {
        return new IamportResponse<>(code, message, null);
    }
}
