package com.kh.woofly.pet.model.vo;



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
public class Diary {
	private int drNo;
	private String drContent;
	private Date drDate;
	private String drStatus;
	private String writerId;
	private int petId;
	private String petHealth;
	private String petName;
	private String petProfile;
}
