package com.kh.woofly.shop.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class ProductCategory {
	
	private int productDetailNo;	// 프머키
	private int productCategoryNo;
	private String productCategory;
	private String productDetailCategory;

}
