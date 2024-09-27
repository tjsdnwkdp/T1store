package com.T1store.T1store.entity;

import com.T1store.T1store.constant.Category;
import com.T1store.T1store.constant.ItemSellStatus;
import com.T1store.T1store.dto.ItemFormDto;
import com.T1store.T1store.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id; // 상품코드

    @Column(nullable = false,length = 255)
    private String itemNm; // 상품명

    @Column(name = "price", nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private Integer stockNumber; // 수량

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

//    private LocalDateTime regTime; // 등록 시간
//
//    private LocalDateTime updateTime; // 수정 시간

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "member_item",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Member> member;

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
        this.category = itemFormDto.getCategory(); //
    }
    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량: "+this.stockNumber+")");
        }
        this.stockNumber = restStock;
    }
    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }
}

