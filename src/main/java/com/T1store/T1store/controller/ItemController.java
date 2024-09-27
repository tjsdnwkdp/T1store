package com.T1store.T1store.controller;

import com.T1store.T1store.constant.Category;
import com.T1store.T1store.dto.ItemFormDto;
import com.T1store.T1store.dto.ItemSearchDto;
import com.T1store.T1store.entity.Item;
import com.T1store.T1store.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto",new ItemFormDto());
        model.addAttribute("categories", Category.values()); // 모든 키테고리 전달
        model.addAttribute("hideSearchBar", true);
        return "/item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){
        if(bindingResult.hasErrors()){
            model.addAttribute("categories",Category.values());
            return "item/itemForm";
        }
        if(itemFormDto.getStockNumber() == null){
            itemFormDto.setStockNumber(100);
        }if(itemFormDto.getCategory() == null){
            model.addAttribute("errorMessage","카테고리를 선택해 주세요.");
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage",
                    "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage",
                    "싱품등록중 에러가 발생하였습니다.");
            model.addAttribute("categories", Category.values());
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId")Long itemId, Model model){
        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
            model.addAttribute("categories", Category.values());
            model.addAttribute("hideSearchBar", true);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto",new ItemFormDto());
            model.addAttribute("categories", Category.values());
            model.addAttribute("hideSearchBar", true);
            return "item/itemForm";
        }
        model.addAttribute("hideSearchBar", true);
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("categories", Category.values());
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage",
                    "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage",
                    "싱품등록중 에러가 발생하였습니다.");
            model.addAttribute("categories", Category.values());
            return "item/itemForm";
        }
        return "redirect:/";
    }
    @GetMapping(value = {"admin/items","/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page,
                             Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() :
                0, 10);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);
        model.addAttribute("hideSearchBar", true);
        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId")Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item",itemFormDto);

        return "item/itemDtl";
    }
}
