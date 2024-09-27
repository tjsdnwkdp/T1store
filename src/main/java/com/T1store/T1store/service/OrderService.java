package com.T1store.T1store.service;

import com.T1store.T1store.constant.PaymentStatus;
import com.T1store.T1store.dto.MemberFormDto;
import com.T1store.T1store.dto.OrderDto;
import com.T1store.T1store.dto.OrderHistDto;
import com.T1store.T1store.dto.OrderItemDto;
import com.T1store.T1store.entity.*;
import com.T1store.T1store.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;
    private final PaymentRepository paymentRepository;

    public Long order(OrderDto orderDto, String userId){

        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByUserId(userId);
        if (member == null) {
            System.out.println("^^^^^^^^^^^" + userId);
            throw new EntityNotFoundException("Member not found for userId: " + userId);
        }

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);
        Payment payment = new Payment();

        Order order = Order.createOrder(member, orderItemList, payment);
        order.setOrderPrice(order.getOrderPrice());
        System.out.println("오더프라이스"+order.getOrderPrice());
        System.out.println("토탈프라이스"+order.getTotalPrice());
        payment.setPrice(order.getTotalPrice());
        payment.setPaymentUid(order.getOrderUid());
        paymentRepository.save(payment);
        orderRepository.save(order);

        payment.setItemNm(order.getItemNmList());
        order.setItemNm(order.getItemNmList());

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String userId, Pageable pageable){
        List<Order> orders = orderRepository.findOrders(userId, pageable);
        Long totalCount = orderRepository.countOrder(userId);
        List<OrderHistDto> orderHistDtos = new ArrayList<>();
        //Order -> OrderHistDto
        //OrderItem -> OrderItemDto
        for(Order order : orders){
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem : orderItems){
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(),
                        "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String userId){
        Member curMember = memberRepository.findByUserId(userId);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.getUserId(), savedMember.getUserId())){
            return false;
        }
        return true;
    }
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }
    public Long orders(List<OrderDto> orderDtoList, String userId) {
        Member member = memberRepository.findByUserId(userId);

        List<OrderItem> orderItemList = new ArrayList<>();

        for(OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
            System.out.println(";;;;;;;;;;"+item.getItemNm());
            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }
        Payment payment = new Payment();
        Order order = Order.createOrder(member, orderItemList, payment);
        payment.setPaymentUid(order.getOrderUid());
        payment.setPaymentUid(order.getOrderUid());
        payment.setPrice(order.getTotalPrice());
        payment.setStatus(PaymentStatus.OK);
        paymentRepository.save(payment);
        orderRepository.save(order);

        payment.setItemNm(order.getItemNmList());
        order.setItemNm(order.getItemNmList());

        return order.getId();
    }


}
