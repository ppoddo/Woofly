package com.kh.woofly.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class OrderDetail {
	private int orderDetailId;
	private int quantity;
	private int price;
	private int productId;
	private int orderId;
	private String productName;
}
