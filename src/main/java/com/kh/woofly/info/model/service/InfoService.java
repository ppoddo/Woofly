package com.kh.woofly.info.model.service;

import java.util.ArrayList;
import java.util.Properties;

import com.kh.woofly.common.PageInfo;
import com.kh.woofly.info.model.vo.Company;
import com.kh.woofly.info.model.vo.Notice;
import com.kh.woofly.info.model.vo.NoticeAttm;
import com.kh.woofly.info.model.vo.QNA;

public interface InfoService {

	int insertCompany(Company c);

	ArrayList<Notice> selectNoticeList(PageInfo pi);

	ArrayList<QNA> selectQNAList(PageInfo pi);

	ArrayList<Company> selectCompanyList(PageInfo pi);

	int getListCount(int i);

	int insertQNA(QNA q);

	QNA selectQNA(int qId);

	Company selectCompany(int comNo);

	int deleteQNA(int qId);

	int updateQNA(QNA q);

	int updateCompany(Company c);

	int deleteCompany(int comNo);

	int insertNotice(Notice n);

	int deleteNotice(int nNo);

	int updateNotice(Notice n);

	Notice selectNotice(int nNo, String id);

	ArrayList<Notice> searchNotice(PageInfo pi, Properties prop);

	int searchCount(Properties prop);

	ArrayList<QNA> searchQNA(PageInfo pi, Properties prop);

	ArrayList<Company> searchCompany(PageInfo pi, Properties prop);

	int selectCount(Properties prop);

	ArrayList<QNA> selectQNACategory(PageInfo pi, Properties prop);
	
	ArrayList<Notice> selectNoticeCategory(PageInfo pi, Properties prop);
	
	ArrayList<Notice> searchAllNotice(PageInfo pi, Properties prop);

	ArrayList<QNA> searchAllQNA(PageInfo pi, Properties prop);

	int insertAttm(ArrayList<NoticeAttm> list);

	int noticeNo();

	ArrayList<NoticeAttm> selectAttmNList(int nNum);

	int deleteAttm(String string);

	int selectComNo();

}
