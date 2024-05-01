package com.kh.woofly.contest.model.vo;

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
public class Contest {
	private int conNo;
	private int conGen;
	private String conName;
	private Date conStartDate;
	private Date conEndDate;
	private String conStatus;
}