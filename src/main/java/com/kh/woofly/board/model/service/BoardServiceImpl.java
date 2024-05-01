package com.kh.woofly.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.woofly.admin.model.vo.Report;
import com.kh.woofly.board.model.dao.BoardDAO;
import com.kh.woofly.board.model.vo.Attachment;
import com.kh.woofly.board.model.vo.UsedBoard;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.board.model.vo.Board;
import com.kh.woofly.board.model.vo.DwBoard;
import com.kh.woofly.board.model.vo.LostBoard;

import com.kh.woofly.board.model.vo.UsedBoard;
import com.kh.woofly.board.model.vo.WmBoard;

import com.kh.woofly.common.PageInfo;
import com.kh.woofly.common.Reply;
import com.kh.woofly.common.ReplyLike;

@Service
public class BoardServiceImpl implements BoardService{

   @Autowired
   private BoardDAO bDAO;
private BoardServiceImpl boardDAO;
   
   
   @Override
	public int getReplyListCount(int i, int dwNo, String bType) {
	   return bDAO.getReplyListCount(i, dwNo, bType);
	}

   
 //	자유게시판 "/board/free"
   
    @Override
	public ArrayList<Board> searchFreeBoards(HashMap<String, String> map) {
    	return bDAO.searchFreeBoards(map);
	}
   
	@Override
	public int getListCount(int i) {
		return bDAO.getListCount(i);
	}
	
	@Override
	public ArrayList<Board> selectFreeBoardList(PageInfo pi, int i) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return bDAO.selectFreeBoardList(i, rowBounds);
	}

	@Override
	public ArrayList<Attachment> selectAttmFreeBoardList(Integer bId) {
		return bDAO.selectAttmFreeBoardList(bId);
	}

	@Override
	public Board selectFreeBoard(int bNo, String id) {
		Board b = bDAO.selectFreeBoard(bNo);
		if(b != null) {
			if(id !=null && !b.getMbId().equals(id)) {
				int result = bDAO.updateFreeCount(bNo);
				if(result >0) {
					b.setBCount(b.getBCount() + 1);
				}
			}
		}
		return b;
	}

	@Override
	public int insertFreeBoard(Board b) {
		return bDAO.insertFreeBoard(b);
	}

// 실종신고 게시판 "/board/lost" //
	@Override
	public int insertFreeAttm(ArrayList<Attachment> attachments) {
		return bDAO.insertFreeAttm(attachments);
	}
	
	@Override
	public int updateFreeBoard(Board b) {
		return bDAO.updateFreeBoard(b);
	}

	@Override
	public int deleteFreeBoard(int bNo) {
		return bDAO.deleteFreeBoard(bNo);
	}
	
	@Override
	public int deleteFreeAttm(ArrayList<String> delRename) {
		return bDAO.deleteFreeAttm(delRename);
	}

	@Override
	public void updateAttmLevel(int bNo) {
		bDAO.updateAttmLevel(bNo);
		
	}
	
	@Override
	public int statusNAttm(HashMap<String, Object> board) {
		return bDAO.statusNAttm(board);
	}

	@Override
	public int insertFreeReply(Reply r) {
		return bDAO.insertFreeReply(r);
	}
	
	@Override
	public int deleteFreeReply(Reply r) {
		return bDAO.deleteFreeReply(r);
	}
	
	@Override
	public int updateFreeReply(Reply r) {
		return bDAO.updateFreeReply(r);
	}

	@Override
	public ArrayList<Reply> selectFreeReply(int bNo) {
		return bDAO.selectFreeReply(bNo);
	}
	
	@Override
	public int freeBoardLike(HashMap<String, Object> map) {
		return bDAO.freeBoardLike(map);
	}


	@Override
	public int deleteFreeBoardLike(HashMap<String, Object> map) {
		return bDAO.deleteFreeBoardLike(map);
	}


	@Override
	public int insertFreeBoardLike(HashMap<String, Object> map) {
		return bDAO.insertFreeBoardLike(map);
	}

	@Override
	public ReplyLike selectReplyLike(Reply tempR) {
		return bDAO.selectReplyLike(tempR);
	}


	@Override
	public int deleteBoardReplyLike(HashMap<String, Object> map) {
		return bDAO.deleteBoardReplyLike(map);
	}


	@Override
	public int insertBoardReplyLike(HashMap<String, Object> map) {
		return bDAO.insertBoardReplyLike(map);
	}
	
	@Override
	public int insertFreeBoardNotice(HashMap<String, Object> map) {
		return bDAO.insertFreeBoardNotice(map);
	}

	@Override
	public int insertFreeReplyNotice(HashMap<String, Object> map) {
		return bDAO.insertFreeReplyNotice(map);
	}

	

	

	
//======// 도그워커  //===============================
	
	@Override
	public ArrayList<DwBoard> searchDwBoards(HashMap<String, String> map) {
		return bDAO.searchDwBoards(map);
	}
	
	@Override
	public int getDwListCount(int i) {
		return bDAO.getDwListCount(i);
	}
	
	@Override
	public ArrayList<DwBoard> selectDwBoardList(PageInfo pi, int i) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return bDAO.selectDwBoardList(i, rowBounds);
	}

	@Override
	public ArrayList<Attachment> selectAttmDwBoardList(Object object) {
		return bDAO.selectAttmDwBoardList(object);
	}
	
	//후기
	
	@Override
	public ArrayList<DwBoard> searchDwRvBoards(HashMap<String, String> map) {
		return bDAO.searchDwRvBoards(map);
	}
	
	@Override
	public int getDwRvListCount(int i) {
		return bDAO.getDwRvListCount(i);
	}

	@Override
	public ArrayList<DwBoard> selectDwRvBoardList(PageInfo pi, int i) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return bDAO.selectDwRvBoardList(i, rowBounds);
	}

	@Override
	public ArrayList<Attachment> selectAttmDwRvBoardList(Object object) {
		return bDAO.selectAttmDwRvBoardList(object);
	}
	
	@Override
	public DwBoard selectDwBoard(int dwNo, String id) {
		DwBoard dw = bDAO.selectDwBoard(dwNo);
		if(dw != null) {
			if(id !=null && !dw.getMbId().equals(id)) {
				int result = bDAO.updateDwCount(dwNo);
				if(result >0) {
					dw.setDwCount(dw.getDwCount() + 1);
				}
			}
		}
		return dw;
	}

	@Override
	public ArrayList<Reply> selectDwReply(int dwNo) {
		return bDAO.selectDwReply(dwNo);
	}
	
	@Override
	public int insertDwBoard(DwBoard dw) {
		return bDAO.insertDwBoard(dw);
	}

	@Override
	public int insertDwAttm(ArrayList<Attachment> attachments) {
		return bDAO.insertDwAttm(attachments);
	}
	
	@Override
	public int updateDwBoard(DwBoard dw) {
		return bDAO.updateDwBoard(dw);
	}
	
	@Override
	public int deleteDwBoard(int dwNo) {
		return bDAO.deleteDwBoard(dwNo);
	}
	
	@Override
	public int deleteDwAttm(ArrayList<String> delRename) {
		return bDAO.deleteDwAttm(delRename);
	}
	
	@Override
	public int insertDwReply(Reply r) {
		return bDAO.insertDwReply(r);
	}

	@Override
	public int deleteDwReply(Reply r) {
		return bDAO.deleteDwReply(r);
	}
	
	@Override
	public int updateDwReply(Reply r) {
		return bDAO.updateDwReply(r);
	}
	
	@Override
	public int selectBoardReport(Report rep) {
		return bDAO.selectBoardReport(rep);
	}
	
	@Override
	public int BoardReport(Report rep) {
		return bDAO.BoardReport(rep);
	}
	
	@Override
	public int checkReplyResult(Report rep) {
		return bDAO.checkReplyResult(rep);
	}

	@Override
	public int insertReplyReport(Report rep) {
		return bDAO.insertReplyReport(rep);
	}
	
	@Override
	public int DwBoardLike(HashMap<String, Object> map) {
		return bDAO.DwBoardLike(map);
	}

	@Override
	public int deleteDwBoardLike(HashMap<String, Object> map) {
		return bDAO.deleteDwBoardLike(map);
	}


	@Override
	public int insertDwBoardLike(HashMap<String, Object> map) {
		return bDAO.insertDwBoardLike(map);
	}


	@Override
	public int insertDwBoardNotice(HashMap<String, Object> map) {
		return bDAO.insertDwBoardNotice(map);
	}


	@Override
	public int insertDwReplyNotice(HashMap<String, Object> map) {
		return bDAO.insertDwReplyNotice(map);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//======// 산책메이트  //===============================
	
	@Override
	public ArrayList<WmBoard> searchWmBoards(HashMap<String, String> map) {
		return bDAO.searchWmBoards(map);
	}

	@Override
	public ArrayList<WmBoard> searchWmRvBoards(HashMap<String, String> map) {
		return bDAO.searchWmRvBoards(map);
	}

	@Override
	public int getWmListCount(int i) {
		return bDAO.getWmListCount(i);
	}

	@Override
	public ArrayList<WmBoard> selectWmBoardList(PageInfo pi, int i) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return bDAO.selectWmBoardList(i, rowBounds);
	}

	@Override
	public ArrayList<Attachment> selectAttmWmBoardList(Object object) {
		return bDAO.selectAttmWmBoardList(object);
	}	
	
	@Override
	public int getWmRvListCount(int i) {
		return bDAO.getWmRvListCount(i);
	}

	@Override
	public ArrayList<WmBoard> selectWmRvBoardList(PageInfo pi, int i) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return bDAO.selectWmRvBoardList(i, rowBounds);
	}

	@Override
	public ArrayList<Attachment> selectAttmWmRvBoardList(Object object) {
		return bDAO.selectAttmWmRvBoardList(object);
	}
	
	@Override
	public int insertWmBoard(WmBoard wm) {
		return bDAO.insertWmBoard(wm);
	}

	@Override
	public int insertWmAttm(ArrayList<Attachment> attachments) {
		return bDAO.insertWmAttm(attachments);
	}
	
	@Override
	public WmBoard selectWmBoard(@Param("wmNo") int wmNo, @Param("id") String id) {
		WmBoard wm = bDAO.selectWmBoard(wmNo);
		if(wm != null) {
			if(id !=null && !wm.getMbId().equals(id)) {
				int result = bDAO.updateWmCount(wmNo);
				if(result >0) {
					wm.setWmCount(wm.getWmCount() + 1);
				}
			}
		}
		return wm;
	}
	
	@Override
	public int deleteWmAttm(ArrayList<String> delRename) {
		return bDAO.deleteWmAttm(delRename);
	}

	@Override
	public int updateWmBoard(WmBoard wm) {
		return bDAO.updateWmBoard(wm);
	}	

	@Override
	public int deleteWmBoard(int wmNo) {
		return bDAO.deleteWmBoard(wmNo);
	}	
	
	@Override
	public int inserWmReply(Reply r) {
		return bDAO.inserWmReply(r);
	}

	@Override
	public int deletWmReply(Reply r) {
		return bDAO.deletWmReply(r);
	}

	@Override
	public ArrayList<Reply> selectWmReply(int wmNo) {
		return bDAO.selectWmReply(wmNo);
	}

	@Override
	public int updateWmReply(Reply r) {
		return bDAO.updateWmReply(r);
	}

	@Override
	public int WmBoardLike(HashMap<String, Object> map) {
		return bDAO.WmBoardLike(map);
	}


	@Override
	public int deleteWmBoardLike(HashMap<String, Object> map) {
		return bDAO.deleteWmBoardLike(map);
	}


	@Override
	public int insertWmBoardLike(HashMap<String, Object> map) {
		return bDAO.insertWmBoardLike(map);
	}


	@Override
	public int insertWmBoardNotice(HashMap<String, Object> map) {
		return bDAO.insertWmBoardNotice(map);
	}


	@Override
	public int insertWmReplyNotice(HashMap<String, Object> map) {
		return bDAO.insertWmReplyNotice(map);
	}
	
	
	
	
	

	
//======// 실종신고 게시판 "/board/lost" //===============================
	@Override
	public int getMlistCount(int i) {
		return bDAO.mListCount(i);
	}
	
	// 게시글 상세보기 "/board/lost/detail" //
	@Override
	public ArrayList<LostBoard> selectLostBoardList(PageInfo pi, int i) {
		int offset = (pi.getCurrentPage() - 1) * pi.getPageLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getPageLimit());
		return bDAO.selectLostBoardList(rowBounds, i);
	}

	@Override
	public ArrayList<Attachment> selectAttmLostBoardList(Integer bId) {
		return bDAO.selectAttmLostBoardList(bId);
	}

	
	

	// 실종 검색 //
	

	@Override
	public int insertLostBoard(LostBoard m) {
		return bDAO.insertLostBoard(m);
	}

	@Override
	public int insertLostAttm(ArrayList<Attachment> attachments) {
		return bDAO.insertLostAttm(attachments);
	}

	@Override
	public ArrayList<LostBoard> searchLostBoards(HashMap<String, String> map) {
		return bDAO.searchLostBoards(map);
	}

	@Override
	public LostBoard selectLostBoard(Integer mNo, String mbId) {
		
		LostBoard m = bDAO.selectLostBoard(mNo);
		if(m != null) {
			if(mbId !=null && !m.getMbId().equals(mbId)) {
				int result = bDAO.updateLostCount(mNo);
				if(result >0) {
					m.setMCount(m.getMCount() + 1);
				}
			}
		}
		return m;
	}

	@Override
	public LostBoard editLostBoard(int bId, Object object) {
		return bDAO.editLostBoard(bId, object);
	}

	/*
	 * @Override public int deleteLostBoard(int bId) { return
	 * bDAO.deleteLostBoard(bId); }
	 */
	

	 @Override
	 public int deleteLostBoard(int mNo) {
	 	return bDAO.deleteLostBoard(mNo);
	 }
	
	 @Override public int deleteLostBoardAttm(int mNo) { 
		 return bDAO.deleteLostBoardAttm(mNo); 
	 }
	

	@Override
	public int editLostBoard(LostBoard lb) {
		return bDAO.editLostBoard(lb);
	}
	
	@Override
	public int deleteLostAttm(ArrayList<String> delRename) {
		return bDAO.deleteLostAttm(delRename);
	}


	@Override
	public int updateLostBoard(LostBoard m) {
		return bDAO.updateLostBoard(m);
	}
	

	@Override
	public ArrayList<Reply> selectLostReply(int mNo) {
		return bDAO.selectLostReply(mNo);
	}
	
	@Override
	public boolean hasLevelOne(int mNo) {
		return bDAO.hasLevelOne(mNo);
	}

	@Override
	public int insertLostReply(Reply r) {
		return bDAO.insertLostReply(r);
	}


	@Override
	public int deleteLostReply(Reply r) {
		return bDAO.deleteLostReply(r);
	}


	@Override
	public int updateLostReply(Reply r) {
		return bDAO.updateLostReply(r);
	}
	
	@Override
	public int LostBoardLike(HashMap<String, Object> map) {
		return bDAO.LostBoardLike(map);
	}


	@Override
	public int insertLostBoardLike(HashMap<String, Object> map) {
		return bDAO.insertLostBoardLike(map);
	}


	@Override
	public int deleteLostBoardLike(HashMap<String, Object> map) {
		return bDAO.deleteLostBoardLike(map);
	}


	@Override
	public int insertLostBoardNotice(HashMap<String, Object> map) {
		return bDAO.insertLostBoardNotice(map);
	}


	@Override
	public int insertLostReplyNotice(HashMap<String, Object> map) {
		return bDAO.insertLostReplyNotice(map);
	}



	
//======// 중고게시판 "/board/used"  //===============================
	
		
	@Override
	public int getUlistCount(int i) {
		return bDAO.uListCount(i);
	}

	@Override
	public ArrayList<UsedBoard> selectUsedBoardList(PageInfo pi, int i) {
		int offset = (pi.getCurrentPage() - 1) * pi.getPageLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getPageLimit());
		return bDAO.selectUsedBoardList(rowBounds, i);
	}

	@Override
	public ArrayList<Attachment> selectAttmUsedBoardList(Object object) {
		return bDAO.selectAttmUsedBoardList(object);
	}

	@Override
	public ArrayList<UsedBoard> searchUsedBoards(HashMap<String, String> map) {
		return bDAO.searchUsedBoards(map);
	}



	public ArrayList<UsedBoard> selectMyUsedBuying(PageInfo pi, HashMap<String, Object> map) {
		int offset = (pi.getCurrentPage() - 1) * pi.getPageLimit();
		RowBounds rowbounds = new RowBounds(offset, pi.getPageLimit());
		return bDAO.selectMyUsedBuying(rowbounds, map);
	}

	@Override
	public int selectMyUsedBuyingCount(HashMap<String, Object> map) {
		return bDAO.selectMyUsedBuyingCount(map);
	}

	@Override
	public int selectMySellingCount(HashMap<String, Object> map) {
		return bDAO.selectMySellingCount(map);
	}

	@Override
	public ArrayList<UsedBoard> selectMySelling(PageInfo pi, HashMap<String, Object> map) {
		int offset = (pi.getCurrentPage() - 1) * pi.getPageLimit();
		RowBounds rowbounds = new RowBounds(offset, pi.getPageLimit());
		return bDAO.selectMySelling(rowbounds, map);
	}




	/* 하은 중고 */
	@Override
	public UsedBoard selectUsedBoard(Integer uNo, String mbId) {
		UsedBoard u = bDAO.selectUsedBoard(uNo);
		if(u != null) {
			if(mbId !=null && !u.getMbId().equals(mbId)) {
				int result = bDAO.updateUsedCount(uNo);
				if(result >0) {
					u.setUCount(u.getUCount() + 1);
				}
			}
		}
		return u;
	}


	@Override
	public Reply selectReply(Integer rNo) {
		return bDAO.selectReply(rNo);
	}


	@Override
	public int insertUsedBoard(UsedBoard u) {
		return bDAO.insertUsedBoard(u);
	}


	@Override
	public int insertUsedAttm(ArrayList<Attachment> attachments) {
		return bDAO.insertUsedAttm(attachments);
	}
	
	@Override
	public int deleteUsedAttm(ArrayList<String> delRename) {
		return bDAO.deleteUsedAttm(delRename);
	}


	@Override
	public int updateUsedBoard(UsedBoard u) {
		return bDAO.updateUsedBoard(u);
	}


	@Override
	public boolean UsedhasLevelOne(int uNo) {
		return bDAO.UsedhasLevelOne(uNo);
	}

	@Override
	public int deleteUsedBoard(int uNo) {
		return bDAO.deleteUsedBoard(uNo);
	}


	@Override
	public int deleteUsedBoardAttm(int uNo) {
		return bDAO.deleteUsedBoardAttm(uNo);
	}
	
	@Override
	public int insertUsedReply(Reply r) {
		return bDAO.insertUsedReply(r);
	}


	@Override
	public int deleteUsedReply(Reply r) {
		return bDAO.deleteUsedReply(r);
	}


	@Override
	public int updateUsedReply(Reply r) {
		return bDAO.updateUsedReply(r);
	}
	
	@Override
	public ArrayList<Reply> selectUsedReply(int uNo) {
		return bDAO.selectUsedReply(uNo);
	}





	
	
	
	
	
	//==========================중고게시판 황유경
	
	
	@Override
	public int insertUsedRvBoard(UsedBoard selectProduct) {
		return bDAO.insertUsedRvBoard(selectProduct);
	}


	@Override
	public int insertUsedRvAttm(ArrayList<Attachment> attachments) {
		return bDAO.insertUsedRvAttm(attachments);
	}


	@Override
	public ArrayList<UsedBoard> selectProdList(UsedBoard u) {
		return bDAO.selectProdList(u);
	}


	@Override
	public UsedBoard checkProdList(int prodNo) {
		return bDAO.checkProdList(prodNo);
	}

	/* 글 목록 */
	@Override
	public int getUsedRvListCount(int i) {
		return bDAO.getUsedRvListCount(i);
	}


	@Override
	public ArrayList<UsedBoard> selectUsedRvBoardList(PageInfo pi, int i) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return bDAO.selectUsedRvBoardList(i, rowBounds);
	}


	@Override
	public ArrayList<Attachment> selectAttmUsedRvBoardList(Object object) {
		return bDAO.selectAttmUsedRvBoardList(object);
	}


	@Override
	public ArrayList<UsedBoard> searchUsedRvBoards(HashMap<String, String> map) {
		return bDAO.searchUsedRvBoards(map);
	}


	@Override
	public UsedBoard selectUsedRvBoard(int uNo, String id) {
		
		UsedBoard u = bDAO.selectUsedRvBoard(uNo);
		if(u != null) {
			if(id !=null && !u.getMbId().equals(id)) {
				int result = bDAO.updateUsedRvCount(uNo);
				if(result >0) {
					u.setUCount(u.getUCount() + 1);
				}
			}
		}
		return u;
	}


	@Override
	public int getUsedRvReplyListCount(int uNo) {
		return bDAO.getUsedRvReplyListCount(uNo);
	}


	@Override
	public ArrayList<Reply> selectUsedRvReply(int uNo) {
		return bDAO.selectUsedRvReply(uNo);
	}

	/* 글 수정 */

	@Override
	public int deleteUsedRvAttm(ArrayList<String> delRename) {
		return bDAO.deleteUsedRvAttm(delRename);
	}


	@Override
	public int updateUsedRvBoard(UsedBoard u) {
		return bDAO.updateUsedRvBoard(u);
	}


	@Override
	public int deleteUsedRvBoard(int uNo) {
		return bDAO.deleteUsedRvBoard(uNo);
	}
	
	
	/* 댓글 */


	@Override
	public int insertUsedRvReply(Reply r) {
		return bDAO.insertUsedRvReply(r);
	}


	@Override
	public int deleteUsedRvReply(Reply r) {
		return bDAO.deleteUsedRvReply(r);
	}


	@Override
	public int updateUsedRvReply(Reply r) {
		return bDAO.updateUsedRvReply(r);
	}


	@Override
	public int UsedBoardLike(HashMap<String, Object> map) {
		return bDAO.UsedBoardLike(map);
	}


	@Override
	public int insertUsedBoardLike(HashMap<String, Object> map) {
		return bDAO.insertUsedBoardLike(map);
	}


	@Override
	public int deleteUsedBoardLike(HashMap<String, Object> map) {
		return bDAO.deleteUsedBoardLike(map);
	}


	@Override
	public int insertUsedRvBoardNotice(HashMap<String, Object> map) {
		return bDAO.insertUsedRvBoardNotice(map);
	}


	@Override
	public int insertUsedRvReplyNotice(HashMap<String, Object> map) {
		return bDAO.insertUsedRvReplyNotice(map);
	}


	@Override
	public int selectUsedSaved(HashMap<String, Object> map) {
		return bDAO.selectUsedSaved(map);
	}


	@Override
	public int deleteUsedSaved(HashMap<String, Object> map) {
		return bDAO.deleteUsedSaved(map);
	}


	@Override
	public int insertUsedSaved(HashMap<String, Object> map) {
		return bDAO.insertUsedSaved(map);
	}


	


	


	


	

	

	



	

	

	


	

	

	







	
	



	

	
	

	



	

	
	

	

	
	

	


	

	

	
	

	
	
	

	

	

	

	

	

	

	

	

	

	
	
	
	


}
