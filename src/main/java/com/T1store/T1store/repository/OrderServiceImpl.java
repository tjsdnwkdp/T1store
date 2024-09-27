package com.T1store.T1store.repository;


import com.T1store.T1store.constant.PaymentStatus;
import com.T1store.T1store.entity.Member;
import com.T1store.T1store.entity.Order;
import com.T1store.T1store.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor

public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Order autoOrder(Member member) {

        // 임시 결제내역 생성
        Payment payment = Payment.builder()
                .price(1000)
                .status(PaymentStatus.READY)
                .build();

        paymentRepository.save(payment);

        // 주문 생성
        Order order = Order.builder()
                .member(member)
                .orderPrice(1000)
                .itemNm("1달러샵 상품")
                .orderUid(UUID.randomUUID().toString())
                .payment(payment)
                .build();

        return orderRepository.save(order);
    }

}
