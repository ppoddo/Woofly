package com.kh.woofly.admin.controller;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.woofly.admin.model.exception.AdminException;
import com.kh.woofly.admin.model.service.AdminService;
import com.kh.woofly.admin.model.vo.Report;
import com.kh.woofly.common.PageInfo;
import com.kh.woofly.common.Pagination;
import com.kh.woofly.contest.model.vo.Contest;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.member.model.vo.MemberAddress;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	
	@Autowired
	private AdminService aService;
	
	@GetMapping("/adminMain.ad")
	public String moveToAdminMain(Model model, HttpSession session) {
		// HashMap<String, ArrayList<Object>> lists =  aService.selectAllBoard();
		
		Member admin = (Member)session.getAttribute("loginUser");
		if(admin.getIsAdmin().equals("N")) {
			return "index";
		}
		
		// 월간 수익률
		int monthlyEarnings = aService.selectMonthlyEarnings();
		
		// 연간 수익률
		int annualEarnings = aService.selectAnnualEarnings();
		
		// 콘테스트 참가 수 대비 쇼핑몰 물품 참여 회원 수
		HashMap<String, Object> contestRatio = aService.selectContestRatio();
		
		int partiTotal = Integer.parseInt(contestRatio.get("TOTAL_PARTICIPANTS").toString());
		int partiPart = Integer.parseInt(contestRatio.get("PARTICIPANTS_WITH_O_PRODUCT").toString());
		int partPercent = 0;
		if(partiTotal != 0) {
			partPercent = (int)Math.round((double)partiPart/partiTotal * 100);
		}
		// 협력업체 총 수
		int companyCount = aService.selectCompanyCount();
		
		// 달 별 수익리스트
		ArrayList<HashMap<String, Object>> monthlyList = aService.selectMonthlyList();
		
		YearMonth currentYearMonth = YearMonth.now();
		ArrayList<String> controlGroup = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
		    YearMonth targetYearMonth = currentYearMonth.minusMonths(i);
		    String formattedMonth = String.format("%04d-%02d", targetYearMonth.getYear(), targetYearMonth.getMonthValue());
		    controlGroup.add(formattedMonth);
		}

		for (String month : controlGroup) {
		    boolean found = false;
		    for (HashMap<String, Object> map : monthlyList) {
		        if (map.get("MONTH").equals(month)) {
		            found = true;
		            break;
		        }
		    }

		    if (!found) {
		        HashMap<String, Object> notFoundMap = new HashMap<>();
		        notFoundMap.put("MONTH", month);
		        notFoundMap.put("TOTAL_SUM", 0);
		        monthlyList.add(notFoundMap);
		    }
		}
		
		// 정렬
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
	    monthlyList.sort(Comparator.comparing(map -> YearMonth.parse((CharSequence) map.get("MONTH"), formatter)));
	    
	    // 게시판 조회수 총량 대비 게시판 별 먹는 퍼센트게이지(1년도 추가하셈)
	    HashMap<String, Object> counts = aService.selectBoardsCount();
	    int uCount = Integer.parseInt(counts.get("U_COUNT").toString());
	    int dwCount = Integer.parseInt(counts.get("DW_COUNT").toString());
	    int mCount = Integer.parseInt(counts.get("M_COUNT").toString());
	    int bCount = Integer.parseInt(counts.get("B_COUNT").toString());
	    int wmCount = Integer.parseInt(counts.get("WM_COUNT").toString());
	    
	    int total = uCount + dwCount + mCount + bCount + wmCount;
	    
	    HashMap<String, Double> percents = new HashMap<>();
	    if(total != 0) {
		    double uPercent = Math.round((double)uCount/total * 100);
		    double dwPercent = Math.round((double)dwCount/total * 100);
		    double mPercent =  Math.round((double)mCount/total * 100);
		    double wmPercent = Math.round((double)wmCount/total * 100);
		    double bPercent =  100 - (uPercent + dwPercent + mPercent + wmPercent);
		    
		    percents.put("uPercent", uPercent);
		    percents.put("dwPercent", dwPercent);
		    percents.put("mPercent", mPercent);
		    percents.put("wmPercent", wmPercent);
		    percents.put("bPercent", bPercent);
	    }
	    //게시판 별 신고 누적 수(1년)
	    ArrayList<HashMap<String, Object>> boardsReport = aService.selectReportBoardCount();
	    
	    //int mrCount = Integer.parseInt(boardsReport[0].get("TOTAL_COUNT").toString());
	    
	    int[] reportSort = new int[boardsReport.size()];
	    String[] reportCate = new String[boardsReport.size()];
	    
	    for(int i = 0; i < boardsReport.size(); i++) {
	    	HashMap<String, Object> board = boardsReport.get(i);
	    	Object reportCount = board.get("TOTAL_COUNT");
	    	Object reportCategory = board.get("R_CATEGORY");
	    	
	    	reportSort[i] = Integer.parseInt(reportCount.toString());
	    	reportCate[i] = reportCategory.toString();
	    	
	    }
	    
	    int reportTotal = 0;
	    for(int i = 0; i < reportSort.length; i++) {
	    	reportTotal += reportSort[i];
	    }
	    
	    int selectJ = 0;
	    HashMap<String, Integer> reportPercents = new HashMap<>();
	    if(reportTotal != 0) {
	    	for(int i = 0; i < reportSort.length; i++) {
	    		int j = (int) Math.round((double)reportSort[i]/reportTotal * 100);
	    		reportPercents.put(reportCate[i], j);
	    		
	    		selectJ += j;
	    		
	    		if(i == reportSort.length-1 && selectJ != 100) {
	    			int supple = 100 - selectJ;
	    			reportPercents.put(reportCate[i], reportPercents.get(reportCate[i]) + supple);
	    		}
	    	}
	    }
	    
	    // 오늘 날짜 기준으로 endDate가 이후인 콘테스트 가져오기
	    ArrayList<Contest> contestList = aService.selectAfterContest();
	    
	    model.addAttribute("admin", admin);
		model.addAttribute("monthlyEarnings", monthlyEarnings);
		model.addAttribute("percents", percents);
		model.addAttribute("annualEarnings", annualEarnings);
		model.addAttribute("contestRatio", contestRatio);
		model.addAttribute("partPercent", partPercent);
		model.addAttribute("companyCount", companyCount);
		model.addAttribute("monthlyList", monthlyList);
		model.addAttribute("boardsReport", boardsReport);
		model.addAttribute("reportPercents", reportPercents);
		model.addAttribute("contestList", contestList);
		
		return "adminMain";
	}
	
	@GetMapping("/admin/reportList.ad")
	public String moveToReport(@RequestParam(value = "page", defaultValue="1") int page,
								@RequestParam(value = "mbId", required = false) String searchId,
								Model model, HttpServletRequest request
								) {
					//@RequestParam(value="mbId", required=false) String rAccused
		
		// 신고 전체리스트
		int listCount = aService.getReportCount(searchId);
		PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
		
		ArrayList<Report> rList = aService.selectReportList(pi, searchId);
		// 랭킹 리스트 나중에 일자 별 검색으로 바꿀 때는 검색할 N 숫자 보내면 됨
		ArrayList<HashMap<String, Object>> rkList = aService.selectReportRank();
		for(int i = 0; i < rkList.size(); i ++) {
			String date = new SimpleDateFormat("yyyy-MM-dd").format(rkList.get(i).get("LAST_REPORT_DATE"));
			rkList.get(i).put("LAST_REPORT_DATE", date);
		}
		
		model.addAttribute("rkList", rkList);
		model.addAttribute("rList", rList);
		model.addAttribute("pi", pi);
		model.addAttribute("loc", request.getRequestURI());
		// R_ACCUSED
		
		return "reportList";
	}
	
	@GetMapping("/admin/reportDetail")
	public String moveToReportDetail(@RequestParam("rNo") int rNo, Model model) {
		
		// 현재 신고한 신고 정보(신고자, 게시글 구분, 카테고리, date, 내용, 피신고자 이름)
		Report r = aService.selectReportDetail(rNo);
		ArrayList<Report> rList = aService.selectTargetList(r.getRAccused());
		
		int pileWarning = 0;
		int pileBanded = 0;
		if(!rList.isEmpty()) {
			for(Report dr : rList) {
				if(dr.getRSituation().equalsIgnoreCase("W")) {
					pileWarning++;
				} else if(dr.getRSituation().equalsIgnoreCase("B")) {
					pileBanded++;
				}
			}
		}
		model.addAttribute("pileWarning", pileWarning);
		model.addAttribute("pileBanded", pileBanded);
		model.addAttribute("r", r);
		model.addAttribute("rList", rList);
		
		return "reportDetail";
	}
	
	@GetMapping("/admin/stopUser.ad")
	public String stopUser(@ModelAttribute Report r,
							HttpSession session,
							RedirectAttributes re,
							Model model,
							@RequestParam("reason") String reason) {
		Member admin = (Member)session.getAttribute("loginUser");
		if(admin == null || !admin.getIsAdmin().equals("Y")) {
			return "index";
		} 
		
		if(r != null) {
			r.setRSituation("B");
			int result1 = aService.updateStopDate(r.getRAccused());
			int result2 = aService.updateReportSit(r);
			
			HashMap<String, Object> notifyMap = new HashMap<>();
			notifyMap.put("mbId", r.getRAccused());	// 타겟
			notifyMap.put("notiType", "AM");
			notifyMap.put("notiContent", "사유 : " + reason + " - 로 인해 정지되었습니다.");
			notifyMap.put("fromUser", admin.getMbId());		// 보내는 사람
			notifyMap.put("refNo", r.getRNo());
			
			int notify = aService.insertNotify(notifyMap);
		
			re.addAttribute("rNo", r.getRNo());
			
			return "redirect:/admin/reportDetail";
		} else {
			return "/adminMain.ad";
		}
	}
	
	@GetMapping("/admin/warningUser.ad")
	public String warningUser(@ModelAttribute Report r,
								HttpSession session,
								RedirectAttributes re,
								@RequestParam("reason") String reason) {
		
		Member admin = (Member)session.getAttribute("loginUser");
		if(admin == null || !admin.getIsAdmin().equals("Y")) {
			return "index";
		} 
		
		// 카운트 되는지 확인하고 DB에서 r_rContent 구분자 애들한테 정리하라고 해야함
		if(r != null) {
			int count = aService.selectWarningCount(r);
			System.out.println(count);
			r.setRSituation("W");
			if(count != 0) {
				if(count % 5 == 4) {
					r.setRSituation("B");
					int result1 = aService.updateReportSit(r);
					int result2 = aService.updateStopDate(r.getRAccused());
				} else {
					int result3 = aService.updateReportSit(r);
				}
			} else {
				int result4 = aService.updateReportSit(r);
			}
			
			HashMap<String, Object> notifyMap = new HashMap<>();
			notifyMap.put("mbId", r.getRAccused());	// 타겟
			notifyMap.put("notiType", "AM");
			notifyMap.put("notiContent", "사유 : " + reason + " - 로 인해 경고되었습니다.");
			notifyMap.put("fromUser", admin.getMbId());		// 보내는 사람
			notifyMap.put("refNo", r.getRNo());
			
			int notify = aService.insertNotify(notifyMap);
			
			re.addAttribute("rNo", r.getRNo());
			
			return "redirect:/admin/reportDetail";
		} else {
			return "/adminMain.ad";
		}
	}
	
	@GetMapping("/admin/returnReport.ad")
	public String returnReport(@ModelAttribute Report r,
								HttpSession session,
								RedirectAttributes re,
								@RequestParam("reason") String reason) {
		
		Member admin = (Member)session.getAttribute("loginUser");
		if(admin == null || !admin.getIsAdmin().equals("Y")) {
			return "index";
		} 
		
		System.out.println("rrrr" + r);
		
		if(r != null) {
			r.setRSituation("R");
			aService.updateReportSit(r);
			
			HashMap<String, Object> notifyMap = new HashMap<>();
			notifyMap.put("mbId", r.getRAccused());	// 타겟
			notifyMap.put("notiType", "AM");
			notifyMap.put("notiContent", "사유 : " + reason + " - 로 인해 반려되었습니다.");
			notifyMap.put("fromUser", admin.getMbId());		// 보내는 사람
			notifyMap.put("refNo", r.getRNo());
			
			int notify = aService.insertNotify(notifyMap);
			
			re.addAttribute("rNo", r.getRNo());
			
			return "redirect:/admin/reportDetail";
		} else {
			return "/adminMain.ad";
		}
	}
	
	@GetMapping("/admin/memberManagement.ad")
	public String moveToManagement(@RequestParam(value="mbId", required=false) String mbId,
									@RequestParam(value="page", defaultValue="1") int page,
									Model model,
									HttpServletRequest request) {
		
		int listCount = aService.getMembersCount(mbId);
		PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
		
		 ArrayList<Member> mList = aService.selectAllMembers(pi, mbId);
		 ArrayList<MemberAddress> addrList = aService.selectAllAddress();
		 
		 model.addAttribute("loc", request.getRequestURI());
		 model.addAttribute("pi", pi);
		 model.addAttribute("mList", mList);
		 model.addAttribute("addrList", addrList);
		
		return "memberManagement";
	}
	
	@GetMapping("/admin/membersStop.ad")
	public String manualStopmembers(@RequestParam("mbId") String mbId,
									@RequestParam("reason") String reason,
									HttpSession session) {
		
		int result = aService.updateStopDate(mbId);
		Member admin = (Member)session.getAttribute("loginUser");
		
		if(result > 0) {
			HashMap<String, Object> notifyMap = new HashMap<>();
			notifyMap.put("mbId", mbId);	// 타겟
			notifyMap.put("notiType", "AM");
			notifyMap.put("notiContent", "사유 : " + reason + " - 로 인해 정지되었습니다.");
			notifyMap.put("fromUser", admin.getMbId());		// 보내는 사람
			
			int notify = aService.insertNotify(notifyMap);
			
			return "redirect:/admin/memberManagement.ad";
		} else {
			throw new AdminException(mbId + " 회원 정지를 실패하였습니다.");
		}
	}
	
	@GetMapping("/admin/kickOutMembers.ad")
	public String manualKickOutMembers(@RequestParam("mbId") String mbId,
										@RequestParam("reason") String reason,
										HttpSession session) {
		
		int result = aService.updateKickOutMembers(mbId);
		Member admin = (Member)session.getAttribute("loginUser");
		
		if(result > 0) {
			
			HashMap<String, Object> notifyMap = new HashMap<>();
			notifyMap.put("mbId", mbId);	// 타겟
			notifyMap.put("notiType", "AM");
			notifyMap.put("notiContent", "사유 : " + reason + " - 로 인해 탈퇴되었습니다.");
			notifyMap.put("fromUser", admin.getMbId());		// 보내는 사람
			
			int notify = aService.insertNotify(notifyMap);
			
			return "redirect:/admin/memberManagement.ad";
		} else {
			throw new AdminException(mbId + "회원 강제 탈퇴를 실패하였습니다.");
		}
	}
	
	@GetMapping("/admin/updateInfo.ad")
	@ResponseBody
	public String updateInfo(@RequestParam("column") String column,
							@RequestParam("datas") String datas,
							@RequestParam("mbId") String mbId) {
		
		Properties prop = new Properties();
		column = column.equals("nickName") ? "MB_NICKNAME" : (column.equals("isAdmin") ? "IS_ADMIN" : "MB_EMAIL");
		
		prop.setProperty("column", column);
		prop.setProperty("datas", datas);
		prop.setProperty("mbId", mbId);
		
		String result = aService.updateInfo(prop) == 0 ? "fail" : "success";
		return result;
	}
	
	@GetMapping("/infoMain")
	public String moveToInfo() {
		
		return "infoMain";
	}

}
