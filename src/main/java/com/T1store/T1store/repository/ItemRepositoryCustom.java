package com.T1store.T1store.repository;

import com.T1store.T1store.constant.Category;
import com.T1store.T1store.dto.ItemSearchDto;
import com.T1store.T1store.dto.ProductItemDto;
import com.T1store.T1store.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<ProductItemDto> getProductItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
