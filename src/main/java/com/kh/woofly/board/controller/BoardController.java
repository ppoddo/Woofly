
package com.kh.woofly.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.woofly.admin.model.vo.Report;
import com.kh.woofly.board.model.exception.BoardException;
import com.kh.woofly.board.model.service.BoardService;
import com.kh.woofly.board.model.vo.Attachment;
import com.kh.woofly.board.model.vo.Board;
import com.kh.woofly.board.model.vo.DwBoard;
import com.kh.woofly.board.model.vo.LostBoard;
import com.kh.woofly.board.model.vo.UsedBoard;
import com.kh.woofly.board.model.vo.WmBoard;
//github.com/younjun1234/WOOFLY.git
import com.kh.woofly.common.PageInfo;
import com.kh.woofly.common.Pagination;
import com.kh.woofly.common.Reply;
import com.kh.woofly.common.ReplyLike;
import com.kh.woofly.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {
	
//		@Autowired
//		private BoardService bService;
		
		//private final BoardService bService;
		
		@Autowired
		private BoardService bService;
		
		
		
		// 중고 거래 내역  // 연준이꺼
		@GetMapping("my/usedBuying")
		public String usedBuyingView(@RequestParam(value="page", defaultValue="1") int page, Model model, HttpSession session,
									 @RequestParam(value="startDate", required=false) String startDate, 
									 @RequestParam(value="endDate", required=false) String endDate, HttpServletRequest request,
									 @RequestParam(value="sort", defaultValue="orderDate desc") String sort) {
			String id = ((Member)session.getAttribute("loginUser")).getMbId();
			HashMap<String, Object> map = new HashMap<>();
			map.put("id", id);
			try {
		        Date currentDate = new Date();
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTime(currentDate);
		        calendar.add(Calendar.DAY_OF_MONTH, 1);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				if (startDate == null) {
					map.put("startDate", null);
				} else {
					map.put("startDate", sdf.parse(startDate));
				}
		 		if (endDate == null) {
					map.put("endDate", sdf.format(calendar.getTime()));
				} else {
					Date newDate = sdf.parse(endDate);
			        calendar.setTime(newDate);
			        calendar.add(Calendar.DAY_OF_MONTH, 1);
					map.put("endDate", sdf.format(calendar.getTime()));
					
					if (newDate.after(new Date())) {
						
						Calendar newCalendar = Calendar.getInstance();
						newCalendar.setTime(newDate);
						newCalendar.add(Calendar.DAY_OF_MONTH, -1);
						endDate = sdf.format(newCalendar.getTime());
					}
					
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			int listCount = bService.selectMyUsedBuyingCount(map);
			PageInfo pi = new Pagination().getPageInfo(page, listCount, 10);
			map.put(sort.split(" ")[0], sort.split(" ")[1]);

			ArrayList<UsedBoard> list = bService.selectMyUsedBuying(pi, map);
//			ArrayList<Attachment> aList = new ArrayList<>();

//			for(UsedBoard ub : list) {
//				aList.add(bService.selectUsedAttm(ub.getUNo()));
//			}
			
			if (list != null) {
				model.addAttribute("sort", sort);
				model.addAttribute("startDate", startDate);
				model.addAttribute("endDate", endDate);
				model.addAttribute("loc", request.getRequestURI());
				model.addAttribute("list", list);
//				model.addAttribute("aList", aList);
				model.addAttribute("pi", pi);
				return "myUsedBuying";
				
			} else {
				throw new BoardException("중고 거래 조회에 실패하였습니다");
			}
		}
		
		
		@GetMapping("my/selling")
		public String usedSellingView(@RequestParam(value="page", defaultValue="1") int page, Model model, HttpSession session,
									 @RequestParam(value="startDate", required=false) String startDate, 
									 @RequestParam(value="endDate", required=false) String endDate, HttpServletRequest request,
									 @RequestParam(value="sort", defaultValue="soldDate asc") String sort) {
			String id = ((Member)session.getAttribute("loginUser")).getMbId();
			HashMap<String, Object> map = new HashMap<>();
			map.put("id", id);
			try {
		        Date currentDate = new Date();
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTime(currentDate);
		        calendar.add(Calendar.DAY_OF_MONTH, 1);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				if (startDate == null) {
					map.put("startDate", null);
				} else {
					map.put("startDate", sdf.parse(startDate));
				}
				
		 		if (endDate == null) {
					map.put("endDate", null);
				} else {
					Date newDate = sdf.parse(endDate);
			        calendar.setTime(newDate);
			        calendar.add(Calendar.DAY_OF_MONTH, 1);
					map.put("endDate", sdf.format(calendar.getTime()));
					
					if (newDate.after(new Date())) {
						
						Calendar newCalendar = Calendar.getInstance();
						newCalendar.setTime(newDate);
						newCalendar.add(Calendar.DAY_OF_MONTH, -1);
						endDate = sdf.format(newCalendar.getTime());
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			int listCount = bService.selectMySellingCount(map);
			PageInfo pi = new Pagination().getPageInfo(page, listCount, 10);
			map.put(sort.split(" ")[0], sort.split(" ")[1]);

			ArrayList<UsedBoard> list = bService.selectMySelling(pi, map);
//			ArrayList<Attachment> aList = new ArrayList<>();

//			for(UsedBoard ub : list) {
//				aList.add(bService.selectUsedAttm(ub.getUNo()));
//			}
			System.out.println(list);
			if (list != null) {
				model.addAttribute("sort", sort);
				model.addAttribute("startDate", startDate);
				model.addAttribute("endDate", endDate);
				model.addAttribute("loc", request.getRequestURI());
				model.addAttribute("list", list);
//				model.addAttribute("aList", aList);
				model.addAttribute("pi", pi);
				return "mySelling";
				
			} else {
				throw new BoardException("중고 거래 조회에 실패하였습니다");
			}
		}
		
	
		// << 글형식 >>
		// 
		// 1. 자유게시판 //
				
		@GetMapping("/board/free")
		public String freeBoardView(@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value = "searchType", required = false) String searchType,
					@RequestParam(value = "searchKeyword", required = false) String searchKeyword, Model model, HttpServletRequest request) throws BoardException {
			
			if (searchType == null || searchKeyword == null) { // 게시글 검색을 하지 않을 때(=검색어가 없을 때)

				int listCount = bService.getListCount(1);
				
				PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
				ArrayList<Board> list = bService.selectFreeBoardList(pi, 1);		
				ArrayList<Attachment> aList = bService.selectAttmFreeBoardList(null);
				
				//System.out.println(list);
				if(list != null) {
					model.addAttribute("pi", pi);
					model.addAttribute("list", list);
					model.addAttribute("aList", aList);					
					
				} else {
					throw new BoardException("게시글 조회 실패");
				}
				
			  } else { // 게시글 검색을 할 때(= 검색어가 있을 때// searchType(작성자, 글제목, 작성자+글제목), searchKeyword()
	              HashMap<String, String> map = new HashMap<>();
	            map.put("searchKeyword", searchKeyword);
	            map.put("searchType", searchType);
	            
	            int listCount = bService.getListCount(1);
	            PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
	            
	            ArrayList<Board> searchResults = bService.searchFreeBoards(map);
	            ArrayList<Attachment> aList = bService.selectAttmFreeBoardList(null);
	            model.addAttribute("pi", pi);
				model.addAttribute("list", searchResults);
				model.addAttribute("aList", aList);	
	           }
			
			model.addAttribute("loc", request.getRequestURI());
			
			return "freeBoard";

		}
		
		
		@GetMapping("/board/free/detail")
		public String freeBoardDetail(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam("bNo") int bNo, HttpSession session, Model model) throws BoardException {
		
			Member loginUser = (Member)session.getAttribute("loginUser");
			String id = null;
			if(loginUser != null) {
				id = loginUser.getMbId();
			}
			Board b = bService.selectFreeBoard(bNo, id);
			ArrayList<Attachment> list = bService.selectAttmFreeBoardList((Integer)bNo); 
			ArrayList<Reply> rList = bService.selectFreeReply(bNo);
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("bNo", bNo);
			map.put("id", id);
			int likeCount = bService.freeBoardLike(map);			
			ArrayList<ReplyLike> likeList = new ArrayList<>();
			
			for(Reply r: rList) {
				Reply tempR = new Reply();
				tempR.setRNo(r.getRNo());
				tempR.setMbId(id);
				
				likeList.add(bService.selectReplyLike(tempR));
			}
			
			
			if(b != null) {
				model.addAttribute("b", b);
				model.addAttribute("page", page);
				model.addAttribute("list", list);
				model.addAttribute("rList", rList);
				model.addAttribute("aLike", likeCount);
				model.addAttribute("lList", likeList);
				//System.out.println(rList);
				return "freeBoardDetail";
			} else {
				throw new BoardException("게시글 상세보기를 실패하였습니다.");
			}
		}
		
		@GetMapping("/board/free/write")
		public String freeBoardWrite(@ModelAttribute Board b) {
			
			return "freeBoardWrite";
		}
		
		@PostMapping("/board/free/insertFreeBoard")
		public String insertFreeBoard(@ModelAttribute Board b, @RequestParam(value = "file", required = false) ArrayList<MultipartFile> files, HttpSession session, HttpServletRequest request) throws BoardException {
		    String boardWriter = ((Member) session.getAttribute("loginUser")).getMbId();
		    b.setMbId(boardWriter);
		    int result1 = bService.insertFreeBoard(b); // 글 내용을 board 테이블에 저장
		    
		    ArrayList<Attachment> attachments = new ArrayList<>();
		    if (files != null) {
		        for (int i = 0; i < files.size(); i++) {
		            MultipartFile upload = files.get(i);
		            if (!upload.getOriginalFilename().equals("")) {
		                String[] returnArr = saveFile(upload);
		                if (returnArr[1] != null) {
		                    Attachment attachment = new Attachment();
		                    attachment.setOriginalName(upload.getOriginalFilename());
		                    attachment.setRenameName(returnArr[1]);
		                    attachment.setAttmPath(returnArr[0]);
		                    attachment.setAttmRefType("B");
		                    attachment.setAttmRefNo(b.getBNo());
		                    attachments.add(attachment);
		                }
		            }
		        }
		        
		        for (int i = 0; i < attachments.size(); i++) {
		            Attachment a = attachments.get(i);
		            if (i == 0) {
		                a.setAttmLevel(1);
		            } else {
		                a.setAttmLevel(2);
		            }
		        }
		        
		        int result2 = bService.insertFreeAttm(attachments);
		        
		        if (result1 + result2 > 0) {
		            return "redirect:/board/free";
		        } else {
		            for (Attachment a : attachments) {
		                deleteFile(a.getRenameName());
		            }
		            throw new BoardException("게시글 작성을 실패하였습니다.");
		        }
		    } else {
		        if (result1 > 0) {
		            return "redirect:/board/free";
		        } else {
		            throw new BoardException("게시글 작성을 실패하였습니다.");
		        }
		    }
		}

			
		
		
		// 파일 저장소 파일 저장(copy)
		private String[] saveFile(MultipartFile upload) {
			
			String root = "C:\\woofly\\";
			String savePath = root + "\\board";
			
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
			String savePath = root + "\\board";
			
			File f = new File(savePath + "\\" + renameName);
			if(f.exists()) {
				f.delete();
			}
		}
		
		@GetMapping("/board/free/editForm")
		public String freeBoardEditForm(@RequestParam("bNo") int bNo, @RequestParam("page") int page, Model model) {
			
			Board b = bService.selectFreeBoard(bNo,null);
			ArrayList<Attachment> list = bService.selectAttmFreeBoardList(bNo);
			model.addAttribute("b", b);
			model.addAttribute("page", page);
			model.addAttribute("list", list);
			
			return "freeBoardEdit";
		}


		@PostMapping("/board/free/edit")
		public String freeBoardEdit(
		    @ModelAttribute Board b,
		    @RequestParam("page") int page,
		    @RequestParam(value = "deleteAttm", required = false, defaultValue = "") String[] deleteAttm,
		    @RequestParam(value = "file", required = false) ArrayList<MultipartFile> files,
		    HttpServletRequest request,
		    RedirectAttributes redirectAttributes) {

		    int updateBoardResult = 0;

		    // 파일이 존재하는 경우에만 파일 및 DB 업데이트 처리 수행
		    if (files != null) {
		        ArrayList<Attachment> list = new ArrayList<>();
		        for (int i = 0; i < files.size(); i++) {
		            MultipartFile upload = files.get(i);

		            if (!upload.getOriginalFilename().equals("")) {
		                String[] returnArr = saveFile(upload);
		                if (returnArr[1] != null) {
		                    Attachment a = new Attachment();
		                    a.setOriginalName(upload.getOriginalFilename());
		                    a.setRenameName(returnArr[1]);
		                    a.setAttmPath(returnArr[0]);

		                    list.add(a);
		                }
		            }
		        }

		        ArrayList<String> delRename = new ArrayList<>();
		        ArrayList<Integer> delLevel = new ArrayList<>();
		        for (String a : deleteAttm) {
		            if (!a.equals("none")) {
		                String[] split = a.split("/");
		                delRename.add(split[0]);
		                delLevel.add(Integer.parseInt(split[1]));
		            }
		        }

		        int deleteAttmResult = 0;
		        boolean existBeforeAttm = true;
		        if (!delRename.isEmpty()) {
		            deleteAttmResult = bService.deleteFreeAttm(delRename);
		            if (deleteAttmResult > 0) {
		                for (String rename : delRename) {
		                    deleteFile(rename);
		                }
		            }

		            if (delRename.size() == deleteAttm.length) {
		                existBeforeAttm = false;

		            } else {
		                for (int level : delLevel) {
		                    if (level == 1) {
		                        bService.updateAttmLevel(b.getBNo());
		                        break;
		                    }
		                }
		            }
		        }

		        boolean hasExistingFile = deleteAttm.length > 0 && !deleteAttm[0].equals("none"); // 파일이 있는지 확인하는 플래그

		        boolean hasLevelOne = false; // 레벨 1이 있는지 확인하는 플래그

		        for (String a : deleteAttm) {
		            if (!a.equals("none")) {
		                String[] split = a.split("/");
		                int level = Integer.parseInt(split[1]);
		                if (level == 1) {
		                    hasLevelOne = true; // 레벨 1이 있다면 플래그 업데이트
		                    break;
		                }
		            }
		        }

		        for (int i = 0; i < list.size(); i++) {
		            Attachment a = list.get(i);
		            a.setAttmRefNo(b.getBNo());

		            if (hasExistingFile) {
		                a.setAttmLevel(2); // 기존 파일이 있는 경우 추가되는 파일은 모두 레벨 2로 설정
		            } else {
		                if (!hasLevelOne) {
		                    a.setAttmLevel(1); // 기존 파일이 없고 레벨 1이 없는 경우 추가되는 첫 번째 파일은 레벨 1로 설정
		                    hasLevelOne = true; // 레벨 1이 없다면 플래그 업데이트
		                } else {
		                    a.setAttmLevel(2); // 기존 파일이 없고 레벨 1이 있는 경우 추가되는 나머지 파일은 레벨 2로 설정
		                }
		            }
		        }

		        // 파일 및 DB 업데이트 처리 수행
		        updateBoardResult = bService.updateFreeBoard(b);
		        int updateAttmResult = 0;
		        if (!list.isEmpty()) {
		            updateAttmResult = bService.insertFreeAttm(list);
		        }
		    } else {
		        // 파일이 없는 경우에는 DB 업데이트만 수행
		        ArrayList<String> delRename = new ArrayList<>();
		        ArrayList<Integer> delLevel = new ArrayList<>();
		        for (String a : deleteAttm) {
		            if (!a.equals("none")) {
		                String[] split = a.split("/");
		                delRename.add(split[0]);
		                delLevel.add(Integer.parseInt(split[1]));
		            }
		        }

		        int deleteAttmResult = 0;
		        boolean existBeforeAttm = true;
		        if (!delRename.isEmpty()) {
		            deleteAttmResult = bService.deleteFreeAttm(delRename);
		            if (deleteAttmResult > 0) {
		                for (String rename : delRename) {
		                    deleteFile(rename);
		                }
		            }

		            if (delRename.size() == deleteAttm.length) {
		                existBeforeAttm = false;

		            } else {
		                for (int level : delLevel) {
		                    if (level == 1) {
		                        bService.updateAttmLevel(b.getBNo());
		                        break;
		                    }
		                }
		            }
		        }/////////////////////////////level 11111111111111

		        // 파일 삭제만 수행
		        updateBoardResult = bService.updateFreeBoard(b);
		    }

		    if (updateBoardResult > 0) {
		        redirectAttributes.addAttribute("bNo", b.getBNo());
		        redirectAttributes.addAttribute("page", page);

		        return "redirect:/board/free/detail";
		    } else {
		        throw new BoardException("게시글 수정 실패하였습니다.");
		    }
		}

		
		@GetMapping("/board/free/delete")
		public String deleteFreeBoard(@RequestParam("bNo") int bNo) throws BoardException {
			int result1 = bService.deleteFreeBoard(bNo);
			String type = "B";
			HashMap<String, Object> board = new HashMap<>();
			board.put("type", type);
			board.put("bNo", bNo);
			int result2 = bService.statusNAttm(board);
			//System.out.println(bNo);
			if(result1 > 0 || result2> 0) {
				return "redirect:/board/free";
			} else {
				throw new BoardException("게시글 삭제 실패");
			}
		}
				
		@GetMapping(value="/insertFreeReply.yk")
		@ResponseBody
		public String insertFreeReply(@ModelAttribute Reply r, HttpSession session) {
			
			String id = ((Member)session.getAttribute("loginUser")).getMbId();
			r.setMbId(id);
			r.setBType("B");
			int result = bService.insertFreeReply(r);
			
			if(result > 0) {
				return "good";
			} else {
				return "bad";
			}
			
		}
		

		@GetMapping(value="/deleteFreeReply.yk")
		@ResponseBody
		public String deleteFreeReply(@ModelAttribute Reply r) {
			int result = bService.deleteFreeReply(r);
			//System.out.println(r);
			
			ArrayList<Reply> rlist = bService.selectFreeReply(r.getBNo());
			
			JSONArray jArr = new JSONArray();  
			for(Reply reply : rlist) {
				JSONObject json = new JSONObject();  
				json.put("rNo", reply.getRNo());
				json.put("bType", reply.getBType());
				json.put("bNo", reply.getBNo());
				json.put("reContent", reply.getReContent());
				json.put("reDate", reply.getReDate());
				json.put("reLike", reply.getReLike());
				json.put("reDStatus", reply.getReDStatus());
				json.put("mbId", reply.getMbId());
				json.put("mbNickname", reply.getMbNickName());
				jArr.put(json);
				
				return jArr.toString();
			}			
			
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		@GetMapping(value="/updateFreeReply.yk")
		@ResponseBody
		public String updateFreeReply(@ModelAttribute Reply r) {

			int result = bService.updateDwReply(r);
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		@GetMapping("/board/free/report")
		@ResponseBody
		public String freeBoardReport(@ModelAttribute Report rep, @RequestParam("bNo") int bNo) {

		    
		    rep.setRCategory("B");
		    rep.setRType("B");
		    rep.setRBoardNo(bNo);
		    
		    int selectBoardReport = bService.selectBoardReport(rep);
		    System.out.println(selectBoardReport);
		    if (selectBoardReport >0) {
		        // 사용자가 이미 동일한 게시물을 신고함
		        return "existBoardReport";
		    }

		    int result = bService.BoardReport(rep); // 수정된 부분

		    if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
		}
		
		//댓글 신고
		@GetMapping("/insertReplyReport.yk")
		@ResponseBody
		public String insertReport(@ModelAttribute Report rep, HttpSession session){
			String id = ((Member)session.getAttribute("loginUser")).getMbId();
			rep.setRAccuser(id);
			rep.setRCategory("B");
			int checkResult = bService.checkReplyResult(rep);
			int result = bService.insertReplyReport(rep);
			
			if(checkResult > 0) {
				return "exist";
			}
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
		}
		
		//보드 좋아요
		@GetMapping("insertDeleteFreeLike.yk")
		@ResponseBody
		public String insertDeleteFreeBoardLike(@RequestParam("bNo") int bNo, @RequestParam("replyInDel") String replyInDel, HttpSession session) {
			HashMap<String, Object> map = new HashMap<>();
			String id = ((Member)session.getAttribute("loginUser")).getMbId();
			
			map.put("bNo", bNo);
			map.put("id", id);
			
			int result = 0;
			if (replyInDel.equals("delete")) {
				result = bService.deleteFreeBoardLike(map);
			} else {
				result = bService.insertFreeBoardLike(map);
				result = bService.insertFreeBoardNotice(map);
			}
				
			if (result > 0) {
				return "good";
			} else {
				return "bad";
			}
		}

		@GetMapping("insertDeleteFreeReply.yk")
		@ResponseBody
		public String insertDeleteFreeReply(@RequestParam("rNo") int rNo, @RequestParam("replyInDel") String replyInDel, HttpSession session) {
			HashMap<String, Object> map = new HashMap<>();
			String id = ((Member)session.getAttribute("loginUser")).getMbId();

			map.put("rNo", rNo);
			map.put("id", id);
			
			int result = 0;
			if (replyInDel.equals("delete")) {
				result = bService.deleteBoardReplyLike(map);
			} else {
				result = bService.insertBoardReplyLike(map);
				result = bService.insertFreeReplyNotice(map);
			}
				
			if (result > 0) {
				return "good";
			} else {
				return "bad";
						
			}
		}

		
	
		
		// 2. 도그워커 //

		@GetMapping("/board/dw")
		public String dwBoardView(@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value = "searchType", required = false) String searchType,
				@RequestParam(value = "searchKeyword", required = false) String searchKeyword, Model model, HttpServletRequest request) throws BoardException {
			
			if (searchType == null || searchKeyword == null) { // 게시글 검색을 하지 않을 때(=검색어가 없을 때)
			
				int listCount = bService.getDwListCount(1);
			
				PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
				ArrayList<DwBoard> list = bService.selectDwBoardList(pi, 1);		
				ArrayList<Attachment> aList = bService.selectAttmDwBoardList(null);
				
				//System.out.println(list);
				if(list != null) {
					model.addAttribute("pi", pi);
					model.addAttribute("list", list);
					model.addAttribute("aList", aList);
									
				} else {
					throw new BoardException("게시글 조회 실패");
				}
			 } else { // 게시글 검색을 할 때(= 검색어가 있을 때// searchType(작성자, 글제목, 작성자+글제목), searchKeyword()
	              HashMap<String, String> map = new HashMap<>();
	            map.put("searchKeyword", searchKeyword);
	            map.put("searchType", searchType);
	            
	            int listCount = bService.getListCount(1);
	            PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
	            
	            ArrayList<DwBoard> searchResults = bService.searchDwBoards(map);
	            ArrayList<Attachment> aList = bService.selectAttmDwBoardList(null);
	            model.addAttribute("pi", pi);
				model.addAttribute("list", searchResults);
				model.addAttribute("aList", aList);	
	           }
			
			model.addAttribute("loc", request.getRequestURI());
			
			return "dwBoard";
		}
		

		@GetMapping("/board/dwReview")
		public String dwReviewBoard(@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value = "searchType", required = false) String searchType,
				@RequestParam(value = "searchKeyword", required = false) String searchKeyword, Model model, HttpServletRequest request) {
						
			if (searchType == null || searchKeyword == null) { // 게시글 검색을 하지 않을 때(=검색어가 없을 때)
			
				int listCount = bService.getDwRvListCount(1);
			
				
				PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
				ArrayList<DwBoard> list = bService.selectDwRvBoardList(pi, 1);		
				ArrayList<Attachment> aList = bService.selectAttmDwRvBoardList(null);
				
				//System.out.println(list);
				if(list != null) {
					model.addAttribute("pi", pi);
					model.addAttribute("list", list);
					model.addAttribute("aList", aList);
					
				} else {
					throw new BoardException("게시글 조회 실패");
				}
			} else { // 게시글 검색을 할 때(= 검색어가 있을 때// searchType(작성자, 글제목, 작성자+글제목), searchKeyword()
	              HashMap<String, String> map = new HashMap<>();
	            map.put("searchKeyword", searchKeyword);
	            map.put("searchType", searchType);
	            
	            int listCount = bService.getListCount(1);
	            PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
	            
	            ArrayList<DwBoard> searchResults = bService.searchDwRvBoards(map);
	            ArrayList<Attachment> aList = bService.selectAttmDwRvBoardList(null);
	            model.addAttribute("pi", pi);
				model.addAttribute("list", searchResults);
				model.addAttribute("aList", aList);	
	           }
			model.addAttribute("loc", request.getRequestURI());
			
			return "dwReviewBoard";
		}
		@GetMapping("/board/dw/detail")
		public String dwBoardDetail(@RequestParam(value="page", defaultValue="1") int page, @RequestParam("dwNo") int dwNo, HttpSession session, Model model) {
			
			Member loginUser = (Member)session.getAttribute("loginUser");
			String id = null;
			if(loginUser != null) {
				id = loginUser.getMbId();
			}
			DwBoard dw = bService.selectDwBoard(dwNo, id);			
			ArrayList<Attachment> list = bService.selectAttmDwBoardList(dwNo); 
			ArrayList<Reply> rList = bService.selectDwReply(dwNo);
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("dwNo", dwNo);
			map.put("id", id);
			int likeCount = bService.DwBoardLike(map);			
			ArrayList<ReplyLike> likeList = new ArrayList<>();
			
			for(Reply r: rList) {
				Reply tempR = new Reply();
				tempR.setRNo(r.getRNo());
				tempR.setMbId(id);
				
				likeList.add(bService.selectReplyLike(tempR));
			}
			
			
			
			if(dw != null) {
				model.addAttribute("dw", dw);
				model.addAttribute("page", page);
				model.addAttribute("list", list);
				model.addAttribute("rList", rList);
				model.addAttribute("aLike", likeCount);
				model.addAttribute("lList", likeList);
				//System.out.println(rList);
				return "dwBoardDetail";
			} else {
				throw new BoardException("게시글 상세보기를 실패하였습니다.");
			}
		}
		
		@GetMapping("/board/dw/write")
		public String dwBoardWrite() {
			
			return "dwBoardWrite";
		}
		
		@PostMapping("/board/dw/insertDwBoard")
		public String insertDwBoard(@RequestParam("dwType") String dwTypeStr, @ModelAttribute DwBoard dw, @RequestParam(value = "file", required = false) ArrayList<MultipartFile> files, 
									HttpSession session, HttpServletRequest request) {
			
			String boardWriter = ((Member)session.getAttribute("loginUser")).getMbId();
			dw.setMbId(boardWriter);
			
			int dwType = Integer.parseInt(dwTypeStr);
			int result1 = bService.insertDwBoard(dw); 
			
			ArrayList<Attachment> attachments = new ArrayList<>();
			if (files != null) {
				for(int i = 0; i<files.size(); i++) {
					MultipartFile upload = files.get(i);
					if(!upload.getOriginalFilename().equals("")) {
						String[] returnArr = saveFile(upload);
						if(returnArr[1] != null) {
							Attachment attachment = new Attachment();
							attachment.setOriginalName(upload.getOriginalFilename());
							attachment.setRenameName(returnArr[1]);
							attachment.setAttmPath(returnArr[0]);
							attachment.setAttmRefType("DW");
							attachment.setAttmRefNo(dw.getDwNo());
							
							attachments.add(attachment);
						}
					}
				}
				
				for(int i=0; i < attachments.size(); i++) {
					Attachment a = attachments.get(i);
					if(i == 0) {
						a.setAttmLevel(1);
					} else {
						a.setAttmLevel(2);
					}
				}
				
				int result2 = bService.insertDwAttm(attachments);
				//System.out.println(result1);
				//System.out.println(result2);
				if(result1 + result2 > 0) {
					return "redirect:/board/dw";
				} else {
					for(Attachment a : attachments) {
						deleteFile(a.getRenameName());
					}
					throw new BoardException("게시글 작성을 실패하였습니다.");
			    }
			} else {
		        if (result1 > 0) {
		            return "redirect:/board/dw";
		        } else {
		            throw new BoardException("게시글 작성을 실패하였습니다.");
		        }
		    }
			
		}
		
		@GetMapping("/board/dw/editForm")
		public String dwBoardEditForm(@RequestParam("dwNo") int dwNo, @RequestParam("page") int page, Model model) {
			
			DwBoard dw = bService.selectDwBoard(dwNo,null);
			//System.out.println(dw);
			ArrayList<Attachment> list = bService.selectAttmDwBoardList(dwNo);
			model.addAttribute("dw", dw);
			model.addAttribute("page", page);
			model.addAttribute("list", list);
			
			return "dwBoardEdit";
		}
		
		@PostMapping("/board/dw/edit")
		public String dwBoardEdit(
		    @RequestParam("dwType") String dwTypeStr,
		    @ModelAttribute DwBoard dw,
		    @RequestParam("page") int page,
		    @RequestParam(value = "deleteAttm", required = false, defaultValue = "") String[] deleteAttm,
		    @RequestParam(value = "file", required = false) ArrayList<MultipartFile> files,
		    HttpServletRequest request,
		    RedirectAttributes redirectAttributes) {

		    int dwType = Integer.parseInt(dwTypeStr);
		    System.out.println(dwType);

		    ArrayList<Attachment> list = new ArrayList<>();

		    // 파일이 존재하는 경우에만 파일 업로드 및 DB 처리 수행
		    if (files != null) {
		        for (int i = 0; i < files.size(); i++) {
		            MultipartFile upload = files.get(i);

		            if (!upload.getOriginalFilename().equals("")) {
		                String[] returnArr = saveFile(upload);
		                if (returnArr[1] != null) {
		                    Attachment a = new Attachment();
		                    a.setOriginalName(upload.getOriginalFilename());
		                    a.setRenameName(returnArr[1]);
		                    a.setAttmPath(returnArr[0]);

		                    list.add(a);
		                }
		            }
		        }
		    }

		    ArrayList<String> delRename = new ArrayList<>();
		    ArrayList<Integer> delLevel = new ArrayList<>();
		    for (String a : deleteAttm) {
		        if (!a.equals("none")) {
		            String[] split = a.split("/");
		            delRename.add(split[0]);
		            delLevel.add(Integer.parseInt(split[1]));
		        }
		    }

		    int deleteAttmResult = 0;
		    int updateBoardResult = 0;
		    boolean existBeforeAttm = true;

		    if (!delRename.isEmpty()) {
		        deleteAttmResult = bService.deleteDwAttm(delRename);
		        if (deleteAttmResult > 0) {
		            for (String rename : delRename) {
		                deleteFile(rename);
		            }
		        }

		        if (delRename.size() == deleteAttm.length) {
		            existBeforeAttm = false;
		        } else {
		            for (int level : delLevel) {
		                if (level == 0) {
		                    bService.updateAttmLevel(dw.getDwNo());
		                    break;
		                }
		            }
		        }
		    }

		    boolean hasExistingFile = deleteAttm.length > 0 && !deleteAttm[0].equals("none");
		    boolean hasLevelOne = false;

		    for (String a : deleteAttm) {
		        if (!a.equals("none")) {
		            String[] split = a.split("/");
		            int level = Integer.parseInt(split[1]);
		            if (level == 1) {
		                hasLevelOne = true;
		                break;
		            }
		        }
		    }

		    for (int i = 0; i < list.size(); i++) {
		        Attachment a = list.get(i);
		        a.setAttmRefNo(dw.getDwNo());

		        if (hasExistingFile) {
		            a.setAttmLevel(2);
		        } else {
		            if (!hasLevelOne) {
		                a.setAttmLevel(1);
		                hasLevelOne = true;
		            } else {
		                a.setAttmLevel(2);
		            }
		        }
		    }

		    // 파일 및 DB 처리 수행
		    updateBoardResult = bService.updateDwBoard(dw);
		    int updateAttmResult = 0;

		    if (!list.isEmpty()) {
		        updateAttmResult = bService.insertDwAttm(list);
		    }

		    if (updateBoardResult + updateAttmResult > 0) {
		        redirectAttributes.addAttribute("dwNo", dw.getDwNo());
		        redirectAttributes.addAttribute("page", page);
		        return "redirect:/board/dw/detail";
		    } else {
		        throw new BoardException("첨부파일 게시글 수정 실패하였습니다.");
		    }
		}


		
		@GetMapping("/board/dw/delete")
		public String deleteDwBoard(@RequestParam("dwNo") int dwNo) throws BoardException {
			int result1 = bService.deleteDwBoard(dwNo);
			String type = "DW";
			HashMap<String, Object> board = new HashMap<>();
			board.put("type", type);
			board.put("bNo", dwNo);
			int result2 = bService.statusNAttm(board);
			//System.out.println(bNo);
			if(result1 > 0 || result2> 0) {
				return "redirect:/board/dw";
			} else {
				throw new BoardException("게시글 삭제 실패");
			}
		}
		
		@GetMapping(value="/insertDwReply.yk")
		@ResponseBody
		public String insertDwReply(@ModelAttribute Reply r, HttpSession session) {
			String id = ((Member)session.getAttribute("loginUser")).getMbId();
			r.setMbId(id);
			r.setBType("DW");
			int result = bService.insertDwReply(r);
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		@GetMapping(value="/deleteDwReply.yk")
		@ResponseBody
		public String deleteDwReply(@ModelAttribute Reply r) {
			int result = bService.deleteDwReply(r);
			
			//System.out.println(r);
			// 우리 댓글테이블은 공유잖아?
			// 그래서 댓글넘버가 프라이머리키야(고유해)
			// 그래서 너는 1,2,3,4번을 가지고 있어 이건 도그워커 + 그 게시글에서 생성된 댓글
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		@GetMapping(value="/updateDwReply.yk")
		@ResponseBody
		public String updateDwReply(@ModelAttribute Reply r) {

			int result = bService.updateDwReply(r);
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		
		
		@GetMapping("/board/dw/report")
		@ResponseBody
		public String dwBoardReport(@ModelAttribute Report rep, @RequestParam("dwNo") int dwNo) {

		    
		    rep.setRCategory("DW");
		    rep.setRType("B");
		    rep.setRBoardNo(dwNo);
		    
		    int selectBoardReport = bService.selectBoardReport(rep);
		    System.out.println(selectBoardReport);
		    if (selectBoardReport >0) {
		        // 사용자가 이미 동일한 게시물을 신고함
		        return "existBoardReport";
		    }

		    int result = bService.BoardReport(rep); // 수정된 부분

		    if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
		}
		
		//댓글 신고
		@GetMapping("/insertDwReplyReport.yk")
		@ResponseBody
		public String insertDwReport(@ModelAttribute Report rep, HttpSession session){
			String id = ((Member)session.getAttribute("loginUser")).getMbId();
			rep.setRAccuser(id);
			rep.setRCategory("DW");
			int checkResult = bService.checkReplyResult(rep);
			int result = bService.insertReplyReport(rep);
			
			if(checkResult > 0) {
				return "exist";
			}
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
		}
		
		//보드 좋아요
				@GetMapping("insertDeleteDwLike.yk")
				@ResponseBody
				public String insertDeleteDwLike(@RequestParam("dwNo") int dwNo, @RequestParam("replyInDel") String replyInDel, HttpSession session) {
					HashMap<String, Object> map = new HashMap<>();
					String id = ((Member)session.getAttribute("loginUser")).getMbId();
					
					map.put("dwNo", dwNo);
					map.put("id", id);
					
					int result = 0;
					if (replyInDel.equals("delete")) {
						result = bService.deleteDwBoardLike(map);
					} else {
						result = bService.insertDwBoardLike(map);
						result = bService.insertDwBoardNotice(map);
					}
						
					if (result > 0) {
						return "good";
					} else {
						return "bad";
					}
				}

				@GetMapping("insertDeleteDwReply.yk")
				@ResponseBody
				public String insertDeleteDwReply(@RequestParam("rNo") int rNo, @RequestParam("replyInDel") String replyInDel, HttpSession session) {
					HashMap<String, Object> map = new HashMap<>();
					String id = ((Member)session.getAttribute("loginUser")).getMbId();

					map.put("rNo", rNo);
					map.put("id", id);
					
					int result = 0;
					if (replyInDel.equals("delete")) {
						result = bService.deleteBoardReplyLike(map);
					} else {
						result = bService.insertBoardReplyLike(map);
						result = bService.insertDwReplyNotice(map);
					}
						
					if (result > 0) {
						return "good";
					} else {
						return "bad";
								
					}
				}

			
		// 3. 산책메이트 //
		
		@GetMapping("/board/wm")
		public String wmBoardView(@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value = "searchType", required = false) String searchType,
				@RequestParam(value = "searchKeyword", required = false) String searchKeyword, Model model, HttpServletRequest request) {
			
			if (searchType == null || searchKeyword == null) { // 게시글 검색을 하지 않을 때(=검색어가 없을 때)
				
				int listCount = bService.getWmListCount(1);
				
				PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
				ArrayList<WmBoard> list = bService.selectWmBoardList(pi, 1);		
				ArrayList<Attachment> aList = bService.selectAttmWmBoardList(null);
				
				//System.out.println(list);
				if(list != null) {
					model.addAttribute("pi", pi);
					model.addAttribute("list", list);
					model.addAttribute("aList", aList);
					
				} else {
					throw new BoardException("게시글 조회 실패");
				}
			 } else { // 게시글 검색을 할 때(= 검색어가 있을 때// searchType(작성자, 글제목, 작성자+글제목), searchKeyword()
	              HashMap<String, String> map = new HashMap<>();
	            map.put("searchKeyword", searchKeyword);
	            map.put("searchType", searchType);
	            
	            int listCount = bService.getListCount(1);
	            PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
	            
	            ArrayList<WmBoard> searchResults = bService.searchWmBoards(map);
	            ArrayList<Attachment> aList = bService.selectAttmWmBoardList(null);
	            model.addAttribute("pi", pi);
				model.addAttribute("list", searchResults);
				model.addAttribute("aList", aList);	
	           }
			
			model.addAttribute("loc", request.getRequestURI());
			
			return "wmBoard";
			
		}
		
		@GetMapping("/board/wmReview")
		public String wmReviewBoard(@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value = "searchType", required = false) String searchType,
				@RequestParam(value = "searchKeyword", required = false) String searchKeyword, Model model, HttpServletRequest request) {
			
			if (searchType == null || searchKeyword == null) { // 게시글 검색을 하지 않을 때(=검색어가 없을 때)
				int listCount = bService.getWmRvListCount(1);
				
				PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
				ArrayList<WmBoard> list = bService.selectWmRvBoardList(pi, 1);		
				ArrayList<Attachment> aList = bService.selectAttmWmRvBoardList(null);
				
				//System.out.println(list);
				if(list != null) {
					model.addAttribute("pi", pi);
					model.addAttribute("list", list);
					model.addAttribute("aList", aList);
					
				} else {
					throw new BoardException("게시글 조회 실패");
				}
			
			} else { // 게시글 검색을 할 때(= 검색어가 있을 때// searchType(작성자, 글제목, 작성자+글제목), searchKeyword()
	              HashMap<String, String> map = new HashMap<>();
	            map.put("searchKeyword", searchKeyword);
	            map.put("searchType", searchType);
	            
	            int listCount = bService.getListCount(1);
	            PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
	            
	            ArrayList<WmBoard> searchResults = bService.searchWmRvBoards(map);
	            ArrayList<Attachment> aList = bService.selectAttmWmRvBoardList(null);
	            model.addAttribute("pi", pi);
				model.addAttribute("list", searchResults);
				model.addAttribute("aList", aList);	
	           }
			
			model.addAttribute("loc", request.getRequestURI());
			
			return "wmReviewBoard";
		}
		
		@GetMapping("/board/wm/detail")
		public String wmBoardDetail(@RequestParam(value="page", defaultValue="1") String page, @RequestParam("wmNo") int wmNo, HttpSession session, Model model) {
			
			Member loginUser = (Member)session.getAttribute("loginUser");
			String id = null;
			if(loginUser != null) {
				id = loginUser.getMbId();
			}
			System.out.println(wmNo);
			System.out.println(id);
			WmBoard wm = bService.selectWmBoard(wmNo, id);
			System.out.println(wm);
			ArrayList<Attachment> list = bService.selectAttmWmBoardList(wmNo); 
			ArrayList<Reply> rList = bService.selectWmReply(wmNo);
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("wmNo", wmNo);
			map.put("id", id);
			int likeCount = bService.WmBoardLike(map);			
			ArrayList<ReplyLike> likeList = new ArrayList<>();
			
			for(Reply r: rList) {
				Reply tempR = new Reply();
				tempR.setRNo(r.getRNo());
				tempR.setMbId(id);
				
				likeList.add(bService.selectReplyLike(tempR));
			}
			
			if(wm != null) {
				model.addAttribute("wm", wm);
				model.addAttribute("page", page);
				model.addAttribute("list", list);
				model.addAttribute("rList", rList);
				model.addAttribute("aLike", likeCount);
				model.addAttribute("lList", likeList);
				//System.out.println(rList);
				return "wmBoardDetail";
			} else {
				throw new BoardException("게시글 상세보기를 실패하였습니다.");
			}
			
		}
		
		@GetMapping("/board/wm/write")
		public String wmBoardWrite() {
			
			return "wmBoardWrite";
		}
		
		@PostMapping("/board/wm/insertWmBoard")
		public String insertWmBoard(@RequestParam("wmType") String wmTypeStr, @ModelAttribute WmBoard wm, @RequestParam(value = "file", required = false) ArrayList<MultipartFile> files, HttpSession session, HttpServletRequest request) {
			
			String boardWriter = ((Member)session.getAttribute("loginUser")).getMbId();
			wm.setMbId(boardWriter);
			
			int wmType = Integer.parseInt(wmTypeStr);
			int result1 = bService.insertWmBoard(wm); 
			
			ArrayList<Attachment> attachments = new ArrayList<>();
			if (files != null) {
				for(int i = 0; i<files.size(); i++) {
					MultipartFile upload = files.get(i);
					if(!upload.getOriginalFilename().equals("")) {
						String[] returnArr = saveFile(upload);
						if(returnArr[1] != null) {
							Attachment attachment = new Attachment();
							attachment.setOriginalName(upload.getOriginalFilename());
							attachment.setRenameName(returnArr[1]);
							attachment.setAttmPath(returnArr[0]);
							attachment.setAttmRefType("Wm");
							attachment.setAttmRefNo(wm.getWmNo());
							
							attachments.add(attachment);
						}
					}
				}
				
				for(int i=0; i < attachments.size(); i++) {
					Attachment a = attachments.get(i);
					if(i == 0) {
						a.setAttmLevel(1);
					} else {
						a.setAttmLevel(2);
					}
				}
				
				int result2 = bService.insertWmAttm(attachments);
				//System.out.println(result1);
				//System.out.println(result2);
				if(result1 + result2 > 0) {
					return "redirect:/board/wm";
				} else {
					for(Attachment a : attachments) {
						deleteFile(a.getRenameName());
					}
					throw new BoardException("게시글 작성을 실패하였습니다.");
			    }
			} else {
		        if (result1 > 0) {
		            return "redirect:/board/wm";
		        } else {
		            throw new BoardException("게시글 작성을 실패하였습니다.");
		        }
		    }
			
		}
		
		@GetMapping("/board/wm/editForm")
		public String dwBoardWmitForm(@RequestParam("wmNo") int wmNo, @RequestParam("page") int page, Model model) {
			
			WmBoard wm = bService.selectWmBoard(wmNo,null);
			//System.out.println(dw);
			ArrayList<Attachment> list = bService.selectAttmWmBoardList(wmNo);
			model.addAttribute("wm", wm);
			model.addAttribute("page", page);
			model.addAttribute("list", list);
			
			return "WmBoardEdit";
		}
		
		@PostMapping("/board/wm/edit")
		public String wmBoardEdit(
		    @RequestParam("wmType") String wmTypeStr,
		    @ModelAttribute WmBoard wm,
		    @RequestParam("page") int page,
		    @RequestParam(value = "deleteAttm", required = false, defaultValue = "") String[] deleteAttm,
		    @RequestParam(value = "file", required = false) ArrayList<MultipartFile> files,
		    HttpServletRequest request,
		    RedirectAttributes redirectAttributes) {

		    int wmType = Integer.parseInt(wmTypeStr);
		    System.out.println(wmType);

		    ArrayList<Attachment> list = new ArrayList<>();

		    // 파일이 존재하는 경우에만 파일 업로드 및 DB 처리 수행
		    if (files != null) {
		        for (int i = 0; i < files.size(); i++) {
		            MultipartFile upload = files.get(i);

		            if (!upload.getOriginalFilename().equals("")) {
		                String[] returnArr = saveFile(upload);
		                if (returnArr[1] != null) {
		                    Attachment a = new Attachment();
		                    a.setOriginalName(upload.getOriginalFilename());
		                    a.setRenameName(returnArr[1]);
		                    a.setAttmPath(returnArr[0]);

		                    list.add(a);
		                }
		            }
		        }
		    }

		    ArrayList<String> delRename = new ArrayList<>();
		    ArrayList<Integer> delLevel = new ArrayList<>();
		    for (String a : deleteAttm) {
		        if (!a.equals("none")) {
		            String[] split = a.split("/");
		            delRename.add(split[0]);
		            delLevel.add(Integer.parseInt(split[1]));
		        }
		    }

		    int deleteAttmResult = 0;
		    int updateBoardResult = 0;
		    boolean existBeforeAttm = true;

		    if (!delRename.isEmpty()) {
		        deleteAttmResult = bService.deleteWmAttm(delRename);
		        if (deleteAttmResult > 0) {
		            for (String rename : delRename) {
		                deleteFile(rename);
		            }
		        }

		        if (delRename.size() == deleteAttm.length) {
		            existBeforeAttm = false;
		        } else {
		            for (int level : delLevel) {
		                if (level == 0) {
		                    bService.updateAttmLevel(wm.getWmNo());
		                    break;
		                }
		            }
		        }
		    }

		    boolean hasExistingFile = deleteAttm.length > 0 && !deleteAttm[0].equals("none");
		    boolean hasLevelOne = false;

		    for (String a : deleteAttm) {
		        if (!a.equals("none")) {
		            String[] split = a.split("/");
		            int level = Integer.parseInt(split[1]);
		            if (level == 1) {
		                hasLevelOne = true;
		                break;
		            }
		        }
		    }

		    for (int i = 0; i < list.size(); i++) {
		        Attachment a = list.get(i);
		        a.setAttmRefNo(wm.getWmNo());

		        if (hasExistingFile) {
		            a.setAttmLevel(2);
		        } else {
		            if (!hasLevelOne) {
		                a.setAttmLevel(1);
		                hasLevelOne = true;
		            } else {
		                a.setAttmLevel(2);
		            }
		        }
		    }

		    // 파일 및 DB 처리 수행
		    updateBoardResult = bService.updateWmBoard(wm);
		    int updateAttmResult = 0;

		    if (!list.isEmpty()) {
		        updateAttmResult = bService.insertWmAttm(list);
		    }

		    if (updateBoardResult + updateAttmResult > 0) {
		        redirectAttributes.addAttribute("wmNo", wm.getWmNo());
		        redirectAttributes.addAttribute("page", page);
		        return "redirect:/board/wm/detail";
		    } else {
		        throw new BoardException("첨부파일 게시글 수정 실패하였습니다.");
		    }
		}

		
		@GetMapping("/board/wm/delete")
		public String deleteWmBoard(@RequestParam("wmNo") int wmNo) throws BoardException {
			int result1 = bService.deleteWmBoard(wmNo);
			String type = "WM";
			HashMap<String, Object> board = new HashMap<>();
			board.put("type", type);
			board.put("bNo", wmNo);
			
			int result2 = bService.statusNAttm(board);
			//System.out.println(bNo);
			if(result1 > 0 || result2> 0) {
				return "redirect:/board/wm";
			} else {
				throw new BoardException("게시글 삭제 실패");
			}
		}
		
		@GetMapping(value="/insertWmReply.yk")
		@ResponseBody
		public String insertWmReply(@ModelAttribute Reply r) {
			r.setBType("WM");
			int result = bService.inserWmReply(r);
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		@GetMapping(value="/deleteWmReply.yk")
		@ResponseBody
		public String deleteWmReply(@ModelAttribute Reply r) {
			int result = bService.deletWmReply(r);
			
			//System.out.println(r);
			// 우리 댓글테이블은 공유잖아?
			// 그래서 댓글넘버가 프라이머리키야(고유해)
			// 그래서 너는 1,2,3,4번을 가지고 있어 이건 도그워커 + 그 게시글에서 생성된 댓글
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		@GetMapping(value="/updateWmReply.yk")
		@ResponseBody
		public String updateWmReply(@ModelAttribute Reply r) {

			int result = bService.updateWmReply(r);
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		
		
		@GetMapping("/board/wm/report")
		@ResponseBody
		public String wmBoardReport(@ModelAttribute Report rep, @RequestParam("wmNo") int wmNo) {

		    
		    rep.setRCategory("WM");
		    rep.setRType("B");
		    rep.setRBoardNo(wmNo);
		    
		    int selectBoardReport = bService.selectBoardReport(rep);
		    System.out.println(selectBoardReport);
		    if (selectBoardReport >0) {
		        // 사용자가 이미 동일한 게시물을 신고함
		        return "existBoardReport";
		    }

		    int result = bService.BoardReport(rep); // 수정된 부분

		    if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
		}
		
		//댓글 신고
		@GetMapping("/insertWmReplyReport.yk")
		@ResponseBody
		public String insertWmReport(@ModelAttribute Report rep, HttpSession session){
			String id = ((Member)session.getAttribute("loginUser")).getMbId();
			rep.setRAccuser(id);
			rep.setRCategory("WM");
			int checkResult = bService.checkReplyResult(rep);
			int result = bService.insertReplyReport(rep);
			
			if(checkResult > 0) {
				return "exist";
			}
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
		}

		//보드 좋아요
		@GetMapping("insertDeleteWmLike.yk")
		@ResponseBody
		public String insertDeleteWmLike(@RequestParam("wmNo") int wmNo, @RequestParam("replyInDel") String replyInDel, HttpSession session) {
			HashMap<String, Object> map = new HashMap<>();
			String id = ((Member)session.getAttribute("loginUser")).getMbId();
			
			map.put("wmNo", wmNo);
			map.put("id", id);
			
			int result = 0;
			if (replyInDel.equals("delete")) {
				result = bService.deleteWmBoardLike(map);
			} else {
				result = bService.insertWmBoardLike(map);
				result = bService.insertWmBoardNotice(map);
			}
				
			if (result > 0) {
				return "good";
			} else {
				return "bad";
			}
		}

		@GetMapping("insertDeleteWmReply.yk")
		@ResponseBody
		public String insertDeleteWmReply(@RequestParam("rNo") int rNo, @RequestParam("replyInDel") String replyInDel, HttpSession session) {
			HashMap<String, Object> map = new HashMap<>();
			String id = ((Member)session.getAttribute("loginUser")).getMbId();

			map.put("rNo", rNo);
			map.put("id", id);
			
			int result = 0;
			if (replyInDel.equals("delete")) {
				result = bService.deleteBoardReplyLike(map);
			} else {
				result = bService.insertBoardReplyLike(map);
				result = bService.insertWmReplyNotice(map);
			}
				
			if (result > 0) {
				return "good";
			} else {
				return "bad";
						
			}
		}

		
		
	

		
				
		/////////////////////중고게시판 후기 황유경*/
		@GetMapping("/board/usedReview")
		public String usedReviewBoard(@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value = "searchType", required = false) String searchType,
				@RequestParam(value = "searchKeyword", required = false) String searchKeyword, Model model, HttpServletRequest request) {
			
			
			if (searchType == null || searchKeyword == null) { // 게시글 검색을 하지 않을 때(=검색어가 없을 때)
				
				int listCount = bService.getUsedRvListCount(1);
			
				PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
				ArrayList<UsedBoard> list = bService.selectUsedRvBoardList(pi, 1);		
				ArrayList<Attachment> aList = bService.selectAttmUsedRvBoardList(null);
				
				//System.out.println(list);
				if(list != null) {
					model.addAttribute("pi", pi);
					model.addAttribute("list", list);
					model.addAttribute("aList", aList);
					
				} else {
					throw new BoardException("게시글 조회 실패");
				}
			} else { // 게시글 검색을 할 때(= 검색어가 있을 때// searchType(작성자, 글제목, 작성자+글제목), searchKeyword()
	              HashMap<String, String> map = new HashMap<>();
	            map.put("searchKeyword", searchKeyword);
	            map.put("searchType", searchType);
	            
	            int listCount = bService.getListCount(1);
	            PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
	            
	            ArrayList<UsedBoard> searchResults = bService.searchUsedRvBoards(map);
	            ArrayList<Attachment> aList = bService.selectAttmUsedRvBoardList(null);
	            model.addAttribute("pi", pi);
				model.addAttribute("list", searchResults);
				model.addAttribute("aList", aList);	
	           }
			model.addAttribute("loc", request.getRequestURI());
			
			return "usedReviewBoard";
		}
		
		
		
		@GetMapping("/board/usedReview/detail")
		public String usedReviewBoardDetail(@RequestParam(value="page", defaultValue="1") int page, @RequestParam("uNo") int uNo, HttpSession session, Model model) {
			
			Member loginUser = (Member)session.getAttribute("loginUser");
			String id = null;
			if(loginUser != null) {
				id = loginUser.getMbId();
			}
			UsedBoard u = bService.selectUsedRvBoard(uNo, id);
			
			ArrayList<Attachment> list = bService.selectAttmUsedRvBoardList(uNo); 
			
//			System.out.println(uNo);
			int listCount = bService.getUsedRvReplyListCount(uNo);
			
			
			ArrayList<Reply> rList = bService.selectUsedRvReply(uNo);
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("uNo", uNo);
			map.put("id", id);
			int likeCount = bService.UsedBoardLike(map);			
			ArrayList<ReplyLike> likeList = new ArrayList<>();
			
			for(Reply r: rList) {
				Reply tempR = new Reply();
				tempR.setRNo(r.getRNo());
				tempR.setMbId(id);
				
				likeList.add(bService.selectReplyLike(tempR));
			}
			
			if(u != null) {
				model.addAttribute("u", u);
				model.addAttribute("page", page);
				model.addAttribute("list", list);
				model.addAttribute("rList", rList);
				model.addAttribute("aLike", likeCount);
				model.addAttribute("lList", likeList);
				//System.out.println(rList);
				return "usedReviewBoardDetail";
			} else {
				throw new BoardException("게시글 상세보기를 실패하였습니다.");
			}
			
		}
		
				
		
		@GetMapping("/board/usedReview/write")
		public String usedReviewBoardWrite(@ModelAttribute UsedBoard u, HttpSession session, Model model) {
			
			String boardWriter = ((Member)session.getAttribute("loginUser")).getMbId();
			u.setMbId(boardWriter);
			
			ArrayList<UsedBoard> prodList = bService.selectProdList(u);
			model.addAttribute("prodList", prodList);
			
			
			return "usedReviewBoardWrite";
		}
		
		@PostMapping("/board/usedReview/insertUsedRvBoard")
	      public String insertUsedRvBoard(@RequestParam("prodNo") int prodNo, @ModelAttribute UsedBoard u, @RequestParam(value = "file", required = false) ArrayList<MultipartFile> files, 
	                           HttpSession session, HttpServletRequest request, Model model,  @RequestParam("uTitle") String uTitle, @RequestParam("uContent") String uContent) {
	         
	         String boardWriter = ((Member)session.getAttribute("loginUser")).getMbId();
	         u.setMbId(boardWriter);
	         
//	         System.out.println(prodNo);
	         
	         UsedBoard selectProduct = bService.checkProdList(prodNo);
	         
//	         System.out.println(selectProduct);
	         selectProduct.setUTitle(uTitle);
	         selectProduct.setUContent(uContent);
	         selectProduct.setMbId(boardWriter);
	         
	         
	         int result1 = bService.insertUsedRvBoard(selectProduct); 
	         System.out.println(selectProduct);
	         
	         ArrayList<Attachment> attachments = new ArrayList<>();
				if (files != null) {
					for(int i = 0; i<files.size(); i++) {
						MultipartFile upload = files.get(i);
						if(!upload.getOriginalFilename().equals("")) {
							String[] returnArr = saveFile(upload);
							if(returnArr[1] != null) {
								Attachment attachment = new Attachment();
								attachment.setOriginalName(upload.getOriginalFilename());
								attachment.setRenameName(returnArr[1]);
								attachment.setAttmPath(returnArr[0]);
								attachment.setAttmRefType("U");
								attachment.setAttmRefNo(selectProduct.getUNo());
								
								attachments.add(attachment);
							}
						}
					}
					
					for(int i=0; i < attachments.size(); i++) {
						Attachment a = attachments.get(i);
						if(i == 0) {
							a.setAttmLevel(1);
						} else {
							a.setAttmLevel(2);
						}
					}
					System.out.println(attachments);
					int result2 = bService.insertUsedRvAttm(attachments);
					//System.out.println(result1);
					//System.out.println(result2);
					if(result1 + result2 > 0) {
						return "redirect:/board/usedReview";
					} else {
						for(Attachment a : attachments) {
							deleteFile(a.getRenameName());
						}
						throw new BoardException("게시글 작성을 실패하였습니다.");
				    }
				} else {
			        if (result1 > 0) {
			            return "redirect:/board/usedReview";
			        } else {
			            throw new BoardException("게시글 작성을 실패하였습니다.");
			        }
			    }
				
			}
	        
		
		
		@GetMapping("/board/usedReview/editForm")
		public String usedRvBoardEditForm(@RequestParam("uNo") int uNo, @RequestParam("page") int page, Model model) {
			
			UsedBoard u = bService.selectUsedRvBoard(uNo,null);
			ArrayList<Attachment> list = bService.selectAttmUsedRvBoardList(uNo);
			model.addAttribute("u", u);
			model.addAttribute("page", page);
			model.addAttribute("list", list);
			
			return "usedReviewBoardEdit";
		}

		
		@PostMapping("/board/usedReview/edit")
		public String usedReviewBoardEdit(
		        @ModelAttribute UsedBoard u,
		        @RequestParam("page") int page,
		        @RequestParam(value = "deleteAttm", required = false, defaultValue = "") String[] deleteAttm,
		        @RequestParam(value = "file", required = false) ArrayList<MultipartFile> files,
		        HttpServletRequest request,
		        RedirectAttributes redirectAttributes) {

		    int updateBoardResult = 0;

		    // 파일이 첨부되어 있는 경우 또는 삭제할 파일이 있는 경우
		    if (files != null || (deleteAttm != null && deleteAttm.length > 0 && !deleteAttm[0].equals("none"))) {
		        ArrayList<Attachment> list = new ArrayList<>();
		        if (files != null) {
		            // 새로운 파일이 첨부된 경우
		            for (int i = 0; i < files.size(); i++) {
		                MultipartFile upload = files.get(i);

		                if (!upload.getOriginalFilename().equals("")) {
		                    String[] returnArr = saveFile(upload);
		                    if (returnArr[1] != null) {
		                        Attachment a = new Attachment();
		                        a.setOriginalName(upload.getOriginalFilename());
		                        a.setRenameName(returnArr[1]);
		                        a.setAttmPath(returnArr[0]);

		                        list.add(a);
		                    }
		                }
		            }
		        }

		        ArrayList<String> delRename = new ArrayList<>();
		        ArrayList<Integer> delLevel = new ArrayList<>();
		        for (String a : deleteAttm) {
		            if (!a.equals("none")) {
		                String[] split = a.split("/");
		                delRename.add(split[0]);
		                delLevel.add(Integer.parseInt(split[1]));
		            }
		        }

		        int deleteAttmResult = 0;
		        boolean existBeforeAttm = true;
		        if (!delRename.isEmpty()) {
		            deleteAttmResult = bService.deleteUsedRvAttm(delRename);
		            if (deleteAttmResult > 0) {
		                for (String rename : delRename) {
		                    deleteFile(rename);
		                }
		            }

		            if (delRename.size() == deleteAttm.length) {
		                existBeforeAttm = false;

		            } else {
		                for (int level : delLevel) {
		                    if (level == 0) {
		                        bService.updateAttmLevel(u.getUNo());
		                        break;
		                    }
		                }
		            }
		        }

		        boolean hasExistingFile = deleteAttm.length > 0 && !deleteAttm[0].equals("none"); // 파일이 있는지 확인하는 플래그

		        boolean hasLevelOne = false; // 레벨 1이 있는지 확인하는 플래그

		        for (String a : deleteAttm) {
		            if (!a.equals("none")) {
		                String[] split = a.split("/");
		                int level = Integer.parseInt(split[1]);
		                if (level == 1) {
		                    hasLevelOne = true; // 레벨 1이 있다면 플래그 업데이트
		                    break;
		                }
		            }
		        }

		        for (int i = 0; i < list.size(); i++) {
		            Attachment a = list.get(i);
		            a.setAttmRefNo(u.getUNo());

		            if (hasExistingFile) {
		                a.setAttmLevel(2); // 기존 파일이 있는 경우 추가되는 파일은 모두 레벨 2로 설정
		            } else {
		                if (!hasLevelOne) {
		                    a.setAttmLevel(1); // 기존 파일이 없고 레벨 1이 없는 경우 추가되는 첫 번째 파일은 레벨 1로 설정
		                    hasLevelOne = true; // 레벨 1이 없다면 플래그 업데이트
		                } else {
		                    a.setAttmLevel(2); // 기존 파일이 없고 레벨 1이 있는 경우 추가되는 나머지 파일은 레벨 2로 설정
		                }
		            }
		        }

		        // 파일 및 DB 업데이트 처리 수행
		        updateBoardResult = bService.updateUsedRvBoard(u);
		        int updateAttmResult = 0;
		        if (!list.isEmpty()) {
		            updateAttmResult = bService.insertUsedRvAttm(list);
		        }
		    } else {
		        // 파일이 없는 경우에는 DB 업데이트만 수행
		        updateBoardResult = bService.updateUsedRvBoard(u);
		    }

		    if (updateBoardResult > 0) {
		        redirectAttributes.addAttribute("uNo", u.getUNo());
		        redirectAttributes.addAttribute("page", page);

		        return "redirect:/board/usedReview/detail";
		    } else {
		        throw new BoardException("게시글 수정 실패하였습니다.");
		    }
		}



		@GetMapping("/board/usedReview/delete")
		public String deleteUsedRvBoard(@RequestParam("uNo") int uNo) throws BoardException {
			int result1 = bService.deleteUsedRvBoard(uNo);
			String type = "U";
			HashMap<String, Object> board = new HashMap<>();
			board.put("type", type);
			board.put("bNo", uNo);
			int result2 = bService.statusNAttm(board);
			//System.out.println(bNo);
			if(result1 > 0 || result2> 0) {
				return "redirect:/board/usedReview";
			} else {
				throw new BoardException("게시글 삭제 실패");
			}
		}
		
		
		@GetMapping(value="/insertUsedRvReply.yk")
		@ResponseBody
		public String insertUsedRvReply(@ModelAttribute Reply r, HttpSession session) {
			String id = ((Member)session.getAttribute("loginUser")).getMbId();
			r.setMbId(id);
			r.setBType("U");
			int result = bService.insertUsedRvReply(r);
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		@GetMapping(value="/deleteUsedRvReply.yk")
		@ResponseBody
		public String deleteUsedRvReply(@ModelAttribute Reply r) {
			int result = bService.deleteUsedRvReply(r);
			
			//System.out.println(r);
			// 우리 댓글테이블은 공유잖아?
			// 그래서 댓글넘버가 프라이머리키야(고유해)
			// 그래서 너는 1,2,3,4번을 가지고 있어 이건 도그워커 + 그 게시글에서 생성된 댓글
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		@GetMapping(value="/updateUsedRvReply.yk")
		@ResponseBody
		public String updateUsedRvReply(@ModelAttribute Reply r) {

			int result = bService.updateUsedRvReply(r);
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		
		
		@GetMapping("/board/usedReview/report")
		@ResponseBody
		public String usedRvBoardReport(@ModelAttribute Report rep, @RequestParam("uNo") int uNo) {

		    
		    rep.setRCategory("U");
		    rep.setRType("B");
		    rep.setRBoardNo(uNo);
		    
		    int selectBoardReport = bService.selectBoardReport(rep);
		    System.out.println(selectBoardReport);
		    if (selectBoardReport >0) {
		        // 사용자가 이미 동일한 게시물을 신고함
		        return "existBoardReport";
		    }

		    int result = bService.BoardReport(rep); // 수정된 부분

		    if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
		}
		
		//댓글 신고
		@GetMapping("/insertUsedRvReplyReport.yk")
		@ResponseBody
		public String insertUsedRvReplyReport(@ModelAttribute Report rep, HttpSession session){
			String id = ((Member)session.getAttribute("loginUser")).getMbId();
			rep.setRAccuser(id);
			rep.setRCategory("U");
			int checkResult = bService.checkReplyResult(rep);
			int result = bService.insertReplyReport(rep);
			
			if(checkResult > 0) {
				return "exist";
			}
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
		}
		
		//보드 좋아요
		@GetMapping("insertDeleteUsedLike.yk")
		@ResponseBody
		public String insertDeleteUsedLike(@RequestParam("uNo") int uNo, @RequestParam("replyInDel") String replyInDel, HttpSession session) {
			HashMap<String, Object> map = new HashMap<>();
			String id = ((Member)session.getAttribute("loginUser")).getMbId();
			
			map.put("uNo", uNo);
			map.put("id", id);
			
			int result = 0;
			if (replyInDel.equals("delete")) {
				result = bService.deleteUsedBoardLike(map);
			} else {
				result = bService.insertUsedBoardLike(map);
				result = bService.insertUsedRvBoardNotice(map);
			}
				
			if (result > 0) {
				return "good";
			} else {
				return "bad";
			}
		}

		@GetMapping("insertDeleteUsedReply.yk")
		@ResponseBody
		public String insertDeleteUsedReply(@RequestParam("rNo") int rNo, @RequestParam("replyInDel") String replyInDel, HttpSession session) {
			HashMap<String, Object> map = new HashMap<>();
			String id = ((Member)session.getAttribute("loginUser")).getMbId();

			map.put("rNo", rNo);
			map.put("id", id);
			
			int result = 0;
			if (replyInDel.equals("delete")) {
				result = bService.deleteBoardReplyLike(map);
			} else {
				result = bService.insertBoardReplyLike(map);
				result = bService.insertUsedRvReplyNotice(map);
			}
				
			if (result > 0) {
				return "good";
			} else {
				return "bad";
						
			}
		}
		
		
		
		/////////////////////////////////////////
		
		
		
		
		
		//<< 카드댁 형식 >>
		// 
		// 1. 중고게시판 //
		
//		@GetMapping("/board/used")
//		public String usedBoardView() {
//			
//			return "usedBoard";
//		}
		

		// 게시글 목록 조회
	       @GetMapping("/board/used")
	       public String usedBoardView(@RequestParam(value="page", defaultValue="1") int page,
	                            @RequestParam(value = "searchType", required = false) String searchType,
	                            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
	                                   Model model,
	                                   HttpServletRequest request) throws BoardException {
	          
	          ArrayList<Attachment> aList = bService.selectAttmUsedBoardList(null);
	          
	          if (searchType == null || searchKeyword == null) { // 게시글 검색을 하지 않을 때(=검색어가 없을 때)
	              int listCount = bService.getUlistCount(1);
	              
	              PageInfo pi = Pagination.getPageInfo(page, listCount, 9);
	              ArrayList<UsedBoard> uList = bService.selectUsedBoardList(pi, 1); 
	              
	              System.out.println(aList);
	              if(uList.isEmpty()) {
	                 model.addAttribute("message", "게시글이 없습니다.");
	              } else {
	                 model.addAttribute("pi", pi);
	                 model.addAttribute("uList", uList); // 게시글 목록을 'mList'라는 이름으로 모델에 추가
	                 
	              }
	           } else { // 게시글 검색을 할 때(= 검색어가 있을 때// searchType(작성자, 글제목, 작성자+글제목), searchKeyword()
	              HashMap<String, String> map = new HashMap<>();
	            map.put("searchKeyword", searchKeyword);
	            map.put("searchType", searchType);
	            
	            int listCount = bService.getUlistCount(1); // 추가
	            PageInfo pi = Pagination.getPageInfo(page, listCount, 9); // 추가
	            ArrayList<UsedBoard> searchResults = bService.searchUsedBoards(map);
	            model.addAttribute("uList", searchResults);
	            model.addAttribute("pi", pi);
	           }
	           
	          model.addAttribute("aList", aList); 
	           model.addAttribute("loc", request.getRequestURI()); // 이전 정보에 대한 uri 담고 있음
	           
	           return "usedBoard";
	       }
	    
	   
	    
		
//		 // 첨부파일 게시글 상세보기 //
		@GetMapping("/board/used/detail")
		public String usedBoardDetail(@RequestParam(value="page", defaultValue="1") String page, @RequestParam("uNo") int uNo, HttpSession session, Model model){
			
			Member loginUser = (Member)session.getAttribute("loginUser");
			String mbId = null;
			if(loginUser != null) {
				mbId = loginUser.getMbId();
			}
			
			System.out.println(uNo);
			
			
			
			UsedBoard u = bService.selectUsedBoard(uNo, mbId);
			ArrayList<Attachment> aList = bService.selectAttmUsedBoardList((Integer)uNo); 
			ArrayList<Reply> rList = bService.selectUsedReply(uNo);
			HashMap<String, Object> map = new HashMap<>();
			map.put("mbId", mbId);
			map.put("uNo", uNo);
			int result = bService.selectUsedSaved(map);
			ArrayList<ReplyLike> likeList = new ArrayList<>();
			
			for(Reply r: rList) {
				Reply tempR = new Reply();
				tempR.setRNo(r.getRNo());
				tempR.setMbId(mbId);
				
				likeList.add(bService.selectReplyLike(tempR));
			}
			
			if(u != null) {
				model.addAttribute("saved", result);
//				model.addAttribute("r", r);
				model.addAttribute("u", u);
				model.addAttribute("page", page);
				model.addAttribute("aList", aList);
				model.addAttribute("rList", rList);
				model.addAttribute("lList", likeList);
				//System.out.println(m);
				return "usedBoardDetail";
			} else {
				throw new BoardException("게시글 상세보기를 실패하였습니다.");
			}
		}
		 	
		
		 
		 @GetMapping("/board/used/write") 
		 public String usedBoardWrite() {
			 
			 return "usedBoardWrite"; 
		 }

		@PostMapping("/board/used/insertUsedBoard")/*@RequestParam("dwType") String dwTypeStr*/
		 public String insertUsedBoard (@ModelAttribute UsedBoard u, @RequestParam(value = "file", required = false) ArrayList<MultipartFile> files, HttpSession session, HttpServletRequest request) {
		 
				
				String boardWriter = ((Member)session.getAttribute("loginUser")).getMbId();
				u.setMbId(boardWriter);
				
				
				System.out.println("soldDate: " + u.getSoldDate());
				
				if (u.getSoldDate() == null || u.getSoldDate().toString().isEmpty()) {
				    u.setSoldDate(null);
				}
				
//				int dwType = Integer.parseInt(dwTypeStr);
				int result1 = bService.insertUsedBoard(u); 
				
				ArrayList<Attachment> attachments = new ArrayList<>();
				if (files != null) {
					for(int i = 0; i<files.size(); i++) {
						MultipartFile upload = files.get(i);
						if(!upload.getOriginalFilename().equals("")) {
							String[] returnArr = saveFile(upload);
							if(returnArr[1] != null) {
								Attachment attachment = new Attachment();
								attachment.setOriginalName(upload.getOriginalFilename());
								attachment.setRenameName(returnArr[1]);
								attachment.setAttmPath(returnArr[0]);
								attachment.setAttmRefType("U");
								attachment.setAttmRefNo(u.getUNo());
								
								attachments.add(attachment);
							}
						}
					}
					
					for(int i=0; i < attachments.size(); i++) {
						Attachment a = attachments.get(i);
						if(i == 0) {
							a.setAttmLevel(1);
						} else {
							a.setAttmLevel(2);
						}
					}
					
					int result2 = bService.insertUsedAttm(attachments);
					//System.out.println(result1);
					//System.out.println(result2);
					if(result1 + result2 > 0) {
						return "redirect:/board/used";
					} else {
						for(Attachment a : attachments) {
							deleteFile(a.getRenameName());
						}
						throw new BoardException("게시글 작성을 실패하였습니다.");
				    }
				} else {
			        if (result1 > 0) {
			            return "redirect:/board/used";
			        } else {
			            throw new BoardException("게시글 작성을 실패하였습니다.");
			        }
			    }
				
			}
			
			
			@GetMapping("/board/used/editForm")
			public String usedBoardEditForm(@RequestParam("uNo") int uNo, @RequestParam("page") int page, Model model) {
				
				UsedBoard u = bService.selectUsedBoard(uNo,null);
				//System.out.println(dw);
				ArrayList<Attachment> list = bService.selectAttmUsedBoardList(uNo);
				model.addAttribute("u", u);
				model.addAttribute("page", page);
				model.addAttribute("list", list);
				
				return "usedBoardEdit";
			}
		 	 
		 
			 
			@PostMapping("/board/used/edit") 
			public String usedBoardEdit(@ModelAttribute UsedBoard u,
								        @RequestParam("page") int page,
								        @RequestParam(value = "deleteAttm", required = false, defaultValue = "") String[] deleteAttm,
								        @RequestParam(value = "file", required = false) ArrayList<MultipartFile> files,
								        HttpServletRequest request,
								        RedirectAttributes redirectAttributes) {
				
				
				 ArrayList<Attachment> list = new ArrayList<>();
				 
				 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				    // 파일이 존재하는 경우에만 파일 업로드 및 DB 처리 수행
				    if (files != null) {
				        for (int i = 0; i < files.size(); i++) {
				            MultipartFile upload = files.get(i);

				            if (!upload.getOriginalFilename().equals("")) {
				                String[] returnArr = saveFile(upload);
				                if (returnArr[1] != null) {
				                    Attachment a = new Attachment();
				                    a.setOriginalName(upload.getOriginalFilename());
				                    a.setRenameName(returnArr[1]);
				                    a.setAttmPath(returnArr[0]);

				                    list.add(a);
				                }
				            }
				        }
				    }

				    ArrayList<String> delRename = new ArrayList<>();
				    ArrayList<Integer> delLevel = new ArrayList<>();
				    for (String a : deleteAttm) {
				        if (!a.equals("none")) {
				            String[] split = a.split("/");
				            delRename.add(split[0]);
				            delLevel.add(Integer.parseInt(split[1]));
				        }
				    }

				    int deleteAttmResult = 0;
				    int updateBoardResult = 0;
				    boolean existBeforeAttm = true;

				    if (!delRename.isEmpty()) {
				        deleteAttmResult = bService.deleteUsedAttm(delRename);
				        if (deleteAttmResult > 0) {
				            for (String rename : delRename) {
				                deleteFile(rename);
				            }
				        }

				        if (delRename.size() == deleteAttm.length) {
				            existBeforeAttm = false;
				        } else {
				            boolean levelOneDeleted = false; // 삭제된 파일 중 레벨 1이 있는지 여부

				            for (int level : delLevel) {
				                if (level == 1) {
				                    levelOneDeleted = true;
				                    break;
				                }
				            }

				            if (levelOneDeleted) {
				                // 삭제된 파일 중 레벨 1이 있다면 다른 기존 파일의 레벨을 1로 설정
				                bService.updateAttmLevel(u.getUNo());
				            }
				        }
				    }

				    boolean hasExistingFile = deleteAttm.length > 0 && !deleteAttm[0].equals("none");
				    boolean hasLevelOne = false;

				    for (String a : deleteAttm) {
				        if (!a.equals("none")) {
				            String[] split = a.split("/");
				            int level = Integer.parseInt(split[1]);
				            if (level == 1) {
				                hasLevelOne = true;
				                break;
				            }
				        }
				    }

				    // 기존에 1인 파일이 있는지 여부를 확인
				    boolean existingLevelOne = bService.UsedhasLevelOne(u.getUNo());

				    for (int i = 0; i < list.size(); i++) {
				        Attachment a = list.get(i);
				        a.setAttmRefNo(u.getUNo());

				        if (hasExistingFile || existingLevelOne) {
				            a.setAttmLevel(2);
				        } else {
				            a.setAttmLevel(1);
				            existingLevelOne = true;
				        }
				    }

				    // 파일 및 DB 처리 수행
				    updateBoardResult = bService.updateUsedBoard(u);
				    int updateAttmResult = 0;

				    if (!list.isEmpty()) {
				        updateAttmResult = bService.insertUsedAttm(list);
				    }

				    if (updateBoardResult + updateAttmResult > 0) {
				        redirectAttributes.addAttribute("uNo", u.getUNo());
				        redirectAttributes.addAttribute("page", page);
				        return "redirect:/board/used/detail";
				    } else {
				        throw new BoardException("첨부파일 게시글 수정 실패하였습니다.");
				    }
				}
			 
			
			@GetMapping("/board/used/delete")
			public String deleteUsedBoard(@RequestParam("uNo") int uNo) throws BoardException {
				int result1 = bService.deleteUsedBoard(uNo);
				int result2 = bService.deleteUsedBoardAttm(uNo);
				//System.out.println(bNo);
				if(result1 > 0 || result2> 0) {
					return "redirect:/board/used";
				} else {
					throw new BoardException("게시글 삭제 실패");
				}
			}
		 
		 
			@GetMapping(value="/insertLostReply.ha")
			@ResponseBody
			public String insertLostReply(@ModelAttribute Reply r) {
				r.setBType("M");
				int result = bService.insertLostReply(r);
				
				if(result > 0) {
					return "good";
					
				} else {
					return "bad";
				}
				
			}
			
			@GetMapping(value="/deleteLostReply.ha")
			@ResponseBody
			public String deleteLostReply(@ModelAttribute Reply r) {
				int result = bService.deleteLostReply(r);
				
				//System.out.println(r);
				// 우리 댓글테이블은 공유잖아?
				// 그래서 댓글넘버가 프라이머리키야(고유해)
				// 그래서 너는 1,2,3,4번을 가지고 있어 이건 도그워커 + 그 게시글에서 생성된 댓글
				
				if(result > 0) {
					return "good";
					
				} else {
					return "bad";
				}
				
			}
			
			@GetMapping(value="/updateLostReply.ha")
			@ResponseBody
			public String updateLostReply(@ModelAttribute Reply r) {

				int result = bService.updateLostReply(r);
				
				if(result > 0) {
					return "good";
					
				} else {
					return "bad";
				}
				
			}
			
			
			
			@GetMapping("/board/lost/report")
			@ResponseBody
			public String lostBoardReport(@ModelAttribute Report rep, @RequestParam("mNo") int mNo) {

			    
			    rep.setRCategory("M");
			    rep.setRType("B");
			    rep.setRBoardNo(mNo);
			    
			    int selectBoardReport = bService.selectBoardReport(rep);
			    System.out.println(selectBoardReport);
			    if (selectBoardReport >0) {
			        // 사용자가 이미 동일한 게시물을 신고함
			        return "existBoardReport";
			    }

			    int result = bService.BoardReport(rep); // 수정된 부분

			    if(result > 0) {
					return "good";
					
				} else {
					return "bad";
				}
			}
			
			//댓글 신고
			@GetMapping("/insertLostReplyReport.ha")
			@ResponseBody
			public String insertLostReplyReport(@ModelAttribute Report rep, HttpSession session){
				String id = ((Member)session.getAttribute("loginUser")).getMbId();
				rep.setRAccuser(id);
				rep.setRCategory("M");
				int checkResult = bService.checkReplyResult(rep);
				int result = bService.insertReplyReport(rep);
				
				if(checkResult > 0) {
					return "exist";
				}
				if(result > 0) {
					return "good";
					
				} else {
					return "bad";
				}
			}
			
			@GetMapping("insertDeleteUsedSaved.yk")
		    @ResponseBody
		    public String insertDeleteUsedSaved(@RequestParam("productId") int productId, @RequestParam("replyInDel") String replyInDel, HttpSession session) {
				HashMap<String, Object> map = new HashMap<>();
				String id = ((Member)session.getAttribute("loginUser")).getMbId();
				
				map.put("productId", productId);
				map.put("id", id);
				
				int result = 0;
				if (replyInDel.equals("delete")) {
					result = bService.deleteUsedSaved(map);
				} else {
					result = bService.insertUsedSaved(map);
				}
					
				if (result > 0) {
					return "good";
				} else {
					return "bad";
				}
			}
			//보드 좋아요
			@GetMapping("insertDeleteLostLike.yk")
			@ResponseBody
			public String insertDeleteLostLike(@RequestParam("mNo") int mNo, @RequestParam("replyInDel") String replyInDel, HttpSession session) {
				HashMap<String, Object> map = new HashMap<>();
				String id = ((Member)session.getAttribute("loginUser")).getMbId();
				
				map.put("mNo", mNo);
				map.put("id", id);
				
				int result = 0;
				if (replyInDel.equals("delete")) {
					result = bService.deleteLostBoardLike(map);
				} else {
					result = bService.insertLostBoardLike(map);
					result = bService.insertLostBoardNotice(map);
				}
					
				if (result > 0) {
					return "good";
				} else {
					return "bad";
				}
			}

			@GetMapping("insertDeleteLostReply.yk")
			@ResponseBody
			public String insertDeleteLostReply(@RequestParam("rNo") int rNo, @RequestParam("replyInDel") String replyInDel, HttpSession session) {
				HashMap<String, Object> map = new HashMap<>();
				String id = ((Member)session.getAttribute("loginUser")).getMbId();

				map.put("rNo", rNo);
				map.put("id", id);
				
				int result = 0;
				if (replyInDel.equals("delete")) {
					result = bService.deleteBoardReplyLike(map);
				} else {
					result = bService.insertBoardReplyLike(map);
					result = bService.insertLostReplyNotice(map);
				}
					
				if (result > 0) {
					return "good";
				} else {
					return "bad";
							
				}
			}
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		
		
				// 2. 실종신고 //
		
		// 글 목록
		//	첨부파일 게시글 조회 //
		// 게시글 목록 페이지로 이동할 때 필요한 데이터를 모델에 담아 뷰로 전달하는 역할
		
			// 게시글 목록 조회
            @GetMapping("/board/lost")
            public String lostBoardView(
                                 @RequestParam(value="page", defaultValue="1") int page,
                                 @RequestParam(value = "searchType", required = false) String searchType,
                                 @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                                        Model model,
                                        HttpServletRequest request) throws BoardException {
               // 1번, 아래 로직은 서치타입이랑 서치키워드가 없을 때만 발동
              // else 둘 다 존재하면(검색했을때) -> if문 발동 안함
              //
              ArrayList<Attachment> aList = bService.selectAttmLostBoardList(null);
               if (searchType == null || searchKeyword == null) { // 게시글 검색을 하지 않을 때(=검색어가 없을 때)
                   int listCount = bService.getMlistCount(1);
                   
                   PageInfo pi = Pagination.getPageInfo(page, listCount, 9);
                   ArrayList<LostBoard> mList = bService.selectLostBoardList(pi, 1);      
                   
                   System.out.println(aList);
                   if(mList.isEmpty()) {
                      model.addAttribute("message", "게시글이 없습니다.");
                   } else {
                      model.addAttribute("pi", pi);
                      model.addAttribute("mList", mList); // 게시글 목록을 'mList'라는 이름으로 모델에 추가
                      
                   }
                } else { // 게시글 검색을 할 때(= 검색어가 있을 때// searchType(작성자, 글제목, 작성자+글제목), searchKeyword()
                   HashMap<String, String> map = new HashMap<>();
                 map.put("searchKeyword", searchKeyword);
                 map.put("searchType", searchType);
                 
                 System.out.println(searchKeyword);
                 System.out.println(searchType);
                 int listCount = bService.getMlistCount(1); // 추가
                 PageInfo pi = Pagination.getPageInfo(page, listCount, 9); // 추가
                 ArrayList<LostBoard> searchResults = bService.searchLostBoards(map);
                 model.addAttribute("mList", searchResults);
                 model.addAttribute("pi", pi);
                }
               
                model.addAttribute("aList", aList); // 첨부파일 목록 추가
                model.addAttribute("loc", request.getRequestURI());
                
                return "lostBoard";
            }
		

//	    글작성 //
	    @GetMapping("/board/lost/write")
	      public String LostBoardWrite() {
	         
	         return "lostBoardWrite";
	      }
	    
//	  글 등록//
      @PostMapping("/board/lost/insertLostBoard")
      public String insertLostBoard(@ModelAttribute LostBoard m, 
    		  						@RequestParam("file") ArrayList<MultipartFile> files, 
    		  						HttpSession session, HttpServletRequest request) throws BoardException {
         String boardWriter = ((Member)session.getAttribute("loginUser")).getMbId();
         m.setMbId(boardWriter);
         //System.out.println(boardWriter);
         int result1 = bService.insertLostBoard(m); // 글 내용을 board 테이블에 저장
         //System.out.println(b.getBNo());
         
         System.out.println(m);
         ArrayList<Attachment> attachments = new ArrayList<>();
         for(int i = 0; i<files.size(); i++) {
            MultipartFile upload = files.get(i);
            if(!upload.getOriginalFilename().equals("")) {
               String[] returnArr = saveFile(upload);
               if(returnArr[1] != null) {
                  Attachment attachment = new Attachment();
                  attachment.setOriginalName(upload.getOriginalFilename());
                  attachment.setRenameName(returnArr[1]);
                  attachment.setAttmPath(returnArr[0]);
                  attachment.setAttmRefType("M");
                  attachment.setAttmRefNo(m.getMNo());
                  
                  attachments.add(attachment);
//	                  System.out.println(attachment.getAttmRefType());
               }
            }
         }
         
         // 썸네일 첫번째 첨부파일으로 하는거
         for(int i=0; i < attachments.size(); i++) {
            Attachment a = attachments.get(i);
            if(i == 0) {
               a.setAttmLevel(1);
            } else {
               a.setAttmLevel(2);
            }
         }
         
         int result2 = bService.insertLostAttm(attachments);
         System.out.println(attachments);
         //System.out.println(result1);
         //System.out.println(result2);
         
         System.out.println(attachments);
         if(result1 + result2 > 0) {
            return "redirect:/board/lost";
         } else {
            for(Attachment a : attachments) {
               deleteFile(a.getRenameName());
            }
            throw new BoardException("게시글 작성을 실패하였습니다.");
          }
      }
	         
	      
	      
	

		
	   // 첨부파일 게시글 상세보기 //
			@GetMapping("/board/lost/detail")
			public String lostBoardDetail(@RequestParam(value="page", defaultValue="1") String page, @RequestParam("mNo") int mNo, HttpSession session, Model model) {
				
				Member loginUser = (Member)session.getAttribute("loginUser");
				String mbId = null;
				if(loginUser != null) {
					mbId = loginUser.getMbId();
				}
				
				System.out.println(mNo);
				
				
				LostBoard m = bService.selectLostBoard(mNo, mbId);
				ArrayList<Attachment> aList = bService.selectAttmLostBoardList((Integer)mNo); // ArrayList<Attachment> mList = bService.selectAttmLostBoardList((Integer)mNo);였음 
				ArrayList<Reply> rList = bService.selectLostReply(mNo);
				
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("mNo", mNo);
				map.put("id", mbId);
				int likeCount = bService.LostBoardLike(map);			
				ArrayList<ReplyLike> likeList = new ArrayList<>();
				
				for(Reply r: rList) {
					Reply tempR = new Reply();
					tempR.setRNo(r.getRNo());
					tempR.setMbId(mbId);
					
					likeList.add(bService.selectReplyLike(tempR));
				}
				
				
				
				if(m != null) {
					model.addAttribute("m", m);
					model.addAttribute("page", page);
					model.addAttribute("aList", aList); //model.addAttribute("mList", mList);였음
					model.addAttribute("rList", rList);
					model.addAttribute("aLike", likeCount);
					model.addAttribute("lList", likeList);
					//System.out.println(m);
					return "lostBoardDetail";
				} else {
					throw new BoardException("게시글 상세보기를 실패하였습니다.");
				}
			}
	      
		
//		// 첨부파일 게시글 상세보기 //
//		@GetMapping("/board/lost/detail")
//		public String lostBoardDetail(@RequestParam(name="rNo", required=false) Integer rNo,
//									  @RequestParam(name="likeUser", required=false) Integer likeUser,
//									  @RequestParam(name="mNo", required=false) Integer mNo, 
//									  @RequestParam(value = "page", defaultValue = "1") int page, 
//									  HttpSession session, Model model) throws BoardException {
//			Reply r = bService.selectReplyLostBoard(rNo);
//			
//			Member loginUser = (Member)session.getAttribute("loginUser");
//			String mbId = null;
//			if(loginUser != null) {
//				mbId = loginUser.getMbId();
//			}
//			
//			System.out.println(mNo);
//			
//			if(mNo == null) {
//				// mNo가 null인 경우의 처리를 여기 추가
//				throw new BoardException("게시글 번호가 없습니다.");
//			}
//			
//			LostBoard m = bService.selectLostBoard(mNo);
//			ArrayList<Attachment> mList = bService.selectAttmLostBoardList((Integer)mNo); 
//			
//			if(m != null) {
//				model.addAttribute("m", m);
//				model.addAttribute("page", page);
//				model.addAttribute("mList", mList);
//				//System.out.println(m);
//				return "lostBoardDetail";
//			} else {
//				throw new BoardException("게시글 상세보기를 실패하였습니다.");
//			}
//		}
		
			
		//24.01.14_ing
		// 글 수정
			@GetMapping("/board/lost/editForm")
			public String lostBoardEditForm(@RequestParam("mNo") int mNo, @RequestParam("page") int page, Model model) {
				
				LostBoard m = bService.selectLostBoard(mNo,null);
				//System.out.println(dw);
				ArrayList<Attachment> list = bService.selectAttmLostBoardList(mNo);
				model.addAttribute("m", m);
				model.addAttribute("page", page);
				model.addAttribute("list", list);
				
				return "lostBoardEdit";
			}
			
			@PostMapping("/board/lost/edit")
			public String lostBoardEdit(
			        @ModelAttribute LostBoard m,
			        @RequestParam("page") int page,
			        @RequestParam(value = "deleteAttm", required = false, defaultValue = "") String[] deleteAttm,
			        @RequestParam(value = "file", required = false) ArrayList<MultipartFile> files,
			        HttpServletRequest request,
			        RedirectAttributes redirectAttributes) {

			    ArrayList<Attachment> list = new ArrayList<>();

			    // 파일이 존재하는 경우에만 파일 업로드 및 DB 처리 수행
			    if (files != null) {
			        for (int i = 0; i < files.size(); i++) {
			            MultipartFile upload = files.get(i);

			            if (!upload.getOriginalFilename().equals("")) {
			                String[] returnArr = saveFile(upload);
			                if (returnArr[1] != null) {
			                    Attachment a = new Attachment();
			                    a.setOriginalName(upload.getOriginalFilename());
			                    a.setRenameName(returnArr[1]);
			                    a.setAttmPath(returnArr[0]);

			                    list.add(a);
			                }
			            }
			        }
			    }

			    ArrayList<String> delRename = new ArrayList<>();
			    ArrayList<Integer> delLevel = new ArrayList<>();
			    for (String a : deleteAttm) {
			        if (!a.equals("none")) {
			            String[] split = a.split("/");
			            delRename.add(split[0]);
			            delLevel.add(Integer.parseInt(split[1]));
			        }
			    }

			    int deleteAttmResult = 0;
			    int updateBoardResult = 0;
			    boolean existBeforeAttm = true;

			    if (!delRename.isEmpty()) {
			        deleteAttmResult = bService.deleteLostAttm(delRename);
			        if (deleteAttmResult > 0) {
			            for (String rename : delRename) {
			                deleteFile(rename);
			            }
			        }

			        if (delRename.size() == deleteAttm.length) {
			            existBeforeAttm = false;
			        } else {
			            boolean levelOneDeleted = false; // 삭제된 파일 중 레벨 1이 있는지 여부

			            for (int level : delLevel) {
			                if (level == 1) {
			                    levelOneDeleted = true;
			                    break;
			                }
			            }

			            if (levelOneDeleted) {
			                // 삭제된 파일 중 레벨 1이 있다면 다른 기존 파일의 레벨을 1로 설정
			                bService.updateAttmLevel(m.getMNo());
			            }
			        }
			    }

			    boolean hasExistingFile = deleteAttm.length > 0 && !deleteAttm[0].equals("none");
			    boolean hasLevelOne = false;

			    for (String a : deleteAttm) {
			        if (!a.equals("none")) {
			            String[] split = a.split("/");
			            int level = Integer.parseInt(split[1]);
			            if (level == 1) {
			                hasLevelOne = true;
			                break;
			            }
			        }
			    }

			    // 기존에 1인 파일이 있는지 여부를 확인
			    boolean existingLevelOne = bService.hasLevelOne(m.getMNo());

			    for (int i = 0; i < list.size(); i++) {
			        Attachment a = list.get(i);
			        a.setAttmRefNo(m.getMNo());

			        if (hasExistingFile || existingLevelOne) {
			            a.setAttmLevel(2);
			        } else {
			            a.setAttmLevel(1);
			            existingLevelOne = true;
			        }
			    }

			    // 파일 및 DB 처리 수행
			    updateBoardResult = bService.updateLostBoard(m);
			    int updateAttmResult = 0;

			    if (!list.isEmpty()) {
			        updateAttmResult = bService.insertLostAttm(list);
			    }

			    if (updateBoardResult + updateAttmResult > 0) {
			        redirectAttributes.addAttribute("mNo", m.getMNo());
			        redirectAttributes.addAttribute("page", page);
			        return "redirect:/board/lost/detail";
			    } else {
			        throw new BoardException("첨부파일 게시글 수정 실패하였습니다.");
			    }
			}

//		글삭제
		
			/*
			 * @GetMapping("/board/lost/delete/{boardId}") public String
			 * updateLostBoardDelete(@PathVariable("boardId") int bId, Model model) {
			 * 
			 * int bResult = bService.deleteLostBoard(bId); int aResult =
			 * bService.deleteLostBoardAttm(bId);
			 * 
			 * 
			 * 
			 * return "redirect:/board/lost"; }
			 */
		
		@GetMapping("/board/lost/delete")
		public String deleteLostBoard(@RequestParam("mNo") int mNo) throws BoardException {
			int result1 = bService.deleteLostBoard(mNo);
			int result2 = bService.deleteLostBoardAttm(mNo);
			//System.out.println(bNo);
			if(result1 > 0 || result2> 0) {
				return "redirect:/board/lost";
			} else {
				throw new BoardException("게시글 삭제 실패");
			}
		}
		/*
		 * @PostMapping("/editLostBoard.he") public String
		 * updateLostBoard(@ModelAttribute LostBoard lb) {
		 * 
		 * int result = bService.editLostBoard(lb);
		 * 
		 * return "redirect:/board/lost/detail?mNo="+lb.getMNo(); }
		 */
		@GetMapping(value="/insertUsedReply.ha")
		@ResponseBody
		public String insertUsedReply(@ModelAttribute Reply r) {
			r.setBType("U");
			int result = bService.insertUsedReply(r);
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		@GetMapping(value="/deleteUsedReply.ha")
		@ResponseBody
		public String deleteUsedReply(@ModelAttribute Reply r) {
			int result = bService.deleteUsedReply(r);
			
			//System.out.println(r);
			// 우리 댓글테이블은 공유잖아?
			// 그래서 댓글넘버가 프라이머리키야(고유해)
			// 그래서 너는 1,2,3,4번을 가지고 있어 이건 도그워커 + 그 게시글에서 생성된 댓글
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		@GetMapping(value="/updateUsedReply.ha")
		@ResponseBody
		public String updateUsedReply(@ModelAttribute Reply r) {

			int result = bService.updateUsedReply(r);
			
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
			
		}
		
		
		
		@GetMapping("/board/used/report")
		@ResponseBody
		public String usedBoardReport(@ModelAttribute Report rep, @RequestParam("uNo") int uNo) {

		    
		    rep.setRCategory("U");
		    rep.setRType("B");
		    rep.setRBoardNo(uNo);
		    
		    int selectBoardReport = bService.selectBoardReport(rep);
		    System.out.println(selectBoardReport);
		    if (selectBoardReport >0) {
		        // 사용자가 이미 동일한 게시물을 신고함
		        return "existBoardReport";
		    }

		    int result = bService.BoardReport(rep); // 수정된 부분

		    if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
		}
		
		//댓글 신고
		@GetMapping("/insertUsedReplyReport.ha")
		@ResponseBody
		public String insertUsedReplyReport(@ModelAttribute Report rep, HttpSession session){
			String id = ((Member)session.getAttribute("loginUser")).getMbId();
			rep.setRAccuser(id);
			rep.setRCategory("U");
			int checkResult = bService.checkReplyResult(rep);
			int result = bService.insertReplyReport(rep);
			
			if(checkResult > 0) {
				return "exist";
			}
			if(result > 0) {
				return "good";
				
			} else {
				return "bad";
			}
		}
		
		

		
}
    