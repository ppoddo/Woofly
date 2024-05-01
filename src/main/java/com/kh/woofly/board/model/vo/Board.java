package com.kh.woofly.board.model.vo;

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

public class Board {
	private int bNo;
	private String bTitle;	
	private int bCount;
	private Date bCreateDate;
	private String bStatus;
	private String mbId;
	private String bContent;
	private String mbNickName;
	private int bLike;
	private String mbPhoto;
	
	
}
