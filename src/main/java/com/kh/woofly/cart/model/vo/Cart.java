package com.kh.woofly.cart.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Cart {
	private int cartId;
	private String mbId;
	private int quantity;
	private int productId;
	private int price;
	private String productName;
	private String selected;
	private String pSize;
	private String color;
}
