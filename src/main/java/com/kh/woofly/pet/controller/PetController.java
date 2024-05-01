package com.kh.woofly.pet.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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

import com.kh.woofly.admin.model.vo.Report;
import com.kh.woofly.board.model.vo.Attachment;
import com.kh.woofly.common.PageInfo;
import com.kh.woofly.common.Pagination;
import com.kh.woofly.common.Reply;
import com.kh.woofly.common.ReplyLike;
import com.kh.woofly.contest.model.vo.Participants;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.pet.model.exception.PetException;
import com.kh.woofly.pet.model.service.PetService;
import com.kh.woofly.pet.model.vo.Album;
import com.kh.woofly.pet.model.vo.Diary;
import com.kh.woofly.pet.model.vo.Pet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PetController {

	@Autowired
	private PetService pService;
	
	@GetMapping("profile/{mbId}")
	public String userProfile(Model model, @PathVariable("mbId") String mbId) {
		Member user = pService.selectMember(mbId);
		ArrayList<Pet> list =pService.petInfoList(mbId);
		
		model.addAttribute("user", user);
		model.addAttribute("list", list);
		
		return "userProfile";
	}
	
	//마이펫 리스트
	@GetMapping("pet/petInfo")
	public String petInfo(Model model, HttpSession session, @ModelAttribute Pet p) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		p.setOwnerId(id);
		ArrayList<Pet> list = pService.petInfoList(id);
		model.addAttribute("list", list);
			
		return "petInfo";
	}
	
    @GetMapping("insertPhotoReport.dw")
    @ResponseBody
    public String insertPhotoReport(@ModelAttribute Report rt, HttpSession session) {
    	String id = ((Member)session.getAttribute("loginUser")).getMbId();
		rt.setRAccuser(id);
		rt.setRType("B");
		int checkResult = pService.checkResult(rt);
		int result = pService.insertReport(rt);
		
		if(checkResult > 0) {
			return "exist";
		}
		if(result > 0) {
			return "good";
			
		} else {
			return "bad";
		}
    }

    
    	@GetMapping("/userPhoto")
    	public String userPhotoView(Model model, @RequestParam(value="page", defaultValue="1") int page, HttpServletRequest request,
    								@RequestParam(value="petName", required=false) Integer petId, @RequestParam(value="mbId") String mbId) {
    		System.out.println(123);
			HashMap<String, Object> map = new HashMap<>();
			map.put("id", mbId);
			map.put("petId", petId);
  
			int listCount = pService.getListCount(1);
			PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
			ArrayList<Album> aList = pService.selectMyAlbums(map);
			ArrayList<Pet> pList = pService.petInfoList(mbId);
			ArrayList<Reply> rList = pService.repliesList(mbId);
			System.out.println(aList);
			if(aList != null) {
				 model.addAttribute("pi", pi);
				 model.addAttribute("loc", request.getRequestURI());
				 model.addAttribute("pList", pList);
				 model.addAttribute("aList", aList);
				 model.addAttribute("rList", rList);
				 return "userPhoto";
					     
			} else {
			     throw new PetException("사진첩 조회에 실패하였습니다.");
			}
    	}
			    	
    
    
	  @GetMapping("pet/petPhoto")
	   public String petPhotoView(HttpSession session, Model model, @RequestParam(value="page", defaultValue="1") int page,
			   @RequestParam(value="petName", required=false) Integer petId, HttpServletRequest request) {
	      String id = ((Member)session.getAttribute("loginUser")).getMbId();
	      HashMap<String, Object> map = new HashMap<>();
	      map.put("id", id);
          map.put("petId", petId);
          
          int listCount = pService.getListCount(1);
          PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
          
	      ArrayList<Album> aList = pService.selectMyAlbums(map);
	      ArrayList<Pet> pList = pService.petInfoList(id);
	      ArrayList<Reply> rList = pService.repliesList(id);
	      
	      if(aList != null) {
	    	 model.addAttribute("pi", pi);
	    	 model.addAttribute("loc", request.getRequestURI());
	         model.addAttribute("pList", pList);
	         model.addAttribute("aList", aList);
	         model.addAttribute("rList", rList);
	         return "petPhoto";
	         
	      } else {
	         throw new PetException("마이펫 사진첩 조회에 실패하였습니다.");
	      }
	   }

	@GetMapping("pet/petDiary")
	public String petDiaryView(HttpSession session, Model model,@RequestParam(value="page", defaultValue="1") int page,
							 @RequestParam(value="startDate", required=false) String startDate, 
							 @RequestParam(value="endDate", required=false) String endDate, HttpServletRequest request,
							 @RequestParam(value="petName", required=false) Integer petId, @RequestParam(value="petHealth", required=false) String petHealth) {
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
				map.put("endDate", sdf.parse(endDate));
				
				Date newDate = sdf.parse(endDate);
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
		
		if(petId == null) {
			petId = null;
		}
		map.put("petId", petId);
		if (petHealth == null || petHealth.equals("")) {
			petHealth = null;
		}
		map.put("petHealth", petHealth);
		
		int listCount = pService.getDiaryCount(map);
		PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
		
		ArrayList<Diary> list = pService.petDiaryList(pi, map);
		ArrayList<Pet> pet = pService.petInfoList(id);
		
		model.addAttribute("petId", petId);
		model.addAttribute("petHealth", petHealth);
		model.addAttribute("list", list);
		model.addAttribute("pet", pet);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("loc", request.getRequestURI());
		model.addAttribute("pi", pi);
		
		return "petDiary";
	}

	
	@GetMapping("pet/petContest")
	public String petContestView(HttpSession session, Model model, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="petName", required=false) Integer petId) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("petId", petId);
		map.put("id", id);
		
		System.out.println("map :" + map);
		
		ArrayList<Participants> ptList = pService.petContestList(map);
		ArrayList<Pet> pList = pService.petInfoList(id);
		
		System.out.println("ptList : " + ptList);
		
		if(ptList != null) {
			model.addAttribute("pList", pList);
			model.addAttribute("ptList", ptList);
		} else {
			throw new PetException("개통령 콘테스트 조회에 실패하였습니다.");
		}
		
		return "petContest";
	}
	
//	@GetMapping("pet/petPhoto")
//	   public String petPhotoView(HttpSession session, Model model, @RequestParam(value="page", defaultValue="1") int page,
//			   @RequestParam(value="petName", required=false) Integer petId, HttpServletRequest request) {
//	      String id = ((Member)session.getAttribute("loginUser")).getMbId();
//	      HashMap<String, Object> map = new HashMap<>();
//	      map.put("id", id);
//       map.put("petId", petId);
//       
//       int listCount = pService.getListCount(1);
//       PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
//       
//	      ArrayList<Album> aList = pService.selectMyAlbums(map);
//	      ArrayList<Pet> pList = pService.petInfoList(id);
//	      ArrayList<Reply> rList = pService.repliesList(id);
//	      
//	      if(aList != null) {
//	    	 model.addAttribute("pi", pi);
//	    	 model.addAttribute("loc", request.getRequestURI());
//	         model.addAttribute("pList", pList);
//	         model.addAttribute("aList", aList);
//	         model.addAttribute("rList", rList);
//	         return "petPhoto";
//	         
//	      } else {
//	         throw new PetException("마이펫 사진첩 조회에 실패하였습니다.");
//	      }
//	   }

	@GetMapping("pet/petDetail/{petId}")
	public String petDetail(@PathVariable("petId") int petId, Model model) {
		Pet pet = pService.petDetail(petId);
		
		if(pet != null) {
			model.addAttribute("p", pet);
			return "petDetail";
		} else {
			throw new PetException("마이펫 상세조회에 실패하였습니다.");
		}
	}

	@GetMapping("pet/petAdd")
	public String petAddView() {
		return "petAdd";
	}
	
	@PostMapping("/petAdd.dw")
	public String petAdd(@ModelAttribute Pet p, HttpSession session, @RequestParam("file") MultipartFile file) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		p.setOwnerId(id);
		
		if(file != null && !file.isEmpty()) {
			String savedFileName = saveFile(file);
			Attachment att = new Attachment();
			att.setOriginalName(file.getOriginalFilename());
			att.setRenameName(savedFileName);
			att.setAttmRefType("PET");
			att.setAttmRefNo(p.getPetId());
			p.setPetProfile(savedFileName);
		} else {
			p.setPetProfile("default_petprofile.jpg");
		}
		int result = pService.petAdd(p);
		
		return "redirect:/pet/petInfo";
	}
	
	@PostMapping("/petEditName.dw")
	public String petEditName(Model model, @RequestParam("petName") String newPetName, @RequestParam("petId") String petId) {
		
		HashMap<String,String> petInfo = new HashMap<String,String>(); 
		
		petInfo.put("newPetName", newPetName);
		petInfo.put("petId", petId);
		
		int result = pService.petEditName(petInfo);
		if(result>0) {
			model.addAttribute("p", result);
			return "redirect:pet/petDetail/"+petId;
		} else {
			throw new PetException("마이펫 수정에 실패하였습니다.");
		}
	}
	
	@PostMapping("/petEditIntro.dw")
	public String petEditIntro(Model model, @RequestParam("petIntro") String newPetIntro, @RequestParam("petId") String petId) {
		
		HashMap<String,String> petInfo = new HashMap<String,String>(); 
		
		petInfo.put("newPetIntro", newPetIntro);
		petInfo.put("petId", petId);
		
		int result = pService.petEditIntro(petInfo);
		if(result>0) {
			model.addAttribute("p", result);
			return "redirect:pet/petDetail/"+petId;
		} else {
			throw new PetException("마이펫 수정에 실패하였습니다.");
		}
	}
	
	@PostMapping("/petEditBirth.dw")
	public String petEditBirth(Model model, @RequestParam("petBirth") String newPetBirth, @RequestParam("petId") String petId) {
		HashMap<String,String> petInfo = new HashMap<String,String>(); 
		
		petInfo.put("newPetBirth", newPetBirth);
		petInfo.put("petId", petId);
		
		int result = pService.petEditBirth(petInfo);
		if(result>0) {
			model.addAttribute("p", result);
			return "redirect:pet/petDetail/"+petId;
		} else {
			throw new PetException("마이펫 수정에 실패하였습니다.");
		}
	}
	
	@PostMapping("/petEditBreed.dw")
	public String petEditBreed(Model model, @RequestParam("petBreed") String newPetBreed, @RequestParam("petId") String petId) {
		HashMap<String,String> petInfo = new HashMap<String,String>(); 
		
		petInfo.put("newPetBreed", newPetBreed);
		petInfo.put("petId", petId);
		
		int result = pService.petEditBreed(petInfo);
		if(result>0) {
			model.addAttribute("p", result);
			return "redirect:pet/petDetail/"+petId;
		} else {
			throw new PetException("마이펫 수정에 실패하였습니다.");
		}
	}
	
	@PostMapping("/petEditWeight.dw")
	public String petEditWeight(Model model, @RequestParam("petWeight") String newPetWeight, @RequestParam("petId") String petId) {
		HashMap<String,String> petInfo = new HashMap<String,String>(); 
		
		petInfo.put("newPetWeight", newPetWeight);
		petInfo.put("petId", petId);
		
		int result = pService.petEditWeight(petInfo);
		if(result>0) {
			model.addAttribute("p", result);
			return "redirect:pet/petDetail/"+petId;
		} else {
			throw new PetException("마이펫 수정에 실패하였습니다.");
		}
	}
	
	@PostMapping("/petEditGender.dw")
	public String petEditGender(Model model, @RequestParam("petGender") String newPetGender, @RequestParam("petId") String petId) {
		HashMap<String,String> petInfo = new HashMap<String,String>(); 
		
		petInfo.put("newPetGender", newPetGender);
		petInfo.put("petId", petId);
		
		int result = pService.petEditGender(petInfo);
		if(result>0) {
			model.addAttribute("p", result);
			return "redirect:pet/petDetail/"+petId;
		} else {
			throw new PetException("마이펫 수정에 실패하였습니다.");
		}
	}
	
	@PostMapping("/petEditMemo.dw")
	public String petEditMemo(Model model, @RequestParam("petMemo") String newPetMemo, @RequestParam("petId") String petId) {
		HashMap<String,String> petInfo = new HashMap<String,String>(); 
		
		petInfo.put("newPetMemo", newPetMemo);
		petInfo.put("petId", petId);
		
		int result = pService.petEditMemo(petInfo);
		if(result>0) {
			model.addAttribute("p", result);
			return "redirect:pet/petDetail/"+petId;
		} else {
			throw new PetException("마이펫 수정에 실패하였습니다.");
		}
	}
	
	@PostMapping("/editPetPhoto.dw")
	public String editPetPhoto(@RequestParam("file") ArrayList<MultipartFile> file, @RequestParam("petId") int petId) {
		 
		 MultipartFile upload = file.get(0); //file에서 첫 번째 파일 가져오기
		 
		 Pet p = pService.petDetail(petId); //petId 정보 가져오기
		 
		 if(!upload.getOriginalFilename().equals("")) { //업로드된 파일이 존재하면서
			 if(!p.getPetProfile().equals("default_petprofile.jpg")) { //디폴트 사진과 파일명이 같다면 
				 deleteFile(p.getPetProfile());  //기존 사진 삭제
			 }
			 
			 String renameName = saveFile(upload); //사용자가 업로드된 파일 저장
			 if(renameName != null) { 
				 p.setPetProfile(renameName); //새 파일명을 petPhoto에 세팅
			 }
		 }
		 
		 int result = pService.editPetPhoto(p);
		 
		 if(result > 0) {
			 return "redirect:pet/petDetail/"+petId;
		 } else {
			 throw new PetException("펫 프로필 수정에 실패하였습니다.");
		 }
	}

	private String saveFile(MultipartFile file) {
		String os = System.getProperty("os.name").toLowerCase();
		String savePath = null;
		if (os.contains("win")) {
			savePath = "C:\\" + "\\woofly";
		} else if (os.contains("mac")) {
			savePath = "/Users/younjun/Desktop/WorkStation/uploadFiles/woofly";
		}
		
		File folder = new File(savePath);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		// 2. 저장된 file rename 
		Date time = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		int ranNum = (int)(Math.random()*100000);
		
		String originFileName = file.getOriginalFilename();
		String renameFileName = sdf.format(time) + ranNum + originFileName.substring(originFileName.lastIndexOf("."));
		
		// 3. rename된 파일을 저장소에 저장
		String renamePath = folder + "/" + renameFileName;
		try {
			file.transferTo(new File(renamePath));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		
		return renameFileName;
	}

	private void deleteFile(String fileName) {
		String os = System.getProperty("os.name").toLowerCase();
		String savePath = null;
		if (os.contains("win")) {
			savePath = "C:\\" + "\\woofly";
		} else if(os.contains("mac")) {
			savePath = "/Users/younjun/Desktop/WorkStation/uploadFiles/woofly/";
		}
		File f = new File(savePath + fileName);
		if(f.exists()) {
			f.delete();
		}
	}

	@GetMapping("/deletePetPhoto.dw")
	public String deletePetPhoto(@RequestParam("petId") int petId) {
		
		Pet p = pService.petDetail(petId);
		if(!p.getPetProfile().equals("default_petprofile.jpg")) {
			deleteFile(p.getPetProfile());
			
			int result = pService.deletePetPhoto(petId);
			
			if(result == 0) {
				throw new PetException("펫 프로필 사진 삭제에 실패하였습니다");
			} else {
				p.setPetProfile("default_petprofile.jpg");
			}
			
		}
		return "redirect:pet/petDetail/"+petId;
	}
	
	@GetMapping("pet/petDelete/{petId}")
	public String petDelete(@PathVariable("petId") int petId) {
		
		int result = pService.petDelete(petId);
		
		if(result > 0) {
			return "redirect:/pet/petInfo";	
		} else {
			throw new PetException("마이펫 삭제에 실패하였습니다.");
		}
	}
	
	@GetMapping("pet/petPhotoDetail/{abNo}")
	public String petPhotoDetail(@PathVariable("abNo") int abNo, Model model, HttpSession session) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		ArrayList<Album> aList = pService.petPhotoDetail(abNo);
		ArrayList<Pet> pList = pService.petInfo(abNo); 
		ArrayList<Reply> rList = pService.replyList(abNo);
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		
		map.put("abNo", abNo);
		map.put("id", id);
		int likeCount = pService.albumLike(map);
		ArrayList<ReplyLike> likeList = new ArrayList<>();
		for(Reply r: rList) {
			Reply tempR = new Reply();
			tempR.setRNo(r.getRNo());
			tempR.setMbId(id);
			
			likeList.add(pService.selectReplyLike(tempR));
		}
		System.out.println(likeList);
		if(aList != null) {
			model.addAttribute("aLike", likeCount);
			model.addAttribute("aList", aList);
			model.addAttribute("pList", pList);
			model.addAttribute("rList", rList);
			model.addAttribute("lList", likeList);
			return "petPhotoDetail";
		} else {
			throw new PetException("마이펫 사진첩 조회에 실패하였습니다.");
		}
	}
	
	@GetMapping("insertDeleteReply.dw")
	@ResponseBody
	public String insertDeleteReply(@RequestParam("rNo") int rNo, @RequestParam("replyInDel") String replyInDel, HttpSession session) {
		HashMap<String, Object> map = new HashMap<>();
		String id = ((Member)session.getAttribute("loginUser")).getMbId();

		map.put("rNo", rNo);
		map.put("id", id);
		
		int result = 0;
		if (replyInDel.equals("delete")) {
			result = pService.deleteReplyLike(map);
		} else {
			result = pService.insertReplyLike(map);
			result = pService.insertReplyNotice(map);
		}
			
		if (result > 0) {
			return "good";
		} else {
			return "bad";
					
		}
	}
	
	@GetMapping("insertDeleteBoardLike.dw")
	@ResponseBody
	public String insertDeleteBoardLike(@RequestParam("abNo") int abNo, @RequestParam("replyInDel") String replyInDel, HttpSession session) {
		HashMap<String, Object> map = new HashMap<>();
		String id = ((Member)session.getAttribute("loginUser")).getMbId();

		map.put("abNo", abNo);
		map.put("id", id);
		
		int result = 0;
		if (replyInDel.equals("delete")) {
			result = pService.deleteBoardLike(map);
		} else {
			result = pService.insertBoardLike(map);
			result = pService.insertBoardNotice(map);
		}
			
		if (result > 0) {
			return "good";
		} else {
			return "bad";
		}
	}
	
	@GetMapping("/pet/petPhotoWrite")
	public String petpetPhotoWriteView(HttpSession session, @ModelAttribute Diary d, Model model) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		ArrayList<Pet> list = pService.petInfoList(id);
		model.addAttribute("list", list);
		
		return "petPhotoWrite";
	}
	
	@PostMapping("/petPhotoWrite.dw")
	public String insertPetPhoto(@RequestParam("saveFile") ArrayList<MultipartFile> files, HttpSession session, @ModelAttribute Album a, @ModelAttribute Pet p) {
		//게시판 내용 보내기
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		a.setWriterId(id);
		a.setPetId(p.getPetId());
		
		System.out.println(files);
		
		//게시물 보내기
		int result = pService.insertPetPhoto(a);
		
		//사진들 보내기
		int result2 = 0;

		if(result > 0) {
			if(files != null && !files.isEmpty()) {
				ArrayList<Attachment> attachments = new ArrayList<>();
				for(MultipartFile file : files) {
					String savedFileName = saveFile(file);
					Attachment att = new Attachment();
					att.setOriginalName(file.getOriginalFilename());
					att.setRenameName(savedFileName);
					att.setAttmRefType("AB");
					att.setAttmRefNo(a.getAbNo());
					attachments.add(att);
					
					for(int i=0; i < attachments.size(); i++) {
			          Attachment at = attachments.get(i);
			          if(i == 0) {
			             at.setAttmLevel(1);
			          } else {
			             at.setAttmLevel(2);
			          }
			       }
					result2 = pService.insertPetAlbum(att);
				}
			}
		} else {
			throw new PetException("마이펫 사진첩 등록에 실패하였습니다.");
		}
		
		if(result > 0) {
			System.out.println(a.getAbNo());
			return "redirect:/pet/petPhotoDetail/"+a.getAbNo();
		} else {
			throw new PetException("마이펫 사진첩 등록에 실패하였습니다.");
		}
	}
	
	@GetMapping("deleteImage.dw")
	@ResponseBody
	public String deleteImage(@ModelAttribute Attachment a) {
		System.out.println(a);
		int result = pService.deleteImage(a);
		if (result >0) {
			return "good";
		} else {
			return "bad";
		}
    }
	
	@GetMapping("pet/petPhotoEdit/{abNo}")
	public String petPhotoEditView(@PathVariable("abNo") int abNo, Model model, HttpSession session) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		
		Album a = pService.petAlbumDetail(abNo);
		ArrayList<Pet> pList = pService.petInfoList(id);
		ArrayList<Attachment> aList = pService.petAttmList(abNo);
		
		if(a != null) {
			model.addAttribute("a", a);
			model.addAttribute("pList", pList);
			model.addAttribute("aList", aList);
			return "petPhotoEdit";
		} else {
			throw new PetException("마이펫 사진첩 수정에 실패하였습니다.");
		}
		
	}
	
	@PostMapping("petPhotoEdit.dw")
	public String petPhotoEdit(@RequestParam("saveFile") ArrayList<MultipartFile> files, @RequestParam("abNumber") int abNo, @ModelAttribute Album a) {
		a.setAbNo(abNo);
		
		//게시물 보내기
		int result = pService.updatePetPhoto(a);
		
		//사진들 보내기
		int result2 = 0;

		if(result > 0) {
			if(files != null && !files.isEmpty()) {
				ArrayList<Attachment> attachments = new ArrayList<>();
				for(MultipartFile file : files) {
					String savedFileName = saveFile(file);
					Attachment att = new Attachment();
					att.setOriginalName(file.getOriginalFilename());
					att.setRenameName(savedFileName);
					att.setAttmRefType("AB");
					att.setAttmRefNo(a.getAbNo());
					attachments.add(att);
					
					for(int i=0; i < attachments.size(); i++) {
			          Attachment at = attachments.get(i);
			          if(i == 0) {
			             at.setAttmLevel(1);
			          } else {
			             at.setAttmLevel(2);
			          }
			       }
					result2 = pService.insertPetAlbum(att);
				}
			}
		} else {
			throw new PetException("마이펫 사진첩 등록에 실패하였습니다.");
		}
		
		if(result > 0) {
			System.out.println(a.getAbNo());
			return "redirect:/pet/petPhotoDetail/"+a.getAbNo();
		} else {
			throw new PetException("마이펫 사진첩 등록에 실패하였습니다.");
		}
	}
	
	//댓글
	@GetMapping(value="/insertReply.dw")
	@ResponseBody
	public String insertReply(@ModelAttribute Reply r, HttpSession session) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		r.setMbId(id);
		r.setBType("AB");
		int result = pService.insertAlbumReply(r);
		
		if(result > 0) {
			return "good";
			
		} else {
			return "bad";
		}
	}
	
	@GetMapping("/updateReply.dw")
	@ResponseBody
	public String updateReply(@ModelAttribute Reply r){
		int result = pService.updateReply(r);
		
		if(result > 0) {
			return "good";
			
		} else {
			return "bad";
		}
	}
	
	@GetMapping("/deleteReply.dw")
	@ResponseBody
	public String deleteReply(@ModelAttribute Reply r){
		int result = pService.deleteReply(r);
		
		if(result > 0) {
			return "good";
			
		} else {
			return "bad";
		}
	}
	
	//댓글 신고
	@GetMapping("/insertReport.dw")
	@ResponseBody
	public String insertReport(@ModelAttribute Report rt, HttpSession session){
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		rt.setRAccuser(id);
		rt.setRType("R");
		int checkResult = pService.checkResult(rt);
		int result = pService.insertReport(rt);
		
		if(checkResult > 0) {
			return "exist";
		}
		if(result > 0) {
			return "good";
			
		} else {
			return "bad";
		}
	}
	
	@GetMapping("pet/petDiaryWrite")
	public String petDiaryWriteView(Model model, HttpSession session) { 
		//보유 강아지 리스트 뿌리기
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		ArrayList<Pet> list = pService.petInfoList(id);
		model.addAttribute("list", list);
		
		return "petDiaryWrite";
	}
	
	@PostMapping("/petDiaryWrite.dw")
	public String petDiaryWrite(@ModelAttribute Diary d, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam("petName") int petId, HttpSession session) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		d.setDrDate(date);
	    d.setWriterId(id);
	    d.setPetId(petId);
	    
	    int result = pService.petDiaryWrite(d);
	    int drNo = d.getDrNo();
	    
	    if(result > 0) {
	    	return "redirect:pet/petDiaryDetail/"+drNo;
	    } else {
	    	throw new PetException("마이펫 다이어리 등록에 실패하였습니다.");
	    }
	}

	@GetMapping("pet/petDiaryDetail")
	public String petDiaryDetailView(@ModelAttribute Diary d, @RequestParam("petId") int petId, HttpSession session) {
		return "petDiaryDetail";
	}
	
	@GetMapping("pet/petDiaryDetail/{drNo}")
	public String petDiaryDetail(@PathVariable("drNo") int drNo, Model model) {
		Diary list = pService.petDiaryDetail(drNo);
		
		if(list != null) {
			model.addAttribute("d", list);
			return "petDiaryDetail";
		} else {
			throw new PetException("마이펫 다이어리 조회에 실패하였습니다.");
		}
	}
	
	@GetMapping("pet/petDiaryEdit/{drNo}")
	public String petDiaryEditView(@PathVariable("drNo") int drNo, Model model, HttpSession session) {
		//수정화면 뿌려주는 곳
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		
		Diary list = pService.petDiaryDetail(drNo);
		ArrayList<Pet> pet = pService.petInfoList(id); //가지고 있는 pet리스트 뿌려줘야해서
		//System.out.println(pet);
		if(list != null) {
			model.addAttribute("d", list);
			model.addAttribute("pet", pet);
			return "petDiaryEdit";
		} else {
			throw new PetException("마이펫 다이어리 조회에 실패하였습니다.");
		}
	}
	
	@PostMapping("/petDiaryEdit.dw")
	public String petDiaryEdit(@ModelAttribute Diary d, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		d.setDrDate(date);
		int result = pService.petDiaryEdit(d);
		int drNo = d.getDrNo();
		
		if(result > 0) {
			return "redirect:pet/petDiaryDetail/"+drNo;
		} else {
			throw new PetException("마이펫 다이어리 수정에 실패하였습니다.");
		}
	}
	
	@GetMapping("pet/petDiaryDelete/{drNo}")
	public String petDiaryDelete(@PathVariable("drNo") int drNo) {
		int result = pService.petDiaryDelete(drNo);
		
		if(result > 0) {
			return "redirect:/pet/petDiary";	
		} else {
			throw new PetException("마이펫 다이어리 삭제에 실패하였습니다.");
		}
	}
	
	@GetMapping("pet/petPhotoDelete/{abNo}")
	public String petPhotoDelete(@PathVariable("abNo") int abNo) {
		int result = pService.petPhotoDelete(abNo);
		
		if(result > 0) {
			return "redirect:/pet/petPhoto";	
		} else {
			throw new PetException("마이펫 사진첩 삭제에 실패하였습니다.");
		}
	}
	
}