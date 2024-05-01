package com.kh.woofly.member.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.woofly.common.PageInfo;
import com.kh.woofly.common.Pagination;
import com.kh.woofly.contest.model.service.ContestService;
import com.kh.woofly.contest.model.vo.ContestAttm;
import com.kh.woofly.contest.model.vo.Participants;
import com.kh.woofly.member.model.exception.MemberException;
import com.kh.woofly.member.model.service.MemberService;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.member.model.vo.MemberAddress;
import com.kh.woofly.member.model.vo.Notification;
import com.kh.woofly.member.model.vo.Payment;
import com.kh.woofly.member.model.vo.Point;
import com.kh.woofly.shop.model.service.ShopService;
import com.kh.woofly.shop.model.vo.Product;
import com.kh.woofly.shop.model.vo.ProductAttm;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@SessionAttributes({"notifications", "notificationStatus"})
@Controller
public class MemberController {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private MemberService mService;
	
	@Autowired
	private ContestService cService;
	
	@Autowired
	private ShopService sService;
	
	final DefaultMessageService messageService;

    public MemberController() {
        this.messageService = NurigoApp.INSTANCE.initialize("NCS8XEQOM4HOQA2T", "SXJCPAE5YMVCBQSKAJ4T48AYDSNHWKAU", "https://api.coolsms.co.kr");
    }
	
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
    	if (session.getAttribute("loginUser") != null) {
    		HashMap<String, String> map = new HashMap<>();  
    		String id = ((Member)session.getAttribute("loginUser")).getMbId();
    		map.put("id", id);
    		ArrayList<Notification> list = mService.selectNotification(map);
    		boolean isAllRead = true;
    		for(Notification noti : list) {
    			if (noti.getIsRead().equals("N")) {
    				isAllRead = false;
    				break;
    			}
    		}
			model.addAttribute("notificationStatus", isAllRead);
    		model.addAttribute("notifications", list);
    	}
    	
    	int cNo = cService.todayContestNo();
    	
    	ArrayList<Participants> participantsList = cService.topFiveBest(cNo);
    	ArrayList<ContestAttm> cAttmList = cService.selectAttmNList();
    	
    	ArrayList<Product> recentlyProducts = sService.recentlyProductFive();
    	ArrayList<Product> popularityProducts = sService.popularityProductFive();
    	ArrayList<ProductAttm> pAttmList = sService.selectProductAttm(null);
    	
    	System.out.println(pAttmList);
    	System.out.println(recentlyProducts);
    	System.out.println(popularityProducts);
    	System.out.println(cAttmList);
    	System.out.println(cNo);
    	System.out.println(participantsList);
    	
    	model.addAttribute("pAttmList", pAttmList);
    	model.addAttribute("popularityProducts", popularityProducts);
    	model.addAttribute("recentlyProducts", recentlyProducts);
    	model.addAttribute("cAttmList", cAttmList);
    	model.addAttribute("participantsList", participantsList);
    	
    	
    	return "index";

    }
    
    @GetMapping("readNotifications.yj")
    @ResponseBody
    public String readNotification(HttpSession session) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		int result = mService.readNotification(id);
		
		return "" + result;
    }
    
    @GetMapping("deleteNotifications.yj")
    @ResponseBody
    public String deleteNotification(@RequestParam("notiNo") String notiNo) {
    	int result = mService.deleteNotification(notiNo);
		
    	if (result > 0) {
    		return "good";
    	} else {
    		return "bad";
    	}
    }
    
    
	@GetMapping("my/login-edit")
	public String loginView(Model model) {
		return "myLogin";
	}

	@GetMapping("my/profile-edit")
	public String profileView(HttpSession session, Model model) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		ArrayList<Member>  list = mService.getBlackList(id);
		if(list != null) {
			model.addAttribute("list", list);
		}
		return "myProfile";
	}

	@GetMapping("my/address")
	public String addressView(HttpSession session, Model model) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		ArrayList<MemberAddress> list = mService.selectMyAddress(id);
		System.out.println(list);
		if(list != null) {
			model.addAttribute("list", list);
		}
		return "myAddress";
	}

	@GetMapping("my/payment")
	public String paymentView(HttpSession session, Model model) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		ArrayList<Payment> list = mService.selectMyPayment(id);
		if(list != null) {
			model.addAttribute("list", list);
		}
		return "myPayment";
	}

	@GetMapping("my/point")
	public String pointView(HttpSession session, Model model, @RequestParam(value="page", defaultValue="1") int page) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();

		
        int result = mService.deletePoints(id);
        
        int listCount = mService.getPointsCount(id);
        PageInfo pi = new Pagination().getPageInfo(page, listCount, 10);
        ArrayList<Point> pList = mService.selectMyPoints(pi, id);
        int pointsUsable = 0;
        int pointsExpiring = 0;
        for (Point p : pList) {
        	if(p.getTransactionType().equals("A")) {
        		pointsUsable += p.getPointChange();
        	} else {
        		pointsUsable -= p.getPointChange();
        	}
        	
            Date currentDate = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            calendar.add(Calendar.MONTH, -11);
            Date elevenMonthsBefore = calendar.getTime();
            calendar.add(Calendar.MONTH, -1);
            Date oneYearBefore = calendar.getTime();


        	if(p.getTransactionDatetime().before(elevenMonthsBefore) && p.getTransactionDatetime().after(oneYearBefore)) {
        		pointsExpiring += p.getPointChange();
        	}
        }
        
        
        if(pList != null) {
        	model.addAttribute("pointsUsable", pointsUsable);
        	model.addAttribute("pointsExpiring", pointsExpiring);
        	model.addAttribute("list", pList);
        	model.addAttribute("pi", pi);
        	return "myPoint";
        } else {
        	throw new MemberException("포인트 조회 실패");
        }
        
	}

	@GetMapping("my/addPayment")
	public String addPayment(@RequestParam("authKey") String authKey, @RequestParam("customerKey") String customerKey) {
		String billingKey = Base64.getEncoder().encodeToString("test_sk_kYG57Eba3G6AeDn45qa98pWDOxmA:".getBytes());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.tosspayments.com/v1/billing/authorizations/issue"))
				.header("Authorization", "Basic " + billingKey).header("Content-Type", "application/json")
				.method("POST",
						BodyPublishers
								.ofString("{\"authKey\":\"" + authKey + "\",\"customerKey\":\"" + customerKey + "\"}"))
				.build();
		int result = 0;
		try {
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
			System.out.println(response.body());
			HashMap<String, Object> map = new ObjectMapper().readValue(response.body(), HashMap.class);
			System.out.println(map);
			result = mService.addPayment(map);
			
		} catch (InterruptedException | IOException var7) {
			var7.printStackTrace();
		}
		if(result > 0) {
			return "redirect:/my/payment";
		} else {
			throw new MemberException("결제 정보 저장에 실패하였습니다.");
		}
	}
	
	@GetMapping("addPayment.yj")
	@ResponseBody
	public String addCard(@RequestParam("authKey") String authKey, @RequestParam("customerKey") String customerKey) {
		String billingKey = Base64.getEncoder().encodeToString("test_sk_kYG57Eba3G6AeDn45qa98pWDOxmA:".getBytes());
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.tosspayments.com/v1/billing/authorizations/issue"))
				.header("Authorization", "Basic " + billingKey).header("Content-Type", "application/json")
				.method("POST",
						BodyPublishers
								.ofString("{\"authKey\":\"" + authKey + "\",\"customerKey\":\"" + customerKey + "\"}"))
				.build();
		int result = 0;
		try {
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
			System.out.println(response.body());
			HashMap<String, Object> map = new ObjectMapper().readValue(response.body(), HashMap.class);
			System.out.println(map);
			result = mService.addPayment(map);
			
		} catch (InterruptedException | IOException var7) {
			var7.printStackTrace();
		}
		if(result > 0) {
			return "good";
		} else {
			throw new MemberException("결제 정보 저장에 실패하였습니다.");
		}
	}
	@GetMapping("updatePayment.yj")
	public String updatePayment(@RequestParam("paymentNo") int paymentNo, HttpSession session) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		Payment payment = new Payment();
		payment.setPaymentNo(paymentNo);
		payment.setMemberId(id);
		int result1 = mService.updatePaymentToN(payment);
		int result2 = mService.updatePayment(payment);
		if (result1 + result2 > 0) {
			return "redirect:/my/payment"; 
		} else {
			throw new MemberException("기본 결제 변경에 실패하였습니다");
		}
	}
	
	@GetMapping("deletePayment.yj")
	public String deletePayment(@RequestParam("paymentNo") int paymentNo) {
		int result = mService.deletePayment(paymentNo);
		if(result > 0 ) {
			return "redirect:/my/payment";
		} else {
			throw new MemberException("결제 정보에 실패하였습니다");
		}
	}

	@GetMapping("my/profile")
	public String memberView() {
		return "profile";
	}

	@GetMapping("checkPwd.yj")
	@ResponseBody
	public String checkPwd(@RequestParam("currentPwd") String currentPwd, HttpSession session) {
		String pwd = ((Member) session.getAttribute("loginUser")).getMbPwd();
		String result = "N";
		if (bcrypt.matches(currentPwd, pwd)) {
			result = "Y";
		}

		return result;
	}
	
	@PostMapping("updatePwd.yj")
	public String updatePwd(@RequestParam("newPwd") String mbPwd, HttpSession session) {
		Member loginUser = ((Member) session.getAttribute("loginUser"));
		loginUser.setMbPwd(bcrypt.encode(mbPwd));
		
		int result = mService.updatePwd(loginUser);
		
		if(result > 0) {
			return "redirect:/my/login-edit";
		} else {
			throw new MemberException("비밀번호 변경에 실패하였습니다");
		}
		
	}
	
	@PostMapping("removeBlock.yj")
	public String removeBlock(@RequestParam("blockedEntity") String blockedEntity, HttpSession session) {
		HashMap<String, String> map = new HashMap<>();
		String id = ((Member)session.getAttribute("loginUser")).getMbId();

		map.put("loginUser", id);
		map.put("blockedEntity", blockedEntity);
		int result = mService.removeBlock(map);
		
		if (result > 0) {
			return "redirect:/my/profile-edit";
		} else {
			throw new MemberException("차단 해제에 실패하였습니다");
		}
	}
	
	@GetMapping("nicknameCheck.yj")
	@ResponseBody
	public String nicknameCheck(@RequestParam("nickname") String nickname, HttpSession session) {
		Member loginUser = ((Member)session.getAttribute("loginUser"));
		if (nickname.equals(loginUser.getMbNickName())) {
			return "using";
		} else {
			int result = mService.nicknameCheck(nickname);
			if (result > 0) {
				return "bad";
			} else {
				return "good";
			}
		}
		
	}
	
	@PostMapping("editNickName.yj")
	public String editNickName(@RequestParam("newNickName") String newNickName, HttpSession session) {
		Member loginUser = ((Member)session.getAttribute("loginUser"));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 30);
		Date date = cal.getTime();
		loginUser.setNextChange(date);
		loginUser.setMbNickName(newNickName);
		int result = mService.editNickName(loginUser);
		
		if(result > 0) {
			return "redirect:/my/profile-edit";
		} else {
			throw new MemberException("프로필 이름 변경에 실패하였습니다.");
		}
	}
	
	@PostMapping("editName.yj")
	public String editName(@RequestParam("newName") String newName, HttpSession session) {
		Member loginUser = ((Member)session.getAttribute("loginUser"));
		loginUser.setMbName(newName);
		
		int result = mService.editName(loginUser);
		
		if(result > 0) {
			return "redirect:/my/profile-edit";
		} else {
			throw new MemberException("이름 변경에 실패하였습니다.");
		}
	}
	
	@PostMapping("editIntro.yj")
	public String editIntro(@RequestParam("newIntro") String newIntro, HttpSession session) {
		Member loginUser = ((Member)session.getAttribute("loginUser"));
		loginUser.setMbIntro(newIntro);
		
		int result = mService.editIntro(loginUser);
		
		if(result > 0) {
			return "redirect:/my/profile-edit";
		} else {
			throw new MemberException("소개글 변경에 실패하였습니다.");
		}
	}
	
	@GetMapping("editIsPrivate.yj")
	public String editIsPrivate(@RequestParam("isPrivate") boolean isPrivate, HttpSession session) {
		Member loginUser = ((Member)session.getAttribute("loginUser"));
		loginUser.setIsPrivate(isPrivate ? "Y" : "N");
		
		int result = mService.editIsPrivate(loginUser);
		
		if( result > 0) {
			return "redirect:/my/profile-edit";
		} else {
			throw new MemberException("공개 범위 변경에 실패하였습니다.");
		}
	}
	
	@PostMapping("editMbPhoto.yj")
	public String editMbPhoto(@RequestParam("file") ArrayList<MultipartFile> file, HttpServletRequest request) {
		Member loginUser = ((Member)request.getSession().getAttribute("loginUser"));
			
		MultipartFile upload = file.get(0);
		if(!upload.getOriginalFilename().equals("")) {
			if (!loginUser.getMbPhoto().equals("default_profile.png")) {
				deleteFile(loginUser.getMbPhoto());
			}
			
			String renameName = saveFile(upload);
			if(renameName != null) {
				loginUser.setMbPhoto(renameName);
			}
		}
		
		if(loginUser.getIsKakao().equals("Y")) {
			loginUser.setIsKakao("N");
		}
		
		int result = mService.editMbPhoto(loginUser);
		
		if (result > 0) {
			return "redirect:/my/profile-edit";
		} else {
			throw new MemberException("프로필 수정에 실패하였습니다.");
		}
	
	}
	
	@GetMapping("deleteMbPhoto.yj")
	public String deleteMbPhoto(HttpSession session) {
		Member loginUser = ((Member)session.getAttribute("loginUser"));
		if (!loginUser.getMbPhoto().equals("default_profile.png")) {
			deleteFile(loginUser.getMbPhoto());
			int result = mService.deleteMbPhoto(loginUser);
			
			if (result == 0) {
				throw new MemberException("프로필 사진 삭제에 실패하였습니다");
			} else {
				loginUser.setMbPhoto("default_profile.png");
			}
		}
		return "redirect:/my/profile-edit";
	}
	


	private void deleteFile(String fileName) {
		String os = System.getProperty("os.name").toLowerCase();
		String savePath = null;
		if (os.contains("win")) {
			savePath = "C:\\woolfy";
		} else if(os.contains("mac")) {
			savePath = "/Users/younjun/Desktop/WorkStation/uploadFiles/woofly/";
		}
		File f = new File(savePath + fileName);
		if(f.exists()) {
			f.delete();
		}
		
	}

	// 파일 저장소 파일 저장(copy)
	public String saveFile(MultipartFile file) {
		String os = System.getProperty("os.name").toLowerCase();
		String savePath = null;
		if (os.contains("win")) {
			savePath = "C:\\woofly";
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
	
	
	@GetMapping("mailCheck.yj")
	@ResponseBody public String sendMail(@RequestParam("to") String to) throws Exception { 
		Random r = new Random(); int checkNum = r.nextInt(888888) + 111111; // 난수 생성 
		String subject = "인증코드"; 
		String content = "인증코드" + checkNum + "입니다"; 
		String from = "testyounjun@gmail.com"; 
		try {
			MimeMessage mail = mailSender.createMimeMessage(); MimeMessageHelper
			mailHelper = new MimeMessageHelper(mail, true, "UTF-8");
		 
			mailHelper.setFrom(from);
		  
			mailHelper.setTo(to); mailHelper.setSubject(subject);
			mailHelper.setText(content, true);
		 
			mailSender.send(mail); 
		} 
		catch (Exception e) { 
			e.printStackTrace(); 
		}
	 
		return checkNum + ""; }
	 
	
	
	@PostMapping("updateEmail.yj")
	public String updateEmail(@RequestParam("to") String to, HttpSession session) {
		Member loginUser = ((Member)session.getAttribute("loginUser"));
		loginUser.setMbEmail(to);
		
		int result = mService.updateEmail(loginUser);
		
		if(result > 0) {
			return "redirect:/my/login-edit";
		} else {
			throw new MemberException("이메일 수정에 실패하였습니다");
		}
	}
	

    @GetMapping("/send-msg")
    @ResponseBody
    public String sendOne(@RequestParam("to") String to) {
    	Random r = new Random();
		int checkNum = r.nextInt(888888) + 111111; // 난수 생성
		String content = "인증코드" + checkNum + "입니다";
		
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01064954499");
        message.setTo(to);
        message.setText(content);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        if(response.getStatusCode().equals("2000")) {
        	return "" + checkNum;
        } else {
        	return "bad";
        }
    }
    
    @PostMapping("updatePhone.yj")
    public String updatePhone(@RequestParam("phone") String phone, HttpSession session) {
		Member loginUser = ((Member)session.getAttribute("loginUser"));
		
		loginUser.setMbTel(phone);
		int result = mService.updatePhone(loginUser);
		if(result > 0) {
			return "redirect:/my/login-edit";
		} else {
			throw new MemberException("핸드폰 번호 수정에 실패하였습니다");
		}
    	
    }
    
    @GetMapping("updateMbStatus.yj")
    public String updateMbStatus(HttpSession session) {
    	Member loginUser = (Member)session.getAttribute("loginUser");
    	int result = mService.updateMbStatus(loginUser);
    	if (result > 0) {
    		session.invalidate();
    		return "redirect:/";
    	} else {
    		throw new MemberException("회원탈퇴에 실패하였습니다");
    	}
    }
    
    @PostMapping("addAddress.yj")
    public String addAddress(HttpSession session, @RequestParam("mbName") String mbName, @RequestParam("mbTel") String mbTel, 
    						 @RequestParam("postcode") String postcode, @RequestParam("address") String address, 
    						 @RequestParam("addressDetail") String addressDetail, @RequestParam(value="addrType", defaultValue="N") String addrType) {
    	
    	String mbId = ((Member)session.getAttribute("loginUser")).getMbId(); 
    	MemberAddress mAddress = new MemberAddress(0, postcode, address, addressDetail, mbId, addrType, mbTel, mbName);
    	int result = mService.addAddress(mAddress);
    	if(result > 0) {
    		return "redirect:/my/address";
    	} else {
    		throw new MemberException("주소 추가에 실패하였습니다");
    	}
    }
    
    @GetMapping("addCheckoutAddr.yj")
    @ResponseBody
    public String addCheckoutAddr(HttpSession session, @RequestParam("mbName") String mbName, @RequestParam("mbTel") String mbTel, 
								 @RequestParam("postcode") String postcode, @RequestParam("address") String address, 
								 @RequestParam("addressDetail") String addressDetail, @RequestParam(value="addrType", defaultValue="N") String addrType) {
    	String mbId = ((Member)session.getAttribute("loginUser")).getMbId(); 
    	MemberAddress mAddress = new MemberAddress(0, postcode, address, addressDetail, mbId, addrType, mbTel, mbName);
    	int result = mService.addAddress(mAddress);
    	if(result > 0) {
    		return "" + mAddress.getAddrId();
    	} else {
    		throw new MemberException("주소 추가에 실패하였습니다");
    	}
    	
    }
    
    @GetMapping("checkAddrType.yj")
    @ResponseBody
    public String checkAddrType(HttpSession session) {
    	String mbId = ((Member)session.getAttribute("loginUser")).getMbId(); 
    	int result = mService.checkAddrType(mbId);
    	if(result > 0) {
    		return "bad";
    	} else {
    		return "good";
    	}
    }
    
    @GetMapping("checkAddr.yj")
    @ResponseBody
    public String checkAddr(HttpSession session, @RequestParam("postcode") String postcode, @RequestParam("address") String address, 
    						 @RequestParam("addressDetail") String addressDetail) {
    	String mbId = ((Member)session.getAttribute("loginUser")).getMbId();
    	MemberAddress mAddress = new MemberAddress();
    	mAddress.setAddr(address);
    	mAddress.setPostcode(postcode);
    	mAddress.setAddrDetail(addressDetail);
    	mAddress.setMbId(mbId);
    	int result = mService.checkAddr(mAddress);
    	if(result > 0) {
    		return "bad";
    	} else {
    		return "good";
    	}
    	
    }
    
    @PostMapping("updateAddr.yj")
    public String updateAddress(HttpSession session, @RequestParam("mbName") String mbName, @RequestParam("mbTel") String mbTel, 
    						 @RequestParam("postcode") String postcode, @RequestParam("address") String address, @RequestParam("addrId") int addrId,
    						 @RequestParam("addressDetail") String addressDetail, @RequestParam(value="addrType", defaultValue="N") String addrType) {
    	String mbId = ((Member)session.getAttribute("loginUser")).getMbId(); 
    	MemberAddress mAddress = new MemberAddress(addrId, postcode, address, addressDetail, mbId, addrType, mbTel, mbName);
    	int result = mService.updateAddr(mAddress);
    	if(result > 0) {
    		return "redirect:/my/address";
    	} else {
    		throw new MemberException("주소 추가에 실패하였습니다");
    	}
    }
    
    @GetMapping("deleteAddr.yj")
    public String deleteAddr(@RequestParam("addrId") String addrId) {
    	int result = mService.deleteAddr(addrId);
    	if(result > 0) {
    		return "redirect:/my/address";
    	} else {
    		throw new MemberException("주소 삭제에 실패하였습니다");
    	}
    }
}
