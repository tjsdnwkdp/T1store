package com.T1store.T1store.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearchDto {

    private String searchBy; // 검색 기준 (예: 제목, 작성자)
    private String searchQuery = ""; // 검색어
    private String searchDateType; // 날짜 필터링 (예: 전체, 1일, 1주, 1개월, 6개월)

    // 추가로 필요하면 검색 조건 필드를 여기에 추가할 수 있습니다.
}
