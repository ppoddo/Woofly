package com.kh.woofly.board.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

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


@Mapper
public interface BoardDAO {
	
	int getReplyListCount(int i, int dwNo, String bType);

//	자유게시판 "/board/free"
	
	ArrayList<Board> searchFreeBoards(HashMap<String, String> map);
	
	int getListCount(int i);

	ArrayList<Board> selectFreeBoardList(int i, RowBounds rowBounds);

	ArrayList<Attachment> selectAttmFreeBoardList(Integer bId);

	Board selectFreeBoard(int bNo);
	
	int updateFreeCount(int bNo);
	
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

	ArrayList<DwBoard> selectDwBoardList(int i, RowBounds rowBounds);

	ArrayList<Attachment> selectAttmDwBoardList(Object object);
	
	//후기
	
	ArrayList<DwBoard> searchDwRvBoards(HashMap<String, String> map);
	
	int getDwRvListCount(int i);

	ArrayList<DwBoard> selectDwRvBoardList(int i, RowBounds rowBounds);

	ArrayList<Attachment> selectAttmDwRvBoardList(Object object);
	
	
	DwBoard selectDwBoard(int dwNo);
	
	ArrayList<Reply> selectDwReply(int dwNo);
	
	int updateDwCount(int dwNo);

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
	
	
	
	
	
	
	

	
	
//======// 산책메이트  //===============================
	
	ArrayList<WmBoard> searchWmBoards(HashMap<String, String> map);

	ArrayList<WmBoard> searchWmRvBoards(HashMap<String, String> map);
	
	int getWmListCount(int i);

	ArrayList<WmBoard> selectWmBoardList(int i, RowBounds rowBounds);

	ArrayList<Attachment> selectAttmWmBoardList(Object object);

	int getWmRvListCount(int i);

	ArrayList<WmBoard> selectWmRvBoardList(int i, RowBounds rowBounds);

	ArrayList<Attachment> selectAttmWmRvBoardList(Object object);

	int insertWmBoard(WmBoard wm);

	int insertWmAttm(ArrayList<Attachment> attachments);
	
	WmBoard selectWmBoard(@Param("wmNo") int wmNo);
	
	int updateWmCount(int wmNo);
	
	int deleteWmAttm(ArrayList<String> delRename);

	int updateWmBoard(WmBoard wm);
	
	int deleteWmBoard(int wmNo);
	
	int inserWmReply(Reply r);

	int deletWmReply(Reply r);

	ArrayList<Reply> selectWmReply(int wmNo);
	
	int updateWmReply(Reply r);
	
	int WmBoardLike(HashMap<String, Object> map);

	int deleteWmBoardLike(HashMap<String, Object> map);

	int insertWmBoardLike(HashMap<String, Object> map);

	int insertWmBoardNotice(HashMap<String, Object> map);

	int insertWmReplyNotice(HashMap<String, Object> map);
	
	
	
	
	


	
	// 연준이꺼
	
	ArrayList<UsedBoard> selectMyUsedBuying(RowBounds rowbounds, HashMap<String, Object> map);

	int selectMyUsedBuyingCount(HashMap<String, Object> map);

	int selectMySellingCount(HashMap<String, Object> map);

	ArrayList<UsedBoard> selectMySelling(RowBounds rowbounds, HashMap<String, Object> map);

	

//	실종신고게시판 "/board/lost"
	ArrayList<LostBoard> searchLostBoards(HashMap<String, String> map);

	ArrayList<LostBoard> selectLostBoardList(RowBounds rowBounds, int i);

	ArrayList<Attachment> selectAttmLostBoardList(Integer bId);

	int mListCount(int i);
	
	// 실종 검색 //
	int insertLostBoard(LostBoard m);

	int insertLostAttm(ArrayList<Attachment> attachments);

	LostBoard selectLostBoard(Integer mNo);
	
	int updateLostCount(Integer mNo);

	LostBoard editLostBoard(int bId, Object object);
	
	int LostBoardLike(HashMap<String, Object> map);

	int insertLostBoardLike(HashMap<String, Object> map);

	int deleteLostBoardLike(HashMap<String, Object> map);

	int insertLostBoardNotice(HashMap<String, Object> map);

	int insertLostReplyNotice(HashMap<String, Object> map);


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
	
	ArrayList<Reply> selectUsedReply(int uNo);

//	Reply selectReplyLostBoard(Integer rNo);

//	ArrayList<Member> selectMemberList(PageInfo pi, int i);
	
	
//	중고게시판 "/board/used" 
	int uListCount(int i);

	ArrayList<UsedBoard> selectUsedBoardList(RowBounds rowBounds, int i);

	ArrayList<Attachment> selectAttmUsedBoardList(Object object);

	ArrayList<UsedBoard> searchUsedBoards(HashMap<String, String> map);

	UsedBoard selectUsedBoard(Integer uNo);
	
	int updateUsedCount(Integer uNo);

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



	
	//==========================중고게시판
	
	int insertUsedRvBoard(UsedBoard selectProduct);

	int insertUsedRvAttm(ArrayList<Attachment> attachments);

	ArrayList<UsedBoard> selectProdList(UsedBoard u);
	
	ArrayList<Attachment> selectAttmUsedRvBoardList(Object object);

	ArrayList<UsedBoard> searchUsedRvBoards(HashMap<String, String> map);

	UsedBoard checkProdList(int prodNo);

	int getUsedRvListCount(int i);

	ArrayList<UsedBoard> selectUsedRvBoardList(int i, RowBounds rowBounds);

	UsedBoard selectUsedRvBoard(int uNo);

	int updateUsedRvCount(int uNo);

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

	int selectUsedSaved(HashMap<String, Object> map);

	int deleteUsedSaved(HashMap<String, Object> map);

	int insertUsedSaved(HashMap<String, Object> map);

	

	

	


	

	

	

	

	

	

	

	

	

	

	
	

	

	

	

	



	

	

	

	

	



	
	

	



	

	

	

	
	

	

	





	

	

	

	

	

	

	

	

	

	

	

	
	




	

	

	

	

	

	


	

	
	
	
}
