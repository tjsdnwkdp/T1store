package com.T1store.T1store.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {
    private Long cartItemId; // 장바구니 상품 아이디
    private String itemNm;
    private int price;
    private int count;
    private String imgUrl;

    public CartDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl){
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.imgUrl = imgUrl;
        this.count = count;
    }
}
