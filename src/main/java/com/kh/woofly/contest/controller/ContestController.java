package com.kh.woofly.contest.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kh.woofly.common.PageInfo;
import com.kh.woofly.common.Pagination;
import com.kh.woofly.contest.model.exception.ContestException;
import com.kh.woofly.contest.model.service.ContestService;
import com.kh.woofly.contest.model.vo.Contest;
import com.kh.woofly.contest.model.vo.ContestAttm;
import com.kh.woofly.contest.model.vo.ContestItem;
import com.kh.woofly.contest.model.vo.Participants;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.pet.model.vo.Pet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ContestController {
	
	@Autowired
	private ContestService cService;
	
	// 현재 콘테스트 최신순 리스트
	@GetMapping("/contest/list")
	public String contestList(Model model, HttpSession session, @RequestParam(value="page", defaultValue="1") int page, HttpServletRequest request) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		String id = null;
		int check = 1;
		
		if(loginUser != null) {
			id = loginUser.getMbId();
		}
		//현재 콘태스트 가져오기
		Integer cNo = cService.todayContestNo();
		// 현재 진행중인 콘테스트 없을때 null 반환
		if(cNo == null) {
			return "contestXList";
		}
		int listCount = cService.getListCount(cNo);
		int currentPage = page;
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 12);
		
		ArrayList<Participants> participantstList = cService.participantstList(cNo, pi);
		ArrayList<ContestAttm> cAttmList = cService.selectAttmNList();
		
		if( participantstList != null ) {
			model.addAttribute("check", check);
			model.addAttribute("pi", pi);
			model.addAttribute("id", id);
			model.addAttribute("pList", participantstList);
			model.addAttribute("aList", cAttmList);
			model.addAttribute("loc", request.getRequestURI());
			
			return "contestList";
		} else {
			throw new ContestException("콘테스트 리스트를 불러올 수 없습니다");
		}
	}
	
	// 현재 콘테스트 인기순
	@GetMapping("/contest/bestList")
	public String contestBestList(Model model, HttpSession session, @RequestParam(value="page", defaultValue="1") int page, HttpServletRequest request) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		String id = null;
		int check = 2;
		
		if(loginUser != null) {
			id = loginUser.getMbId();
		}
		//현재 콘태스트 가져오기
		Integer cNo = cService.todayContestNo();
		
		if(cNo == null) {
			return "contestXList";
		}
		
		int listCount = cService.getListCount(cNo);
		int currentPage = page;
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 12);
		
		ArrayList<Participants> participantstList = cService.bestParticipantstList(cNo, pi);
		
		ArrayList<ContestAttm> cAttmList = cService.selectAttmNList();
		
		model.addAttribute("check", check);
		model.addAttribute("pi", pi);
		model.addAttribute("id", id);
		model.addAttribute("pList", participantstList);
		model.addAttribute("aList", cAttmList);
		model.addAttribute("loc", request.getRequestURI());
		
		return "contestList";
	}
	
	//  역대 콘테스트 인기순(최신순없음)
	@GetMapping("/contest/allList")
	public String contestAllList(Model model, HttpSession session, @RequestParam(value="page", defaultValue="1") int page, HttpServletRequest request, @RequestParam(value="selectValue", defaultValue="0") int selectValue) {
		
		int sv = 0;
		
		// 역대 콘테스트로 가져와야함   allCNo.get(0)
		ArrayList<Integer> allCNo = cService.allContestNo();
		
//		System.out.println(allCNo.get(0));
		
		int listCount = 0;
		int currentPage = 0;
		
		
		if(selectValue == 0) {
			sv = allCNo.get(0);
			
			listCount = cService.getListCount(sv);
			currentPage = page;
			
			PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 8);
			int i = 0;
			int generationNo = allCNo.get(i);
			
			ArrayList<Participants> allParticipantstList = cService.allTimeBestList(generationNo, pi);
			
			ArrayList<ContestAttm> cAttmList = cService.selectAttmNList(); //해당 기수만 가져오게 바꿔야 될지도? 
			
			//best 3 강아지 가져오기
			ArrayList<Participants> best3Dog = cService.best3Dog(generationNo);
			if (best3Dog.isEmpty()) {
			    return "contestXList";
			}
			
			model.addAttribute("best3Dog", best3Dog);
			model.addAttribute("allCNo", allCNo);
			model.addAttribute("pi", pi);
			model.addAttribute("pList", allParticipantstList);
			model.addAttribute("aList", cAttmList);
			model.addAttribute("loc", request.getRequestURI());
		} else {
			sv = selectValue;
			
			listCount = cService.getListCount(sv);
			currentPage = page;
			
			PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 8);
			
			int generationNo = sv;
			
			ArrayList<Participants> allParticipantstList = cService.allTimeBestList(generationNo, pi);
			
			ArrayList<ContestAttm> cAttmList = cService.selectAttmNList(); //해당 기수만 가져오게 바꿔야 될지도? 
			
			//best 3 강아지 가져오기
			ArrayList<Participants> best3Dog = cService.best3Dog(generationNo);
			if (best3Dog.isEmpty()) {
			    return "contestXList";
			}
			
			
			model.addAttribute("generationNo", generationNo);
			model.addAttribute("best3Dog", best3Dog);
			model.addAttribute("allCNo", allCNo);
			model.addAttribute("pi", pi);
			model.addAttribute("pList", allParticipantstList);
			model.addAttribute("aList", cAttmList);
			model.addAttribute("loc", request.getRequestURI());
		}
		
		return "contestAllList";
	}
	
	// 현재 콘테스트 검색
	@GetMapping("/contest/searchContestList")
	public String searchContestList(@RequestParam(name = "options-outlined 1", defaultValue="off" ) String check1, @RequestParam(name = "options-outlined 2", defaultValue="off") String check2,
            @RequestParam("search") String search, Model model, HttpSession session, @RequestParam(value="page", defaultValue="1") int page, HttpServletRequest request) {
		// 검색 키워드
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		String id = null;
		
		int check = 0;
		
		if(loginUser != null) {
			id = loginUser.getMbId();
		}
		//현재 콘태스트 가져오기
		int cNo = cService.todayContestNo();
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("cNo", cNo);
		
		int listCount = cService.getListCount(cNo);
		int currentPage = page;
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 12);
		
		// 최신순 리스트
		if(check1.equals("on")) {
			check = 1;
			
			ArrayList<Participants> participantstList = cService.searchParticipantstList(map, pi);
			ArrayList<ContestAttm> cAttmList = cService.selectAttmNList();
			model.addAttribute("search", search);
			model.addAttribute("check", check);
			model.addAttribute("pi", pi);
			model.addAttribute("id", id);
			model.addAttribute("pList", participantstList);
			model.addAttribute("aList", cAttmList);
			model.addAttribute("loc", request.getRequestURI());
			
			return "contestList";
		// 베스트순 리스트
		}else if(check2.equals("on")){
			check = 2;
			
			ArrayList<Participants> participantstList = cService.searchBestParticipantstList(map , pi);
			
			ArrayList<ContestAttm> cAttmList = cService.selectAttmNList();
			
			model.addAttribute("search", search);
			model.addAttribute("check", check);
			model.addAttribute("pi", pi);
			model.addAttribute("id", id);
			model.addAttribute("pList", participantstList);
			model.addAttribute("aList", cAttmList);
			model.addAttribute("loc", request.getRequestURI());
			return "contestList";
		} else {
			throw new ContestException("콘테스트 검색을 실패하였습니다");
		}
	}
	
	// 역대 콘테스트 검색
	@GetMapping("/contest/searchBestContestList")
	public String searchBestContestList( @RequestParam("gen") int gen,
            @RequestParam("search") String search, Model model, HttpSession session, @RequestParam(value="page", defaultValue="1") int page, HttpServletRequest request) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		ArrayList<Integer> allCNo = cService.allContestNo();
		
		String id = null;
		
		if(loginUser != null) {
			id = loginUser.getMbId();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("cNo", gen);
		
		//그 기수 최고 3마리 
		ArrayList<Participants> best3Dog = cService.best3Dog(gen);
		
		ArrayList<Participants> searchListCount = cService.searchParticipantstList(map, null);
		
//		System.out.println(searchListCount.size());
		
		int listCount = searchListCount.size();
		int currentPage = page;
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 12);
		
		//검색
		ArrayList<Participants> participantstList = cService.searchBestParticipantstList(map, pi);
			
		ArrayList<ContestAttm> cAttmList = cService.selectAttmNList();
		
		model.addAttribute("allCNo", allCNo);
		model.addAttribute("gen", gen);
		model.addAttribute("search", search);
		model.addAttribute("best3Dog", best3Dog);
		model.addAttribute("pi", pi);
		model.addAttribute("id", id);
		model.addAttribute("pList", participantstList);
		model.addAttribute("aList", cAttmList);
		model.addAttribute("loc", request.getRequestURI());
		
		return "contestAllList";
	
	}
	
	
	// 콘테스트 등록
	@GetMapping("/contest/open")
	public String contestOpen(Model model) {
		// 최신 콘테스트 1개값 가져옴
		Contest c = cService.contestNewList();
		
		model.addAttribute("c", c);
		
		return "contestOpen";
	}
	
	// 콘테스트 개최
	@GetMapping("/contest/contestOpen")
	public String contestOpen(@ModelAttribute Contest c, HttpSession session) {
//		String id = ((Member)session.getAttribute("loginUser")).getId();	
//		n.setBoardWriter(id);
		
		int result = cService.contestOpen(c);
		
		if(result > 0) {
			return "redirect:/contest/edit";
		}else {
			throw new ContestException("콘테스트 개최를 실패하였습니다");
		}
	}
	
	// 콘테스트 수정페이지 이동
	@GetMapping("/contest/edit")
	public String contestEdit(Model model) {
		
		Contest c = cService.contestNewList();
		ArrayList<Contest> contestList = cService.contestList();
		
		if(contestList != null) {
			model.addAttribute("c", c);
			model.addAttribute("contestList", contestList);
			
			return "contestEdit";
		}else {
			throw new ContestException("콘테스트 수정페이지 이동을 실패하였습니다");
		}
		
	}
	
	// 콘테스트 수정
	@GetMapping("/contest/contestUpdate")
	public String contestUpdate(@ModelAttribute Contest c, HttpSession session, Model model) {
//		String id = ((Member)session.getAttribute("loginUser")).getId();	
//		n.setBoardWriter(id);
		
		int result = cService.contestUpdate(c);
		ArrayList<Contest> contestList = cService.contestList();
		if(result > 0) {		
			model.addAttribute("c", c);
			model.addAttribute("contestList", contestList);
			return "contestEdit";
		}else {
			throw new ContestException("콘테스트 수정을 실패하였습니다");
		}
	}
	
	// 콘테스트 참가페이지 이동
	@GetMapping("/contest/contestParticipation")
	public String contestParticipation(@ModelAttribute Contest c, HttpSession session, Model model,  HttpServletRequest request) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		String id = loginUser.getMbId();
		
		// 유저의 펫 리스트
		ArrayList<String> petList = cService.petList(id);
		
		int cNo = cService.todayContestNo();
		
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("cNo", cNo);
		 
		// 유저의 콘테스트 참가 기록
		ArrayList<String> cPetList = cService.cPetList(map);
		
		// 펫이 없으면 펫 등록으로 
		if(petList.isEmpty()) {
	//		System.out.println(petList);
		}
		
		// 나의 구매내역 
		ArrayList<ContestItem> itemList = cService.itemList(id);
		
		model.addAttribute("itemList", itemList);
		
		if(itemList != null) {
			model.addAttribute("cPetList", cPetList);
			model.addAttribute("petList", petList);
			model.addAttribute("list", itemList);
			model.addAttribute("loc", request.getRequestURI()); //getRequestURI == contextpath 제외하고 가져옴
			return "contestParticipation";
		} else {
			throw new ContestException("콘테스트 참가에 실패하였습니다");
		}
	}
	
	// 콘테스트 아이템 검색
	@GetMapping("/contest/searchItem")
	@ResponseBody
	public ArrayList<ContestItem> searchItem(@RequestParam("pSearch") String pSearch, Model model) {
		
		ArrayList<ContestItem> list = cService.searchItem(pSearch);
		
		ArrayList<ContestAttm> productList = new ArrayList<ContestAttm>();
		
		
		if(list != null) {
			
			for(int i = 1; i < list.size(); i++) {
				int outO = list.get(i).getProductId();
				
				ContestAttm productAttm = cService.productAttm(outO);
	        	
	        	productList.add(productAttm);
			}
			
			
			return list;
		}else {
			throw new ContestException("콘테스트 참가에 실패하였습니다");
		}
		
	}
	
	// 콘테스트 참가
	@PostMapping("/contest/contestEnroll")
	public String contestEnroll(@ModelAttribute Participants p, HttpSession session, @RequestParam("thumbnailFile") ArrayList<MultipartFile> thumbFiles, @RequestParam(value = "selectedItemsNum", defaultValue = "") ArrayList<String> itemNums) {
		
		LocalDate today = LocalDate.now();
		Contest c = cService.contestId(today);
		
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		String nickName = ((Member)session.getAttribute("loginUser")).getMbNickName();
		Integer pId = Integer.parseInt(p.getPPet());
		String petName = cService.petName(pId);
		int cNo = c.getConNo();
		
		p.setMbId(id);
		p.setMbName(nickName);
		p.setPetId(pId);
		p.setPPet(petName);
		p.setContestId(cNo);
		// 문자열 연결 ( 문자 사이에 + 추가하면서 연결)
		StringBuilder result = new StringBuilder();
        for (String itemNum : itemNums) {
        	
        	if (result.length() > 0) {
                result.append("+");
            }
            result.append(itemNum);
        }
        p.setPProduct(result.toString());
      
        // 포인트 등록 참가 자격 확인 ( 이번 기수에 참가 이력 없으면 마일리지 증가 )
        int countList = cService.countList(p);
        
        if(countList == 0) {
        	cService.contestPoint(p);
        }
//      콘테스트 참가등록 되는곳
		int result1 = cService.contestEnroll(p);
		
		Participants thisParticipant = cService.thisParticipant(pId);
		
		int pNo = thisParticipant.getPNo();
		
		// 썸네일 리네임 과정
		ArrayList<ContestAttm> list = new ArrayList<ContestAttm>();
		
		for(int i = 0; i < thumbFiles.size(); i++) {
			MultipartFile upload = thumbFiles.get(i);
			
			if(!upload.getOriginalFilename().equals("")) {
				
				String[] returnArr = saveFile(upload);
				
				if(returnArr[1] != null) {
					ContestAttm t = new ContestAttm();
					t.setOriginalName(upload.getOriginalFilename());
					t.setRenameName(returnArr[1]);
					t.setAttmPath(returnArr[0]);
					t.setAttmRefNo(pNo);// 참가자번호
					t.setAttmLevel(1);
					list.add(t);
					
					break;
				}
			}
		}
		for(int i = 1; i < thumbFiles.size(); i++) {
			MultipartFile upload = thumbFiles.get(i);
			
			if(!upload.getOriginalFilename().equals("")) {
				
				String[] returnArr = saveFile(upload);
				
				if(returnArr[1] != null) {
					ContestAttm t = new ContestAttm();
					t.setOriginalName(upload.getOriginalFilename());
					t.setRenameName(returnArr[1]);
					t.setAttmPath(returnArr[0]);
					t.setAttmRefNo(pNo);
					t.setAttmLevel(2);
					
					list.add(t);
				}
			}
		}
		int result2 = cService.insertAttm(list);
		
		if( result1 + result2 == list.size() + 1) {
			return "redirect:/contest/list";
		} else {
			for(ContestAttm a : list) {
				deleteFile(a.getRenameName());
			}
			throw new ContestException("콘테스트 참가등록에 실패하였습니다");
		}
	}
	
	private String[] saveFile(MultipartFile upload) {
		
		String root = "C:\\woofly\\";
		String savePath = root + "\\contestFiles";
		
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
		String savePath = root + "\\contestFiles";
		
		File f = new File(savePath + "\\" + renameName);
		if(f.exists()) {
			f.delete();
		}
	}
	
	// 콘테스트 상세페이지 이동
	@GetMapping("/contest/selectContest")
	public String selectNotice(@RequestParam("pNo") int pNo, Model model, @RequestParam(value="page", defaultValue="1") int page, HttpSession session, @RequestParam(value="type", defaultValue="0") int type) {					
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String id = null;
		
		if(loginUser != null) {
			id = loginUser.getMbId();
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("pNo", pNo);
		
		Integer  voteCheck = cService.voteCheck(map);
		
		if( voteCheck > 0 ) {
			voteCheck = 1;
		}
		
		// id 조횟수 시간나면 ㄱ
		Participants p = cService.selectParticipants(pNo, id);
		
		String mbId = p.getMbId();
		
		ArrayList<Pet> petLists = cService.selectDog(mbId);
		
		System.out.println(petLists);
		
		
		
		
		
        // 참가자가 가지고 있는 상품 사진 리스트 ( 중복 상품 제외 )
        ArrayList<ContestAttm> productList = new ArrayList<ContestAttm>();
        
        // 주문번호
		ArrayList<String> withO = new ArrayList<>();
		// 물품번호
        ArrayList<Integer> withoutO = new ArrayList<>();
		
		if(p.getPProduct() != null) {
	//		System.out.println(p.getPProduct());
			// 콘테스트 아이템
			String[] parts = p.getPProduct().split("\\+");
	        
	        // 분리된 각 부분을 검사하여 withO 또는 withoutO에 추가
	        for (String part : parts) {
	            if (part.startsWith("o")) {
	                withO.add(part.substring(1)); // 'o'를 제외하고 추가
	            } else {
	                withoutO.add(Integer.parseInt(part));
	            }
	        }
	//        System.out.println(withO);
	        ArrayList<Integer> wOs = new ArrayList<>();
	        for(String wO : withO) {
//	        	System.out.println(Integer.parseInt(wO));
	        	Integer wO2 = cService.wOProductList(wO);
	        	 wOs.add(wO2);
	        }
	        
//	        System.out.println(wOs);
	        
	        // withoutO에 중복 없이 추가
	        Set<Integer> withoutOSet = new HashSet<>(withoutO);
	        withoutOSet.addAll(wOs);

	        // 중복이 제거된 리스트 얻기
	        withoutO.clear();
	        withoutO.addAll(withoutOSet);
	        
	        // 결과 출력
	        for(Integer outO : withoutO) {
//	        	System.out.println(outO);
	        	
	        	ContestAttm productAttm = cService.productAttm(outO);
	        	
	        	productList.add(productAttm);
	        }
		}
		
		if(p != null) {
			int pNum = p.getPNo();
			
			ArrayList<ContestAttm> pList = cService.selectAttmPList(pNum);
			ArrayList<ContestAttm> mList = new ArrayList<ContestAttm>();
			ArrayList<ContestAttm> aList = new ArrayList<ContestAttm>();
			
			//파일이 있을때만 실행
			if( !pList.isEmpty() ) {
				for(int i = 0; i < pList.size(); i++) {
					int level = pList.get(i).getAttmLevel();

					if(pList.get(i).getAttmLevel() == 1) {
						// 썸네일
						mList.add(pList.get(i));
					}else if(level == 2){
						// 그외
						aList.add(pList.get(i));
					}else {
						System.out.println("오류");
					}
				}
			}
			model.addAttribute("type", type);
			model.addAttribute("withoutO", withoutO);
			model.addAttribute("productList", productList);
			model.addAttribute("pList", pList);
			model.addAttribute("voteCheck", (int)voteCheck);
			model.addAttribute("pNo", pNo);
			model.addAttribute("mList", mList);
			model.addAttribute("aList", aList);
			model.addAttribute("p", p);
			model.addAttribute("page", page);
			return "contestDetail";
		} else {
			throw new ContestException("콘테스트를 불러올 수 없습니다.");
		}
	}	
	
	// 콘테스트 투표
		@GetMapping("/contest/bestDogVote")
		public String bestDogVote(@RequestParam("pNo") int pNo, HttpSession session) {
			// 최신 콘테스트 1개값 가져옴
			Member loginUser = (Member)session.getAttribute("loginUser");
			
			String id = null;
			
			if(loginUser != null) {
				id = loginUser.getMbId();
			}
			
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			map.put("pNo", pNo);
			
			Integer  voteCheck = cService.voteCheck(map);
			
			if(voteCheck == 0) {
				int result1 = cService.bestDogVote(map);
				
				if( result1 == 1) {
					int result2 = cService.bestDogCountUpdate(pNo);
					
					return  "redirect:/contest/list";
				} else {
					throw new ContestException("콘테스트 투표 실패하였습니다");
				}
			} else {
				throw new ContestException("콘테스트 투표 실패하였습니다");
			}
			
		}
		
		
		
		
	
}








//01240902