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
public class Company {
	
	private int comNo;
	private String comName;
	private String comCategory;
	private String comPhone;
	private Date comCreateDate;
	private String comEmail;
	private String comStatus;
	private String comAddr;
}
