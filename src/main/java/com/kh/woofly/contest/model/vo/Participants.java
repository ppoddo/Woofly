package com.kh.woofly.contest.model.vo;

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
public class Participants {
	
	private int pNo;
	private String pTitle;
	private String pPet;
	private String pContent;
	private int pCount;
	private String pStatus;
	private int petId;
	private int contestId;
	private String pProduct;
	
	private String mbId;
	private String mbName;
	private String ownerId;
	private int conGen;
	private String renameName;
}
