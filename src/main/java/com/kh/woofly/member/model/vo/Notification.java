package com.kh.woofly.member.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Notification {
	private int notiNo;
	private String mbId;
	private String notiType;
	private String notiContent;
	private String isRead;
	private String fromUser;
	private int refNo;
	private Date sentDate;
	
	
}
