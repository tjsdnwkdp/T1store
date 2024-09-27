package com.T1store.T1store.controller;

import com.T1store.T1store.constant.Category;
import com.T1store.T1store.dto.ItemSearchDto;
import com.T1store.T1store.dto.ProductItemDto;
import com.T1store.T1store.entity.Item;
import com.T1store.T1store.service.ItemService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class ProductController {
    private final ItemService itemService;

    @GetMapping("/{categoryName}/{id}")
    public String handleCategory(@PathVariable String categoryName, @PathVariable int id, Model model, ItemSearchDto itemSearchDto,
                                 Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);
        if (itemSearchDto.getSearchQuery() == null){
            itemSearchDto.setSearchQuery("");
        }

        try {
            Category category = Category.fromPath(categoryName);

            Page<ProductItemDto> items;

            if (itemSearchDto.getSearchQuery() != null && !itemSearchDto.getSearchQuery().isEmpty()) {
                if (category == Category.SHOP) {
                    // 샵 페이지에서 전체 검색
                    items = itemService.searchItemsInAllCategories(itemSearchDto.getSearchQuery(), pageable);
                } else {
                    // 특정 카테고리 내에서 검색
                    items = itemService.searchItemsInCategory(category, itemSearchDto.getSearchQuery(), pageable);
                }
            } else {
                // 검색어가 없으면 카테고리 내 모든 아이템을 가져옴
                items = itemService.getItemsByCategory(category, pageable);
            }

            model.addAttribute("categoryName");
            model.addAttribute("id", id);
            model.addAttribute("items", items);
            model.addAttribute("itemSearchDto", itemSearchDto);
            model.addAttribute("page", items);
            model.addAttribute("maxPage", 5);  // 한 번에 보여줄 최대 페이지 수

            return "product/" + categoryName.toLowerCase();
        } catch (IllegalArgumentException e) {
            return "error";
        }
    }

}
