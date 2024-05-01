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
public class DwBoard {
	private int dwNo;
	private int dwType;
	private String dwTitle;
	private String dwContent;
	private int dwCount;
	private String dwLocation;
	private Date dwCreateDate;
	private String dwStatus;
	private String dwEnd;
	private String mbId;
	private String mbNickName;
	private int dwLike;
	private String dwPrice;
	private String mbPhoto;
}
