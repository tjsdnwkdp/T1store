package com.T1store.T1store.dto;

import com.T1store.T1store.constant.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductItemDto {
    private Long id;
    private String itemNm;
    private String imgUrl;
    private Integer price;
    private Category category;

    @QueryProjection //Querydsl 결과 조회 시 ProductItemDto 객체로 바로 오도록 활용
    public ProductItemDto(Long id, String itemNm, String imgUrl, Integer price, Category category){
        this.id = id;
        this.itemNm = itemNm;
        this.imgUrl = imgUrl;
        this.price = price;
        this.category = category;
    }
}
