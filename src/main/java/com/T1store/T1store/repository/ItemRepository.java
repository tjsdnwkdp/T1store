package com.T1store.T1store.repository;

import com.T1store.T1store.constant.Category;
import com.T1store.T1store.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    //Repository 인터페이스에 간단한 네이밍을 이용하여 메소드 작성시 쿼리문 실행 가능
    // find + (엔티티) + By + 변수이름
    List<Item> findByPriceLessThan(Integer price);
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
    @Query("select i from Item i where i.itemNm like %:itemNm% order by i.price desc")
    List<Item> findByItemNm(@Param("itemNm")String itemNm);
    @Query(value = "select * from item i where i.item_Nm like %:itemNm% order by i.price desc"
            ,nativeQuery = true)
    List<Item> findByItemNmNative(@Param("itemNm")String itemNm);
    List<Item> findByCategory(Category category);
    Page<Item> findByCategory(Category category, Pageable pageable);

    Page<Item> findByCategoryIn(List<Category> categories, Pageable pageable);
    Page<Item> findByCategoryAndItemNmContaining(Category category, String itemNm, Pageable pageable);
    Page<Item> findByItemNmContaining(String itemNm, Pageable pageable);

}
