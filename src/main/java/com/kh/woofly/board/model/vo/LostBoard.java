package com.kh.woofly.board.model.vo;

import java.sql.Date;
import java.util.List;

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
public class LostBoard {
   private int mNo;
   private String mTitle;
   private String mContent;
   private int mCount;
   private Date mCreateDate;
   private String mStatus;
   private String mbId;
   private String mbNickName;
   private int mRescue;
   private int likeUser; // 좋아요 누른 사람
   private int mLike;
   private String mbPhoto;
   private List<Attachment> attachments;
}





