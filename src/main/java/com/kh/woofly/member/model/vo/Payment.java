package com.kh.woofly.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
	private int paymentNo;
	private String billingKey;
	private String cardNumber;
	private String cardCompany;
	private String memberId;
	private String paymentType;
}
