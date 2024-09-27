package com.T1store.T1store.controller;

import com.T1store.T1store.constant.Category;
import com.T1store.T1store.dto.ProductItemDto;
import com.T1store.T1store.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ItemService itemService;

    @GetMapping(value = "/")
    public String main(Model model){
        Pageable pageable = PageRequest.of(0, 7); // Fetch the first 7 items
        Page<ProductItemDto> teamKitItems = itemService.getItemsByCategory(Category.TEAM_KIT, pageable);
        model.addAttribute("teamKitItems", teamKitItems);
        model.addAttribute("hideSearchBar", true);
        return "main";
    }
}
