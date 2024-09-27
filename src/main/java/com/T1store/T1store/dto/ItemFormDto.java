package com.T1store.T1store.dto;


import com.T1store.T1store.constant.Category;
import com.T1store.T1store.constant.ItemSellStatus;
import com.T1store.T1store.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber = 100;

    private ItemSellStatus itemSellStatus;

    private Category category; // 카테고리 필드


    //-------------------------------------------------------
    //ItemImg
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();//상품이미지 정보

    private List<Long> itemImgIds = new ArrayList<>();//상품 이미지 아이디

    //-------------------------------------------------------
    //ModelMapper
    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        //ItemFormDto -> Item 연결
        return modelMapper.map(this, Item.class);
    }
    public static ItemFormDto of(Item item){
        //Item -> ItemFormDto 연결
        return modelMapper.map(item, ItemFormDto.class);
    }
}
