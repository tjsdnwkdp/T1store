package com.T1store.T1store.service;

import com.T1store.T1store.constant.Category;
import com.T1store.T1store.dto.ItemFormDto;
import com.T1store.T1store.dto.ItemImgDto;
import com.T1store.T1store.dto.ItemSearchDto;
import com.T1store.T1store.dto.ProductItemDto;
import com.T1store.T1store.entity.Item;
import com.T1store.T1store.entity.ItemImg;
import com.T1store.T1store.repository.ItemImgRepository;
import com.T1store.T1store.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        //상품등록
        Item item =  itemFormDto.createItem();

        // 재고가 설정되지 않았거나 0 이하인 경우 기본값 100으로 설정
        if (item.getStockNumber() == null || item.getStockNumber() <= 0) {
            item.setStockNumber(100);
        }
        item.setCategory(itemFormDto.getCategory());  // 카테고리 설정
        itemRepository.save(item);
        //이미지 등록
        for(int i=0; i<itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0) { //첫번째 이미지가 대표 이미지
                itemImg.setRepImgYn("Y");
            } else {
                itemImg.setRepImgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for(ItemImg itemimg : itemImgList){
            ItemImgDto itemImgDto = ItemImgDto.of(itemimg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)throws Exception{
        //상품 변경
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        System.out.println("카테고리: " + itemFormDto.getCategory());
        //상품이미지 변경
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        for(int i = 0; i<itemImgFileList.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true) // 쿼리문 실행 읽기만
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto,pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProductItemDto> getProductItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getProductItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProductItemDto> getItemsByCategory(Category category, Pageable pageable) {

        Page<Item> items;


        if (category == Category.SHOP){
            items = itemRepository.findByCategoryIn(Category.getAllSubCategories(),pageable);
        }else {
            items = itemRepository.findByCategory(category, pageable);
        }
        List<ProductItemDto> productItemDtoList = new ArrayList<>();
        for (Item item : items) {
            // 대표 이미지를 가져옴
            ItemImg repImg = itemImgRepository.findByItemIdAndRepImgYn(item.getId(), "Y");
            ProductItemDto productItemDto = new ProductItemDto(item.getId(), item.getItemNm(), repImg.getImgUrl(), item.getPrice(),item.getCategory());
            productItemDtoList.add(productItemDto);
        }

        return new PageImpl<>(productItemDtoList, pageable, items.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<ProductItemDto> searchItemsInCategory(Category category, String searchQuery, Pageable pageable) {
        Page<Item> items = itemRepository.findByCategoryAndItemNmContaining(category, searchQuery, pageable);
        List<ProductItemDto> productItemDtoList = new ArrayList<>();

        for (Item item : items) {
            // 대표 이미지를 가져옴
            ItemImg repImg = itemImgRepository.findByItemIdAndRepImgYn(item.getId(), "Y");
            ProductItemDto productItemDto = new ProductItemDto(item.getId(), item.getItemNm(), repImg.getImgUrl(), item.getPrice(), item.getCategory());
            productItemDtoList.add(productItemDto);
        }

        return new PageImpl<>(productItemDtoList, pageable, items.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<ProductItemDto> searchItemsInAllCategories(String searchQuery, Pageable pageable) {
        Page<Item> items = itemRepository.findByItemNmContaining(searchQuery, pageable);
        List<ProductItemDto> productItemDtoList = new ArrayList<>();

        for (Item item : items) {
            // 대표 이미지를 가져옴
            ItemImg repImg = itemImgRepository.findByItemIdAndRepImgYn(item.getId(), "Y");
            ProductItemDto productItemDto = new ProductItemDto(item.getId(), item.getItemNm(), repImg.getImgUrl(), item.getPrice(), item.getCategory());
            productItemDtoList.add(productItemDto);
        }

        return new PageImpl<>(productItemDtoList, pageable, items.getTotalElements());
    }


}
