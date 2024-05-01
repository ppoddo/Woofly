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
public class WmBoard {
	private int wmNo;
	private int wmType;
	private String wmTitle;
	private String wmContent;
	private int wmCount;
	private String wmLocation;
	private Date wmCreateDate;
	private String wmStatus;
	private String mbId;
	private String mbNickName;
	private int wmLike;
	private String mbPhoto;
}
