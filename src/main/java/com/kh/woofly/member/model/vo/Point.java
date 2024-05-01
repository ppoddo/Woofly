package com.kh.woofly.member.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Point {
	private int historyId;
	private String transactionType;
	private int pointChange;
	private Date transactionDatetime;
	private String mbId;
}
