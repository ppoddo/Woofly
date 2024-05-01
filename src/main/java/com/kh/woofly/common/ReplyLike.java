package com.kh.woofly.common;

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

public class ReplyLike {

	private int likeId;
	private int likeRefBoard;
	private String likeUser;
}
