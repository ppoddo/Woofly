package com.kh.woofly.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.kh.woofly.admin.model.vo.Report;
import com.kh.woofly.board.model.vo.Attachment;
import com.kh.woofly.board.model.vo.Board;
import com.kh.woofly.board.model.vo.DwBoard;
import com.kh.woofly.board.model.vo.LostBoard;

import com.kh.woofly.board.model.vo.UsedBoard;
import com.kh.woofly.board.model.vo.WmBoard;

import com.kh.woofly.common.PageInfo;
import com.kh.woofly.common.Reply;
import com.kh.woofly.common.ReplyLike;

public interface BoardService {
	
	int getReplyListCount(int i, int dwNo, String bType);

//	자유게시판 "/board/free"	
	
	ArrayList<Board> searchFreeBoards(HashMap<String, String> map);
		
	int getListCount(int i);

	ArrayList<Board> selectFreeBoardList(PageInfo pi, int i);

	ArrayList<Attachment> selectAttmFreeBoardList(Integer bId);

	Board selectFreeBoard(int bNo, String id);

	int insertFreeBoard(Board b);

	int insertFreeAttm(ArrayList<Attachment> attachments);
	
	int updateFreeBoard(Board b);

	int deleteFreeBoard(int bNo);
	
	int deleteFreeAttm(ArrayList<String> delRename);
	
	void updateAttmLevel(int bNo);
	
	int statusNAttm(HashMap<String, Object> board);

	int insertFreeReply(Reply r);
	
	int deleteFreeReply(Reply r);
	
	int updateFreeReply(Reply r);

	ArrayList<Reply> selectFreeReply(int bNo);
	
	int freeBoardLike(HashMap<String, Object> map);

	int deleteFreeBoardLike(HashMap<String, Object> map);

	int insertFreeBoardLike(HashMap<String, Object> map);
	
	ReplyLike selectReplyLike(Reply tempR);
	
	int deleteBoardReplyLike(HashMap<String, Object> map);

	int insertBoardReplyLike(HashMap<String, Object> map);
	
	int insertFreeBoardNotice(HashMap<String, Object> map);
	
	int insertFreeReplyNotice(HashMap<String, Object> map);

	
//======// 도그워커  //===============================
	
	ArrayList<DwBoard> searchDwBoards(HashMap<String, String> map);

	int getDwListCount(int i);
	
	ArrayList<DwBoard> selectDwBoardList(PageInfo pi, int i);

	ArrayList<Attachment> selectAttmDwBoardList(Object object);
	
	//후기
	ArrayList<DwBoard> searchDwRvBoards(HashMap<String, String> map);
	
	int getDwRvListCount(int i);

	ArrayList<DwBoard> selectDwRvBoardList(PageInfo pi, int i);

	ArrayList<Attachment> selectAttmDwRvBoardList(Object object);
	
	
	DwBoard selectDwBoard(int dwNo, String id);

	ArrayList<Reply> selectDwReply(int dwNo);
	
	int insertDwBoard(DwBoard dw);
	
	int insertDwAttm(ArrayList<Attachment> attachments);
	
	int updateDwBoard(DwBoard dw);
	
	int deleteDwBoard(int dwNo);
	
	int deleteDwAttm(ArrayList<String> delRename);
	
	int insertDwReply(Reply r);

	int deleteDwReply(Reply r);
	
	int updateDwReply(Reply r);
	
	int selectBoardReport(Report rep);

	int BoardReport(Report rep);
	
	int checkReplyResult(Report rep);

	int insertReplyReport(Report rep);
	
	int DwBoardLike(HashMap<String, Object> map);
	
	int deleteDwBoardLike(HashMap<String, Object> map);

	int insertDwBoardLike(HashMap<String, Object> map);

	int insertDwBoardNotice(HashMap<String, Object> map);

	int insertDwReplyNotice(HashMap<String, Object> map);
	
	
	
//======// 워킹메이트  //===============================
	
	ArrayList<WmBoard> searchWmBoards(HashMap<String, String> map);

	ArrayList<WmBoard> searchWmRvBoards(HashMap<String, String> map);
	
	int getWmListCount(int i);

	ArrayList<WmBoard> selectWmBoardList(PageInfo pi, int i);

	ArrayList<Attachment> selectAttmWmBoardList(Object object);
	
	int getWmRvListCount(int i);

	ArrayList<WmBoard> selectWmRvBoardList(PageInfo pi, int i);

	ArrayList<Attachment> selectAttmWmRvBoardList(Object object);
	
	int insertWmBoard(WmBoard wm);

	int insertWmAttm(ArrayList<Attachment> attachments);
	
	WmBoard selectWmBoard(int wmNo, String id);
	
	int deleteWmAttm(ArrayList<String> delRename);

	int updateWmBoard(WmBoard wm);
	
	int deleteWmBoard(int wmNo);

	ArrayList<Reply> selectWmReply(int wmNo);
	
	int inserWmReply(Reply r);

	int deletWmReply(Reply r);
	
	int updateWmReply(Reply r);
	
	int WmBoardLike(HashMap<String, Object> map);

	int deleteWmBoardLike(HashMap<String, Object> map);

	int insertWmBoardLike(HashMap<String, Object> map);

	int insertWmBoardNotice(HashMap<String, Object> map);

	int insertWmReplyNotice(HashMap<String, Object> map);
	




	
	//연준이꺼

	ArrayList<UsedBoard> selectMyUsedBuying(PageInfo pi, HashMap<String, Object> map);

	int selectMyUsedBuyingCount(HashMap<String, Object> map);

	int selectMySellingCount(HashMap<String, Object> map);

	ArrayList<UsedBoard> selectMySelling(PageInfo pi, HashMap<String, Object> map);

	
	
	
	//==========================중고게시판
	int insertUsedRvBoard(UsedBoard selectProduct);

	int insertUsedRvAttm(ArrayList<Attachment> attachments);

	ArrayList<UsedBoard> selectProdList(UsedBoard u);

	UsedBoard checkProdList(int prodNo);

	int getUsedRvListCount(int i);

	ArrayList<UsedBoard> selectUsedRvBoardList(PageInfo pi, int i);

	ArrayList<Attachment> selectAttmUsedRvBoardList(Object object);

	ArrayList<UsedBoard> searchUsedRvBoards(HashMap<String, String> map);

	UsedBoard selectUsedRvBoard(int uNo, String id);

	int getUsedRvReplyListCount(int uNo);

	ArrayList<Reply> selectUsedRvReply(int uNo);
	
	/* 글 수정 */

	int deleteUsedRvAttm(ArrayList<String> delRename);

	int updateUsedRvBoard(UsedBoard u);
	
	/* 글 삭제 */

	int deleteUsedRvBoard(int uNo);
	
	/* 댓글 */

	int insertUsedRvReply(Reply r);

	int deleteUsedRvReply(Reply r);

	int updateUsedRvReply(Reply r);

	int UsedBoardLike(HashMap<String, Object> map);

	int insertUsedBoardLike(HashMap<String, Object> map);

	int deleteUsedBoardLike(HashMap<String, Object> map);

	int insertUsedRvBoardNotice(HashMap<String, Object> map);

	int insertUsedRvReplyNotice(HashMap<String, Object> map);


	
	

	
	/* 실종신고 게시판 "/board/lost" */
	int getMlistCount(int i);

	ArrayList<LostBoard> selectLostBoardList(PageInfo pi, int i);

	ArrayList<Attachment> selectAttmLostBoardList(Integer bId); 
	
//	첨부파일 게시글 작성 //
//	"/board/lost/detail"
	// 글쓰기
	

//	첨부파일 게시글 작성(등록) //
//	/board/lost/insertLostBoard

	
	// 실종 검색 //

	int insertLostBoard(LostBoard m);

	int insertLostAttm(ArrayList<Attachment> attachments);

	ArrayList<LostBoard> searchLostBoards(HashMap<String, String> map);

	LostBoard selectLostBoard(Integer mNo, String mbId);

	LostBoard editLostBoard(int bId, Object object);

	/*
	 * int deleteLostBoard(int bId);
	 */
	
	int deleteLostBoard(int mNo);
	
	int deleteLostBoardAttm(int mNo);	

	int editLostBoard(LostBoard lb);
	
	int deleteLostAttm(ArrayList<String> delRename);

	int updateLostBoard(LostBoard m);
	
	ArrayList<Reply> selectLostReply(int mNo);
	
	boolean hasLevelOne(int mNo);
	
	int insertLostReply(Reply r);

	int deleteLostReply(Reply r);

	int updateLostReply(Reply r);
	
	int LostBoardLike(HashMap<String, Object> map);

	int insertLostBoardLike(HashMap<String, Object> map);

	int deleteLostBoardLike(HashMap<String, Object> map);

	int insertLostBoardNotice(HashMap<String, Object> map);

	int insertLostReplyNotice(HashMap<String, Object> map);
	
	

//	Reply selectReplyLostBoard(Integer rNo);

//	수정된 첨부파일 게시글 업로드 //
//	int deleteLostAttm(ArrayList<String> delRename);
//	ArrayList<Member> selectMemberList(PageInfo pi, int i);
	
	
	// 중고게시판 //
	int getUlistCount(int i);

	ArrayList<UsedBoard> selectUsedBoardList(PageInfo pi, int i);

	ArrayList<Attachment> selectAttmUsedBoardList(Object object);

	ArrayList<UsedBoard> searchUsedBoards(HashMap<String, String> map);

	UsedBoard selectUsedBoard(Integer uNo, String mbId);

	Reply selectReply(Integer rNo);

	int insertUsedBoard(UsedBoard u);

	int insertUsedAttm(ArrayList<Attachment> attachments);
	
	int deleteUsedAttm(ArrayList<String> delRename);

	int updateUsedBoard(UsedBoard u);

	boolean UsedhasLevelOne(int uNo);

	int deleteUsedBoard(int uNo);

	int deleteUsedBoardAttm(int uNo);

	int insertUsedReply(Reply r);

	int deleteUsedReply(Reply r);

	int updateUsedReply(Reply r);

	ArrayList<Reply> selectUsedReply(int uNo);

	int selectUsedSaved(HashMap<String, Object> map);

	int deleteUsedSaved(HashMap<String, Object> map);

	int insertUsedSaved(HashMap<String, Object> map);

	



	

	

	

	

	

	

	
	
	
	

	

	

	

	

	

	


	

	

	

	

	

	

	

	

	

	

	

	

	

	

	


	

	



	

	

	

	

	

	

	



	

	



	

	

	


	


	
	
	
	


}
