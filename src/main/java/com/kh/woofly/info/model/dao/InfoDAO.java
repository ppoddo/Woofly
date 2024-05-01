package com.kh.woofly.info.model.dao;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.woofly.info.model.vo.Company;
import com.kh.woofly.info.model.vo.Notice;
import com.kh.woofly.info.model.vo.NoticeAttm;
import com.kh.woofly.info.model.vo.QNA;

@Mapper
public interface InfoDAO {

	int insertCompany(Company c);

	ArrayList<Notice> selectNoticeList(RowBounds rowBounds);

	ArrayList<QNA> selectQNAList(RowBounds rowBounds);

	ArrayList<Company> selectCompanyList(RowBounds rowBounds);

	int getListCount(int i);
	
	int insertQNA(QNA q);

	QNA selectQNA(int qId);

	Company selectCompany(int comNo);

	int deleteQNA(int qId);

	int updateQNA(QNA q);

	int updateCompany(Company c);

	int deleteCompany(int comNo);

	int insertNotice(Notice n);

	Notice selectNotice(int nNo);

	int deleteNotice(int nNo);

	int updateNotice(Notice n);

	int updateCount(int nNo);

	ArrayList<Notice> searchNotice(RowBounds rowBounds, Properties prop);

	ArrayList<QNA> searchQNA(RowBounds rowBounds, Properties prop);

	ArrayList<Company> searchCompany(RowBounds rowBounds, Properties prop);

	int searchCount(Properties prop);

	int selectCount(Properties prop);

	ArrayList<QNA> selectQNACategory(RowBounds rowBounds, Properties prop);

	ArrayList<Notice> selectNoticeCategory(RowBounds rowBounds, Properties prop);

	ArrayList<Notice> searchAllNotice(RowBounds rowBounds, Properties prop);

	ArrayList<QNA> searchAllQNA(RowBounds rowBounds, Properties prop);

	int insertAttm(ArrayList<NoticeAttm> list);

	int noticeNo();

	ArrayList<NoticeAttm> selectAttmNList(int nNum);

	int deleteAttm(String string);

	int selectComNo();
	
	

}
