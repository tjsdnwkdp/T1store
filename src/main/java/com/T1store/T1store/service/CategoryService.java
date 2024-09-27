package com.T1store.T1store.service;

import com.T1store.T1store.constant.Category;
import com.T1store.T1store.entity.Item;
import com.T1store.T1store.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final ItemRepository itemRepository;

    public Page<Item> getItemsByCategory(Category category, Pageable pageable) {
        if (category == Category.SHOP) {
            // SHOP 카테고리의 경우 모든 하위 카테고리를 포함해서 페이징
            List<Category> subCategories = Category.getAllSubCategories();
            return itemRepository.findByCategoryIn(subCategories, pageable);
        } else {
            // 특정 카테고리의 상품들을 페이징해서 반환
            return itemRepository.findByCategory(category, pageable);
        }
    }
}
