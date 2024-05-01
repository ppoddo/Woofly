package com.kh.woofly.info.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class NoticeAttm {
	private int attmId;
	private String originalName;
	private String renameName;
	private Date attmCreateDate;
	private Date attmModifyDate;
	private String attmPath;
	private String attmStatus;
	private String attmRefType;
	private int attmRefNo;
	private int attmLevel;
}
