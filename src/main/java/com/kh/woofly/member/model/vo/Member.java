package com.kh.woofly.member.model.vo;

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
public class Member {
	private String mbId;
	private String mbPwd;
	private String mbName;
	private String mbPhoto;
	private String mbIntro;
	private String mbNickName;
	private String mbEmail;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date mbBirth;
	private String mbTel;
	private int mbPoint;
	private String mbStatus;
	private String isAdmin;
	private String isPrivate;
	private Date nextChange;
	private String isKakao;

	// 추가한다고 했음.
	private String isBanned;
	

}
