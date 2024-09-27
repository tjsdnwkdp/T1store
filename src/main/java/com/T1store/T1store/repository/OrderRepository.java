package com.T1store.T1store.repository;

import com.T1store.T1store.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("select o from Order o where o.member.userId = :userId order by o.orderDate desc")
    List<Order> findOrders(@Param("userId") String email, Pageable pageable);

    @Query("select count(o) from Order o where o.member.userId = :userId")
    Long countOrder(@Param("userId") String userId);

    @Query("select o from Order o" +
            " left join fetch o.payment p" +
            " left join fetch o.member m" +
            " where o.id = :id")
    Optional<Order> findOrderAndPaymentAndMember(Long id);

    @Query("select o from Order o" +
            " left join fetch o.payment p" +
            " where o.orderUid = :orderUid")
    Optional<Order> findOrderAndPayment(String orderUid);
}
