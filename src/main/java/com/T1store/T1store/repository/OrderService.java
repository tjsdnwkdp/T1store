package com.T1store.T1store.repository;

import com.T1store.T1store.entity.Member;
import com.T1store.T1store.entity.Order;

public interface OrderService {
    Order autoOrder(Member member);
}
