package com.T1store.T1store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") //외래키
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") //외래키
    private Order order;
    private String itemNm;
    private int orderPrice;
    private int count;
//    private LocalDateTime regTime;
//    private LocalDateTime updateTime;


    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setItemNm(item.getItemNm());
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        item.removeStock(count);
        return orderItem;
    }
    public int getTotalPrice(){
        return orderPrice*count;
    }

    public void cancel(){
        this.getItem().addStock(count);
    }
}
