package com.T1store.T1store.dto;

import com.T1store.T1store.constant.OrderStatus;
import com.T1store.T1store.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {
    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public OrderHistDto(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MMydd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }
    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }
}
