package com.kh.woofly.info.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.woofly.common.PageInfo;
import com.kh.woofly.common.Pagination;
import com.kh.woofly.info.model.service.InfoService;
import com.kh.woofly.info.model.vo.Company;
import com.kh.woofly.info.model.vo.Notice;
import com.kh.woofly.info.model.vo.NoticeAttm;
import com.kh.woofly.info.model.vo.QNA;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.shop.model.exception.ShopException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class InfoController {

	@Autowired
	private InfoService iService;
	
// --------------------------    NOTICE     ----------------------------------- 
	
	//  공지사항 리스트 이동
	@GetMapping("/info/notice")
	public String noticeList(@RequestParam(value="value", defaultValue="") String value, @RequestParam(value="page", defaultValue="1") int page, Model model, HttpServletRequest request, HttpSession session) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String admin = null;
		
		if(loginUser != null) {
			admin = loginUser.getIsAdmin();
		}
		
		int i = 3;
		
		value = null;
		
		int listCount = iService.getListCount(i);
		int currentPage = page;
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 7);
		ArrayList<Notice> list = iService.selectNoticeList(pi);
		
		if(list != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);
			model.addAttribute("loc", request.getRequestURI()); //getRequestURI == contextpath 제외하고 가져옴
			model.addAttribute("value", value);
			model.addAttribute("admin", admin);
			return "noticeList";
		} else {
			return null;
		}
	}
	
	// 공지사항 작성페이지 이동
	@GetMapping("/info/noticeWrite")
	public String noticeWrite(Model model, HttpSession session) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String id = null;
		String admin = null;
		
		if(loginUser != null) {
			id = loginUser.getMbId();
			admin = loginUser.getIsAdmin();
		}
		
		int nNo = iService.noticeNo() + 1;
		
		model.addAttribute("admin", admin);
		model.addAttribute("nNo", nNo);
		model.addAttribute("id", id);
		
		return "noticeWrite";
	}
	
  // 공지사항 등록
	@PostMapping("/info/insertNotice")
	public String insertNotice(@ModelAttribute Notice n, HttpSession session, @RequestPart(value = "NoticeFile", required = false) ArrayList<MultipartFile> noticeFiles, Model model, RedirectAttributes redirectAttributes) {
		
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String id = null;
		String admin = null;
		
		if(loginUser != null) {
			id = loginUser.getMbId();
			admin = loginUser.getIsAdmin();
			n.setNWriter(id);
		}
		
		model.addAttribute("admin", admin);
		
		// 썸네일 리네임 과정
		ArrayList<NoticeAttm> list = new ArrayList<NoticeAttm>();
		
		
		for(int i = 0; i < noticeFiles.size(); i++) {
			MultipartFile upload = noticeFiles.get(i);
			
			if(!upload.getOriginalFilename().equals("")) {
				
				String[] returnArr = saveFile(upload);
				
				if(returnArr[1] != null) {
					NoticeAttm t = new NoticeAttm();
					t.setOriginalName(upload.getOriginalFilename());
					t.setRenameName(returnArr[1]);
					t.setAttmPath(returnArr[0]);
					t.setAttmRefNo(n.getNNo());// 공지 번호 (N_NO)
					
					String oName = upload.getOriginalFilename();
					// 파일 이름을 '.'을 기준으로 나누기
			        String[] parts = oName.split("\\.");
			        // 파일 확장자 얻기 (마지막 부분)
			        String extension = parts[parts.length - 1];
			        if(extension.equals("jpg") || extension.equals("png") || extension.equals("gif") || extension.equals("jpeg")) {
			        	t.setAttmLevel(1);
			        }else {
			        	t.setAttmLevel(2);
			        }
					list.add(t);
				}
			}
		}
		
		int result = iService.insertNotice(n);
		int result2 = 0;
//		System.out.println("111111111111111111111111111111111111111"+list);
		if(!list.isEmpty()) {
			result2 = iService.insertAttm(list);
		}
		
		if( result + result2 == list.size() + 1) {
			
			return "redirect:/info/notice";
			
		} else {
			for(NoticeAttm a : list) {
				deleteFile(a.getRenameName());
			}
			throw new ShopException("공지 등록 실패하였습니다.");
		}
	}
	
	private String[] saveFile(MultipartFile upload) {
		
		String root = "C:\\woofly\\";
		String savePath = root + "\\noticeFiles";
		
		File folder = new File(savePath);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		
		Date time = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		int ranNum = (int)(Math.random()*100000);
		
		String originFileName = upload.getOriginalFilename();
		String renameFileName = sdf.format(time) + ranNum + originFileName.substring(originFileName.lastIndexOf("."));
		
		String renamePath = folder + "\\" + renameFileName;
		
		try {
			upload.transferTo(new File(renamePath));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] returnArr = new String[2];
		returnArr[0] = savePath;
		returnArr[1] = renameFileName;
		
		return returnArr;
	}
	
	private void deleteFile(String renameName) {
		
		String root = "C:\\woofly\\";
		String savePath = root + "\\noticeFiles";
		
		File f = new File(savePath + "\\" + renameName);
		if(f.exists()) {
			f.delete();
		}
	}
	
	
	// 공지사항 상세페이지 이동
	@GetMapping("/info/selectNotice")
	public String selectNotice(@RequestParam("nNo") int nNo, Model model, @RequestParam("page") int page, HttpSession session) {						
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String admin = null;
		String id = null;
		
		if(loginUser != null) {
			admin = loginUser.getIsAdmin();
			id = loginUser.getMbId();
		}
		// id 조횟수 시간나면
		
		Notice n = iService.selectNotice(nNo, id);
		
		if(n != null) {
			int nNum = n.getNNo();
				
			ArrayList<NoticeAttm> nList = iService.selectAttmNList(nNum);
			ArrayList<NoticeAttm> iList = new ArrayList<NoticeAttm>();
			ArrayList<NoticeAttm> aList = new ArrayList<NoticeAttm>();
			
			//파일이 있을때만 실행
			if( !nList.isEmpty() ) {
				for(int i = 0; i < nList.size(); i++) {
					int level = nList.get(i).getAttmLevel();

					if(nList.get(i).getAttmLevel() == 1) {
						// 사진 파일 리스트
						iList.add(nList.get(i));
					}else if(level == 2){
						// 사진 제외 파일 리스트
						aList.add(nList.get(i));
					}else {
						System.out.println("오류");
					}
				}
			}
			model.addAttribute("admin", admin);
			model.addAttribute("iList", iList);
			model.addAttribute("aList", aList);
			model.addAttribute("n", n);
			model.addAttribute("page", page);
			return "noticeDetail";
		} else {
			return null;
		}
	}
	
	// 공지사항 수정페이지 이동
	@PostMapping("/info/NoticeEdit")
	public String NoticeEdit(@RequestParam("nNo") int nNum, Model model, @RequestParam("page") int page, HttpSession session) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String id = null;
		String admin = null;
		
		Notice n = iService.selectNotice(nNum, null);
		
		
		
		ArrayList<NoticeAttm> aList = iService.selectAttmNList(nNum);
		
		if(loginUser != null) {
			id = loginUser.getMbId();
			admin = loginUser.getIsAdmin();
			n.setNWriter(id);
		}
		
		if(n != null) {
			model.addAttribute("aList", aList);
			model.addAttribute("admin", admin);
			model.addAttribute("n", n);
			model.addAttribute("page", page);
			
			return "NoticeEdit";
		} else {
			return null;
		}
	}
	
	// 공지사항 수정
	@PostMapping("/info/updateNotice")
	public String updateNotice(@ModelAttribute Notice n, @RequestParam("page") int page, RedirectAttributes re, HttpSession session, Model model,
											 @RequestParam(value="checkbox", required=false) String[] checkbox,
											@RequestPart(value = "NoticeFile", required = false) ArrayList<MultipartFile> noticeFiles) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String admin = null;
		
		// noticeFiles 정제하기 - saveFIles
		
//		System.out.println(noticeFiles);
		
		// 썸네일 리네임 과정
		ArrayList<NoticeAttm> list = new ArrayList<NoticeAttm>();
		
		
		for(int i = 0; i < noticeFiles.size(); i++) {
			MultipartFile upload = noticeFiles.get(i);
			
			if(!upload.getOriginalFilename().equals("")) {
				
				String[] returnArr = saveFile(upload);
				
				if(returnArr[1] != null) {
					NoticeAttm t = new NoticeAttm();
					t.setOriginalName(upload.getOriginalFilename());
					t.setRenameName(returnArr[1]);
					t.setAttmPath(returnArr[0]);
					t.setAttmRefNo(n.getNNo());// 공지 번호 (N_NO)
					
					String oName = upload.getOriginalFilename();
					// 파일 이름을 '.'을 기준으로 나누기
			        String[] parts = oName.split("\\.");
			        // 파일 확장자 얻기 (마지막 부분)
			        String extension = parts[parts.length - 1];
			        if(extension.equals("jpg") || extension.equals("png") || extension.equals("gif") || extension.equals("jpeg")) {
			        	t.setAttmLevel(1);
			        }else {
			        	t.setAttmLevel(2);
			        }
					list.add(t);
				}
			}
		}
//		System.out.println(list);
		int result3 = 0;
		int result2 = 0;
		
		if(!list.isEmpty()) {
			result2 = iService.insertAttm(list);
		}
		
		if(checkbox != null) {
			for(int i = 0; i < checkbox.length; i++) {
		//		System.out.println(checkbox[i]);
				// 체크된 리스트 날려버리기
				// 이 때 DB랑 어디? deleteFiles 메소드로 보내서 지우기
				deleteFile(checkbox[i]);
				
				result3 = iService.deleteAttm(checkbox[i]);
			}
		}
		
		
		
//		if(checkbox.contains(",")) {
//			// 여러가지니까 스플릿을해야해
//			String[] checkboxes = checkbox.split(",");
//			System.out.println(checkboxes + "1111");
//		}
		
		
		
		
		
		int result = 0;
		if(loginUser != null) {
			admin = loginUser.getIsAdmin();
			result = iService.updateNotice(n);
		}
		
		if(result > 0) {
			model.addAttribute("admin", admin);
			re.addAttribute("nNo", n.getNNo());
			re.addAttribute("page", page);
			return "redirect:/info/selectNotice";
		} else {
			return null;
		}
	}
	
	// 공지사항 삭제(상태 변경)
	@GetMapping("/info/deleteNotice")
	public String deleteNotice(@RequestParam("nNo") int nNo, @RequestParam("page") int page, RedirectAttributes re, HttpSession session, Model model) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String admin = null;
		int result = 0;
		
		if(loginUser != null) {
			admin = loginUser.getIsAdmin();
			result = iService.deleteNotice(nNo);
		}
		
		if(result > 0) {
			model.addAttribute("admin", admin);
			re.addAttribute("page", page);
			return "redirect:/info/notice";
		} else {
			return null;
		}
	}
	
	//  공지사항 검색
	@GetMapping("/info/searchNotice")
	public String searchNotice(@RequestParam("value") String value, @RequestParam("Category") String Category, @RequestParam("search") String search, @RequestParam(value="page", defaultValue="1") int page, Model model, HttpServletRequest request) {
		
		String table = "notice";
		String status = "n_status";
		String abc = "N_CATEGORY";
		String type = "";
		
		Properties prop = new Properties();
		int currentPage = page;
		int listCount = 0;
		
		if(Category.equals("all")) {
			
			if(value == "") {
				type = "1";
				String title = "n_Title";
				String content = "n_Content";
				
				prop.setProperty("value", value);
				prop.setProperty("type", type);
				prop.setProperty("table", table);
				prop.setProperty("search", search);
				prop.setProperty("Category", Category);
				prop.setProperty("status", status);
				prop.setProperty("title", title);
				prop.setProperty("content", content);
			}else {
				type = "4";
				String title = "n_Title";
				String content = "n_Content";
				
				prop.setProperty("abc", abc);
				prop.setProperty("value", value);
				prop.setProperty("type", type);
				prop.setProperty("table", table);
				prop.setProperty("search", search);
				prop.setProperty("Category", Category);
				prop.setProperty("status", status);
				prop.setProperty("title", title);
				prop.setProperty("content", content);
			}
			
			listCount = iService.searchCount(prop);
			
		} else {
			prop.setProperty("table", table);
			prop.setProperty("search", search);
			prop.setProperty("Category", Category);
			prop.setProperty("status", status);
			if(value == "") {
				type = "3";
				prop.setProperty("type", type);
			} else {
				type = "2";
				
				prop.setProperty("abc", abc);
				prop.setProperty("type", type);
				prop.setProperty("value", value);
			}
			listCount = iService.searchCount(prop);
			
		}
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 7);
		ArrayList<Notice> list;
		
		if(value == ""){
			if(Category.equals("all")) {
				type = "1";
			}else {
				type = "2";
			}
			prop.setProperty("type", type);
			
			list = iService.searchAllNotice(pi, prop);
		} else {
			if(Category.equals("all")) {
				type = "1";
				prop.setProperty("type", type);
			}else {
				type = "2";
				prop.setProperty("type", type);
			}
			
			list = iService.searchNotice(pi, prop);
		}
		
		if(list != null) {
			model.addAttribute("value", value);
			model.addAttribute("Category", Category);
			model.addAttribute("search", search);
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);
			model.addAttribute("loc", request.getRequestURI());
			
			return "noticeList";
		} else {
			return null;
		}
	}
	
//  공지사항 카테고리 선택
	@GetMapping("/info/selectCategoryNotice")
	public String selectCategoryCompany(@RequestParam("value") String value, @RequestParam(value="page", defaultValue="1") int page, Model model, HttpServletRequest request) {
		
		String table = "notice";
		String status = "n_status";
		String Category = "n_category";
		
		Properties prop = new Properties();
		prop.setProperty("value", value);
		prop.setProperty("table", table);
		prop.setProperty("status", status);
		prop.setProperty("Category", Category);
		
		int listCount = iService.selectCount(prop);
		int currentPage = page;
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 7);
		ArrayList<Notice> list = iService.selectNoticeCategory(pi, prop);
		
		if(list != null) {
			model.addAttribute("value", value);
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);
			model.addAttribute("loc", request.getRequestURI());
			
			return "noticeList";
			
		} else {
			return null;
		}
	}
	
	
// --------------------------    QNA     ----------------------------------- 	
	
	// QNA(FAQ) 리스트 이동
	@GetMapping("/info/QNA")
	public String QNAList(@RequestParam(value="page", defaultValue="1") int page, Model model, HttpServletRequest request, HttpSession session) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String admin = null;
		
		if(loginUser != null) {
			admin = loginUser.getIsAdmin();
		}
		
		int i = 2;
		
		int listCount = iService.getListCount(i);
		int currentPage = page;

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5);
		ArrayList<QNA> list = iService.selectQNAList(pi);
		
		if(list != null) {
			model.addAttribute("admin", admin);
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);
			model.addAttribute("loc", request.getRequestURI()); //getRequestURI == contextpath 제외하고 가져옴
			
			return "QNAList";
		} else {
			return null;
		}
	}
	
	// QNA(FAQ) 작성페이지 이동
	@GetMapping("/info/QNAWrite")
	public String QNAWrite(HttpSession session, Model model) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String admin = null;
		
		if(loginUser != null) {
			admin = loginUser.getIsAdmin();
			
			model.addAttribute("admin", admin);
			
			return "QNAWrite";
		} else {
			return null;
		}
	}
	
	// QNA(FAQ) 등록
	@PostMapping("/info/insertQNA")
	public String insertQNA(@ModelAttribute QNA q, HttpSession session, Model model) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String id = null;
		String admin = null;
		
		if(loginUser != null) {
			admin = loginUser.getIsAdmin();
			id = loginUser.getMbId();
			q.setQWriter(id);
		}
		
		int result = iService.insertQNA(q);
		
		if(result > 0) {
			model.addAttribute("admin", admin);
			
			return "redirect:/info/QNA";
		} else {
			return null;
		}
	}
	
	// QNA(FAQ) 수정페이지 이동
	@PostMapping("/info/QNAEdit")
	public String noticeEdit(@RequestParam("qId") int qId, Model model, @RequestParam("page") int page, HttpSession session) {						

		Member loginUser = (Member)session.getAttribute("loginUser");
		String admin = null;
		
		if(loginUser != null) {
			admin = loginUser.getIsAdmin();
			QNA qna = iService.selectQNA(qId);
			if(qna != null) {
				model.addAttribute("qna", qna);
				model.addAttribute("page", page);
				model.addAttribute("admin", admin);
				
				return "QNAEdit";
			} else {
				// 수정 실패
				return null;
			}
		}else {
			// 로그인확인
			return null;
		}
		
	}	
	
	// QNA(FAQ) 수정
	@PostMapping("/info/updateQNA")
	public String updateQNA(@ModelAttribute QNA q, @RequestParam("page") int page, RedirectAttributes re, HttpSession session, Model model) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String admin = null;
		int result = 0;
		
		if(loginUser != null) {
			admin = loginUser.getIsAdmin();
			result = iService.updateQNA(q);
		}
		
		if(result > 0) {
			model.addAttribute("admin", admin);
			re.addAttribute("page", page);
			return "redirect:/info/QNA";
		} else {
			return null;
		}
	}
	
	// QNA(FAQ) 삭제(상태 변경)
	@GetMapping("/info/deleteQNA")
	public String deleteQNA(@RequestParam("qId") int qId, @RequestParam("page") int page, RedirectAttributes re, HttpSession session, Model model) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String admin = null;
		int result = 0;
		
		if(loginUser != null) {
			admin = loginUser.getIsAdmin();
	//		String id = loginUser.getMbId();
			// 관리자중에 삭제한사람으로 업데이트?
			result = iService.deleteQNA(qId);
		}
		
		if(result > 0) {
			model.addAttribute("admin", admin);
			re.addAttribute("page", page);
			return "redirect:/info/QNA";
		} else {
			return null;
		}
	}
	
	//  QNA(FAQ) 검색
	@GetMapping("/info/searchQNA")
	public String searchQNA(@RequestParam("value") String value, @RequestParam("Category") String Category, @RequestParam("search") String search, @RequestParam(value="page", defaultValue="1") int page, Model model, HttpServletRequest request) {
	
		String table = "QNA";
		String status = "q_status";
		String id = "Q_ID";
		String abc = "Q_CATEGORY";
		String type = "";
		
		Properties prop = new Properties();
		int currentPage = page;
		int listCount = 0;
		
		if(Category.equals("all")) {
			
			if(value == "") {
				type = "1";
				String title = "q_Title";
				String content = "q_Content";
				
				prop.setProperty("value", value);
				prop.setProperty("type", type);
				prop.setProperty("table", table);
				prop.setProperty("search", search);
				prop.setProperty("Category", Category);
				prop.setProperty("status", status);
				prop.setProperty("title", title);
				prop.setProperty("content", content);
				prop.setProperty("id", id);
			}else{
				type = "4";
				String title = "q_Title";
				String content = "q_Content";
				
				prop.setProperty("abc", abc);
				prop.setProperty("value", value);
				prop.setProperty("type", type);
				prop.setProperty("table", table);
				prop.setProperty("search", search);
				prop.setProperty("Category", Category);
				prop.setProperty("status", status);
				prop.setProperty("title", title);
				prop.setProperty("content", content);
				prop.setProperty("id", id);
			}
			
			listCount = iService.searchCount(prop);
		} else {
			
			prop.setProperty("table", table);
			prop.setProperty("search", search);
			prop.setProperty("Category", Category);
			prop.setProperty("status", status);
			prop.setProperty("id", id);
			
			if(value == "") {
				type = "3";
				prop.setProperty("type", type);
			} else {
				type = "2";
				
				prop.setProperty("abc", abc);
				prop.setProperty("type", type);
				prop.setProperty("value", value);
			}
			
			listCount = iService.searchCount(prop);
		}
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5);
		
		ArrayList<QNA> list; // = iService.searchQNA(pi, prop);
		
		if(value == ""){
			if(Category.equals("all")) {
				type = "1";
			}else {
				type = "2";
			}
			prop.setProperty("type", type);
			
			list = iService.searchAllQNA(pi, prop);
		} else {
			if(Category.equals("all")) {
				type = "1";
				prop.setProperty("type", type);
			}else {
				type = "2";
				prop.setProperty("type", type);
			}
			
			list = iService.searchQNA(pi, prop);
		}
		
		
		if(list != null) {
			model.addAttribute("value", value);
			model.addAttribute("Category", Category);
			model.addAttribute("search", search);
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);
			model.addAttribute("loc", request.getRequestURI());
			
			return "QNAList";
			
		} else {
			return null;
		}
	}
	
//  QNA(FAQ) 카테고리 선택
	@GetMapping("/info/selectCategoryQNA")
	public String selectCategory(@RequestParam("value") String value , @RequestParam(value="page", defaultValue="1") int page, Model model, HttpServletRequest request) {
		
		String table = "QNA";
		String status = "q_status";
		String Category = "q_category";
		
		Properties prop = new Properties();
		prop.setProperty("value", value);
		prop.setProperty("table", table);
		prop.setProperty("status", status);
		prop.setProperty("Category", Category);
		
		int listCount = iService.selectCount(prop);
		int currentPage = page;
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5);
		ArrayList<QNA> list = iService.selectQNACategory(pi, prop);
		
		if(list != null) {
			model.addAttribute("value", value);
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);
			model.addAttribute("loc", request.getRequestURI());
			
			return "QNAList";
			
		} else {
			return null;
		}
	}
	
	
// --------------------------    COMPANY     ----------------------------------- 	
	
	// 협력업체 리스트 이동
	@GetMapping("/info/company")
	public String company(@RequestParam(value="page", defaultValue="1") int page, Model model, HttpServletRequest request, HttpSession session) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String admin = null;
		
		if(loginUser != null) {
			admin = loginUser.getIsAdmin();
		}
		
		
		int i = 1;
		
		int listCount = iService.getListCount(i);
		int currentPage = page;
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 7);
		ArrayList<Company> list = iService.selectCompanyList(pi);
				
		if(list != null) {
			model.addAttribute("admin", admin);
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);
			model.addAttribute("loc", request.getRequestURI()); //getRequestURI == contextpath 제외하고 가져옴
			
			return "companyList";
		} else {
			return null;
		}				
	}
	
	// 협력업체 등록 페이지
	@GetMapping("/info/companyEnroll")
	public String companyEnroll(HttpSession session) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser != null) {
			return "companyEnroll";
		} else {
			return null;
		}
		
	}
	
	// 협력업체 등록
	@PostMapping("/info/insertCompany")
	public String insertCompany(@ModelAttribute Company c, @RequestParam("sample6_postcode")String postcode,@RequestParam("sample6_address")String address, RedirectAttributes redirectAttributes, 
			@RequestParam("sample6_detailAddress")String detailAddress,@RequestParam("sample6_extraAddress")String extraAddress, @RequestParam("tel1") String t1, @RequestParam("tel2") String t2, @RequestParam("tel3") String t3, HttpSession session) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser != null) {
			String subAddress = postcode +"@"+address+"@"+detailAddress+"@"+extraAddress;
			String phone = t1+"-"+t2+"-"+t3;
			
			c.setComAddr(subAddress);
			c.setComPhone(phone);
			
			int result = iService.insertCompany(c);
			
			int comNo = iService.selectComNo();
			int page = 1;
//			System.out.println("asdsadasdsadsadsad"+comNo);
			
			if(result > 0) {
				redirectAttributes.addAttribute("comNo", comNo);
				redirectAttributes.addAttribute("page", page);
				return "redirect:/info/selectCompany";
			}else {
				return null;
			}	
		} else{
			return null;
		}
		
	}
	
	// 협력업체 상세페이지 이동
	@GetMapping("/info/selectCompany")
	public String selectCompany(@RequestParam("comNo") int comNo, Model model, @RequestParam("page") int page, HttpSession session) {						
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String admin = null;
		
		if(loginUser != null) {
			admin = loginUser.getIsAdmin();
		}
		
		Company c = iService.selectCompany(comNo);
		
		if(c != null) {
			model.addAttribute("admin", admin);
			model.addAttribute("c", c);
			model.addAttribute("page", page);
			return "companyDetail";
		} else {
			return null;
		}
	}
	
	// 협력업체 수정페이지 이동
	@PostMapping("/info/CompanyEdit")
	public String CompanyEdit(@RequestParam("comNo") int comNo, Model model, @RequestParam("page") int page, HttpSession session) {						
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		Company c = iService.selectCompany(comNo);
		
		if(loginUser != null) {
			if(c != null) {
				model.addAttribute("c", c);
				model.addAttribute("page", page);
				
				return "CompanyEdit";
			} else {
				return null;
			}
		}else {
			return null;
		}
	}	
	
	// 협력업체 수정
	@PostMapping("/info/updateCompany")
	public String updateCompany(@ModelAttribute Company c, @RequestParam("page") int page, @RequestParam("sample6_postcode")String postcode,@RequestParam("sample6_address")String address, HttpSession session,
			@RequestParam("sample6_detailAddress")String detailAddress,@RequestParam("sample6_extraAddress")String extraAddress, @RequestParam("tel1") String t1, @RequestParam("tel2") String t2, @RequestParam("tel3") String t3, RedirectAttributes re) {
		
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser != null) {
			String subAddress = postcode +"@"+address+"@"+detailAddress+"@"+extraAddress;
			String phone = t1+"-"+t2+"-"+t3;
			c.setComAddr(subAddress);
			c.setComPhone(phone);
			
			int result = iService.updateCompany(c);
			
			if(result > 0) {
				re.addAttribute("comNo", c.getComNo());
				re.addAttribute("page", page);
				return "redirect:/info/selectCompany";
			} else {
				return null;
			}
		}else {
			return null;
		}
		
	}
	
	// 협력업체 삭제(상태 변경)
	@GetMapping("/info/deleteCompany")
	public String deleteCompany(@RequestParam("comNo") int comNo, @RequestParam(value="page", defaultValue="1") int page, RedirectAttributes re, HttpSession session) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser != null) {
			int result = iService.deleteCompany(comNo);
			
			if(result > 0) {
				re.addAttribute("page", page);
				
				return "redirect:/info/company";
			} else {
				return null;
			}
		}else {
			return null;
		}
		
	}
	
	// 협력업체 검색
	@GetMapping("/info/searchCompany")
	public String searchCompany(@RequestParam("comCategory") String comCategory, 
			@RequestParam("Category") String Category, @RequestParam("search") String search, @RequestParam(value="page", defaultValue="1") int page, Model model, HttpServletRequest request) {
		
		String table = "Company";
		String status = "COM_STATUS";
		
		Properties prop = new Properties();
		ArrayList<Company> list;
		PageInfo pi;
		
		int listCount = 0;
		int currentPage = 0;
		
		if(comCategory.equals("all")) {
			
			String type = "3";
			
			prop.setProperty("type", type);
			prop.setProperty("table", table);
			prop.setProperty("search", search);
			prop.setProperty("status", status);
			prop.setProperty("Category", Category);
			
			listCount = iService.searchCount(prop);
			currentPage = page;
		}else {
			
			String type = "5";
			
			prop.setProperty("comCategory", comCategory);
			prop.setProperty("type", type);
			prop.setProperty("table", table);
			prop.setProperty("search", search);
			prop.setProperty("status", status);
			prop.setProperty("Category", Category);
			
			listCount = iService.searchCount(prop);
			currentPage = page;
		}
		
		pi = Pagination.getPageInfo(currentPage, listCount, 7);
		list = iService.searchCompany(pi, prop);

		if(list != null) {
			model.addAttribute("comCategory", comCategory);
			model.addAttribute("Category", Category);
			model.addAttribute("search", search);
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);
			model.addAttribute("loc", request.getRequestURI()); //getRequestURI == contextpath 제외하고 가져옴
			
			return "companyList";
		} else {
			return null;
		}
	}

}