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
public class Pet {
	private int petId;
	private String petProfile;
	private String petName;
	private Date petBirth;
	private String petGender;
	private int petWeight;
	private String petBreed;
	private String petMemo;
	private String petStatus;
	private String ownerId;
	private String petHealth;
	private String petIntro;
}
