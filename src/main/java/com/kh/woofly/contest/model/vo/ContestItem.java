package com.kh.woofly.contest.model.vo;

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
public class ContestItem {
	private int orderId;
	private String customerId;
	
	private int orderDetailId;
	private int productId;
//	orderId
	
	private String productName;
	private int price;
	private String productStatus;
//	orderDetailId
}