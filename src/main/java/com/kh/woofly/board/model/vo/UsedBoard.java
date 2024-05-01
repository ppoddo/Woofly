package com.kh.woofly.board.model.vo;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
public class UsedBoard {
	private int uNo;
	private String uType;
	private String uTitle;
	private String uContent;
	private int uCount;
	private String uLocation;
	private Date uCreateDate;
	private String uStatus;
	private int uWant;
	private String uEnd;
	private String mbId;
	private String productName;
	private String productStatus;
	private int productPrice;
	private String mbPhoto;
	private String buyerNickName;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date soldDate;
	private String buyerId;
	private String mbNickName;

	private String renameName;

	


}
