package com.kh.woofly.info.model.service;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.woofly.common.PageInfo;
import com.kh.woofly.info.model.dao.InfoDAO;
import com.kh.woofly.info.model.vo.Company;
import com.kh.woofly.info.model.vo.Notice;
import com.kh.woofly.info.model.vo.NoticeAttm;
import com.kh.woofly.info.model.vo.QNA;

@Service
public class InfoServiceImpl implements InfoService{
	
	@Autowired
	private InfoDAO iDAO;

	@Override
	public int insertCompany(Company c) {
		
		return iDAO.insertCompany(c);
	}
	
	@Override
	public ArrayList<Notice> selectNoticeList(PageInfo pi) {
		
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return iDAO.selectNoticeList(rowBounds);
	}
	
	@Override
	public ArrayList<QNA> selectQNAList(PageInfo pi) {
		
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return iDAO.selectQNAList(rowBounds);
	}
	
	@Override
	public ArrayList<Company> selectCompanyList(PageInfo pi) {
		
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return iDAO.selectCompanyList(rowBounds);
	}
	
	@Override
	public int getListCount(int i) {
		
		return iDAO.getListCount(i);
	}
	
	@Override
	public int insertQNA(QNA q) {
		
		return iDAO.insertQNA(q);
	}
	
	@Override
	public QNA selectQNA(int qId) {
		
		return iDAO.selectQNA(qId);
	}

	@Override
	public Company selectCompany(int comNo) {
		
		return iDAO.selectCompany(comNo);
	}

	@Override
	public int deleteQNA(int qId) {
		
		return iDAO.deleteQNA(qId);
	}

	@Override
	public int updateQNA(QNA q) {
		
		return iDAO.updateQNA(q);
	}

	@Override
	public int updateCompany(Company c) {
		
		return iDAO.updateCompany(c);
	}

	@Override
	public int deleteCompany(int comNo) {

		return iDAO.deleteCompany(comNo);
	}

	@Override
	public int insertNotice(Notice n) {
		
		return iDAO.insertNotice(n);
	}
	
	
	@Override
	public Notice selectNotice(int nNo, String id) {
		Notice n = iDAO.selectNotice(nNo);
		if(n != null) {
			if(id != null & !n.getNWriter().equals(id)) {
				int result = iDAO.updateCount(nNo);
				if(result > 0) {
					n.setNContent(n.getNContent() + 1);
				}
			}
		}
		return n;
	}

	@Override
	public int deleteNotice(int nNo) {
		
		return iDAO.deleteNotice(nNo);
	}
	
	@Override
	public int updateNotice(Notice n) {
		
		return iDAO.updateNotice(n);
	}
	
	@Override
	public int searchCount(Properties prop) {
		
		return iDAO.searchCount(prop);
	}

	@Override
	public ArrayList<Notice> searchNotice(PageInfo pi, Properties prop) {
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return iDAO.searchNotice(rowBounds, prop);
	}

	@Override
	public ArrayList<QNA> searchQNA(PageInfo pi, Properties prop) {
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return iDAO.searchQNA(rowBounds, prop);
	}

	@Override
	public ArrayList<Company> searchCompany(PageInfo pi, Properties prop) {
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return iDAO.searchCompany(rowBounds, prop);
	}

	@Override
	public int selectCount(Properties prop) {
		
		return iDAO.selectCount(prop);
	}

	@Override
	public ArrayList<QNA> selectQNACategory(PageInfo pi, Properties prop) {
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return iDAO.selectQNACategory(rowBounds, prop);
	}

	@Override
	public ArrayList<Notice> selectNoticeCategory(PageInfo pi, Properties prop) {
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return iDAO.selectNoticeCategory(rowBounds, prop);
	}

	@Override
	public ArrayList<Notice> searchAllNotice(PageInfo pi, Properties prop) {
		
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return iDAO.searchAllNotice(rowBounds, prop);
	}

	@Override
	public ArrayList<QNA> searchAllQNA(PageInfo pi, Properties prop) {
		
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return iDAO.searchAllQNA(rowBounds, prop);
	}

	@Override
	public int insertAttm(ArrayList<NoticeAttm> list) {
		
		return iDAO.insertAttm(list);
	}

	@Override
	public int noticeNo() {
		// TODO Auto-generated method stub
		return iDAO.noticeNo();
	}

	@Override
	public ArrayList<NoticeAttm> selectAttmNList(int nNum) {
		// TODO Auto-generated method stub
		return iDAO.selectAttmNList(nNum);
	}

	@Override
	public int deleteAttm(String string) {
		// TODO Auto-generated method stub
		return iDAO.deleteAttm(string);
	}

	@Override
	public int selectComNo() {
		// TODO Auto-generated method stub
		return iDAO.selectComNo();
	}


	
}
