package com.kh.woofly.info.model.vo;

import java.sql.Date;

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
public class Notice {
	
	private int nNo;
	private String nTitle;
	private String nContent;
	private Date nCreateDate;
	private Date nModifyDate;
	private String nCategory;
	private String nStatus;
	private int nCount;
	private String nWriter; 
	
}
