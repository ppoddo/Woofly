package com.kh.woofly.admin.model.vo;

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

public class Report {

	private int rNo;
	private String rContent;		// DB에서 받아올 때 사용
	private Date rDate;
	private String rSituation;		// A = 접수,W = 경고 , R = 반려, "B" = 정지
	private String rCategory;
	private int rBoardNo;
	private String rType;			// B = 게시글, R= 댓글
	private String rAccuser;		// 신고한사람
	private String rAccused;		// 신고당한사람
	
	private String mbName;			// 가져오는 주체의 이름
	private String mbSubName;		// 네임 2개 필요하면 사용하는 sub
	private Date mbDate;
}
