package com.kh.woofly.shop.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Product {
	private int productId;
	private String productName;
	private int price;
	private Date productCreateDate;
	private Date productModifyDate;
	private int productDetailNo; // 상세분류 번호를 가져오면 대분류 + 상세분류 가능
	private String productStatus;
	
	private String usedStandard;
	private String color;
	private int quantity;
	private int stockId;
	
}
