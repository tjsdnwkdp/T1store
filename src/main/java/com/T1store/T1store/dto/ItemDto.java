package com.T1store.T1store.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemDto {
    private Long id;
    private String itemNm;
    private Integer price;
    private String sellStatCd;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
