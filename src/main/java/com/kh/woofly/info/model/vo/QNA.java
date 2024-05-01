package com.kh.woofly.info.model.vo;

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
public class QNA {
	
	private int qId;
	private String qTitle;
	private String qContent;
	private Date qCreqteDate;
	private Date qModifyDate;
	private String qCategory;
	private String qStatus;
	private int qCount;
	private String qWriter;
	
}
