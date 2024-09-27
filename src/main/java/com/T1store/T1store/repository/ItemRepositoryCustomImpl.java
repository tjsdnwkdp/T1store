package com.T1store.T1store.repository;

import com.T1store.T1store.constant.ItemSellStatus;
import com.T1store.T1store.dto.ItemSearchDto;
import com.T1store.T1store.dto.ProductItemDto;
import com.T1store.T1store.dto.QProductItemDto;
import com.T1store.T1store.entity.Item;
import com.T1store.T1store.entity.QItem;
import com.T1store.T1store.entity.QItemImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    private JPAQueryFactory queryFactory; //동적쿼리 사용하기 위해 JPAQueryFactory 변수 선언
    //생성자
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em); // JPAQueryFactory 실질적인 객체가 만들어 짐
    }
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ?
                null : QItem.item.itemSellStatus.eq(searchSellStatus);
        //ItemSellStatus null이면 null리턴 null아니면 SELL, SOLD 둘중 하나 리턴
    }
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now(); // 현재시간을 추출해서 변수 대입

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        }
        return QItem.item.regTime.after(dateTime);
        //dateTime을 시간에 맞게 세팅 후 시간에 맞는 등록된 상품이 조회하도록 조건값 반환
    }
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemNm",searchBy)){
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        } else if (StringUtils.equals("createdBy",searchBy)) {
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QueryResults<Item> results = queryFactory.selectFrom(QItem.item).
                where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }
    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%"+searchQuery+"%");
    }

    @Override
    public Page<ProductItemDto> getProductItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<ProductItemDto> results = queryFactory.select(new QProductItemDto(
                item.id,
                item.itemNm,
                itemImg.imgUrl,
                item.price,
                item.category))
                .from(itemImg)
                .join(itemImg.item,item)
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<ProductItemDto> content = results.getResults();
        long total = results.getTotal();
        //QShopItemDto @QueryProjection 을 허용하면 DTO로 바로 조회 가능
        return new PageImpl<>(content, pageable, total);
    }
}
