package com.kh.woofly.admin.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.kh.woofly.admin.model.vo.Report;
import com.kh.woofly.common.PageInfo;
import com.kh.woofly.contest.model.vo.Contest;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.member.model.vo.MemberAddress;

public interface AdminService {

	HashMap<String, ArrayList<Object>> selectAllBoard();

	int getReportCount(String searchId);

	ArrayList<Report> selectReportList(PageInfo pi, String searchId);

	ArrayList<HashMap<String, Object>> selectReportRank();

	Report selectReportDetail(int rNo);

	ArrayList<Report> selectTargetList(String rAccused);

	int updateStopDate(String rAccused);

	int updateReportSit(Report r);

	int selectWarningCount(Report r);

	ArrayList<Member> selectAllMembers(PageInfo pi, String mbId);

	ArrayList<MemberAddress> selectAllAddress();

	int getMembersCount(String mbId);

	int updateKickOutMembers(String mbId);

	int updateInfo(Properties prop);

	int selectMonthlyEarnings();

	int selectAnnualEarnings();

	HashMap<String, Object> selectContestRatio();

	int selectCompanyCount();

	ArrayList<HashMap<String, Object>> selectMonthlyList();

	HashMap<String, Object> selectBoardsCount();

	ArrayList<HashMap<String, Object>> selectReportBoardCount();

	int insertNotify(HashMap<String, Object> notifyMap);

	ArrayList<Contest> selectAfterContest();
	

}
