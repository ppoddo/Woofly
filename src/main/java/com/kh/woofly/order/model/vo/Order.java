package com.kh.woofly.order.model.vo;

import java.util.Date;

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
public class Order {
	
	private int orderId;
	private Date orderDate;
	private int total;
	private String customerId;
	private int transFee;
	private int usedPoints;
	private String orderOwner;
	private String mbTel;
	private String addr;
	private String paymentMethod;
	private String orderRequest;
	private String delStatus;
	private int productCount;
}
