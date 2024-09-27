package com.T1store.T1store.controller;

import com.T1store.T1store.dto.ItemDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {
    @ResponseBody
    @RequestMapping("/")
    public String root() throws Exception {
        //localhost/thymeleaf/ex01 -> thymeleafEx01.html
        //${data} -> Hello World 나오도록 출력
        return "Thymeleaf view";
    }

    @GetMapping("/ex01")
    public String thymeleafEx01(Model model) {
        model.addAttribute("data", "Hello World");

        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping("/ex02")
    public String thymeleafEx02(Model model) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());
        model.addAttribute("itemDto", itemDto);
        return "thymeleafEx/thymeleafEx02";
    }
    @GetMapping("/ex03")
    public String thymeleafEx03(Model model) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for(int i=1; i<=10; i++) {
            ItemDto itemDto = new ItemDto();

            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(10000 + i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }
    @GetMapping("/ex04")
    public String thymeleafEx04(Model model) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for(int i=1; i<=10; i++) {
            ItemDto itemDto = new ItemDto();

            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(10000 + i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }
    @GetMapping("/ex05")
    public String thymeleafEx05(Model model) {
        return "thymeleafEx/thymeleafEx05";
    }
//    @GetMapping("/ex06") //@RequestParam -  1:1로 데이터 받기
//    public String thymeleafEx06(@RequestParam("param1") String p1, @RequestParam("param2") String p2, Model model) {
//        model.addAttribute("param1", p1);
//        model.addAttribute("param2", p2);
//        return "thymeleafEx/thymeleafEx06";
//    }
    @GetMapping("/ex06") //HttpServletRequest - /ex06 이후로 오는 모든 값을 req 로 받고 파라미터명에 해당하는 값 받기
    public String thymeleafEx06(HttpServletRequest req, Model model) {
        model.addAttribute("param1", req.getParameter("param1"));
        model.addAttribute("param2", req.getParameter("param2"));
        return "thymeleafEx/thymeleafEx06";
    }
    @GetMapping("/ex07")
    public String thymeleafEx07(Model model) {
        return "thymeleafEx/thymeleafEx07";
    }
}
