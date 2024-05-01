package com.kh.woofly.common;

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

public class Reply {

	private int rNo;
	private String bType;
	private int bNo;
	private String reContent;
	private Date reDate;
	private int reLike;
	private String reDStatus;
	private String mbId;		// 댓글 단 사람 아이디
	
	private String mbNickName;
	private boolean userLiked;
	private String mbPhoto;
}

