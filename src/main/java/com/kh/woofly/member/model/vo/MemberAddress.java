package com.kh.woofly.member.model.vo;

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
public class MemberAddress {
	private int addrId;
	private String postcode;
	private String addr;
	private String addrDetail;
	private String mbId;
	private String addrType;
	private String mbTel;
	private String mbName;
}
