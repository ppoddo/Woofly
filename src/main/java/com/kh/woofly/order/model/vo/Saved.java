package com.kh.woofly.order.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Saved {
	private int savedNo;
	private int productId;
	private String mbId;
	private String savedType;
	
	private String renameName;
	private String productName;
	private int price;
	
	private int uNo;
	private int productPrice;
	private String uTitle;
	private String uContent;
	private Date uCreateDate;
	private String mbNickName;
	
}

