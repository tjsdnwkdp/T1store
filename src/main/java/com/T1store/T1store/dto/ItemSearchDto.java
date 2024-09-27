package com.T1store.T1store.dto;

import com.T1store.T1store.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
    private String searchDateType; // 조회날짜
    private ItemSellStatus searchSellStatus; // 상태
    private String searchBy; // 조회유형
    private String searchQuery = ""; //검색 단어
}
