package com.kh.woofly.pet.model.vo;


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
public class Album {
	private int abNo;
	private String abContent;
	private Date abDate;
	private String abStatus;
	private String writerId;
	private int petId;
	private String renameName;
	private String petName;
	private String petProfile;
	private int abLike;
}
