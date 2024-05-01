package com.kh.woofly.account.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.kh.woofly.account.model.exception.AccountException;
import com.kh.woofly.account.model.service.AccountService;
import com.kh.woofly.account.model.service.KakaoLoginService;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.member.model.vo.MemberAddress;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@SessionAttributes("loginUser")
@Controller
public class AccountController {
	
	@Autowired
	private AccountService aService;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private KakaoLoginService kService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	private Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	
	final DefaultMessageService messageService;

    public AccountController() {
        this.messageService = NurigoApp.INSTANCE.initialize("NCS8XEQOM4HOQA2T", "SXJCPAE5YMVCBQSKAJ4T48AYDSNHWKAU", "https://api.coolsms.co.kr");
    }
 
    @GetMapping("checkLogin.dw")
    @ResponseBody
    public String checkLogin(@ModelAttribute Member m) {
       Member loginUser = aService.login(m);
       System.out.println(m);
       System.out.println(loginUser);
       if (loginUser == null) {
          return "noId";
       } else {
          if (loginUser.getIsBanned().equals("Y")) {
             return "banned";
          } else if (bcrypt.matches(m.getMbPwd(), loginUser.getMbPwd())) {
             return "good";
          } else {
             return "wrongPwd";
          }
       }
    }

    

    
    @GetMapping("kakaoLogin")
    public String kakaoLogin(@RequestParam(value="code", required=false) String code, Model model) {
    	System.out.println(code);
    	String accessToken = kService.getAccessToken(code);
    	System.out.println("###access_Token#### : " + accessToken);
		// 위의 access_Token 받는 걸 확인한 후에 밑에 진행
    	
    	// 3번
		HashMap<String, Object> userInfo = kService.getUserInfo(accessToken);
		System.out.println("###id#### : " + userInfo.get("id"));
		System.out.println("###name#### : " + userInfo.get("name"));
		System.out.println("###profileImage#### : " + userInfo.get("profileImage"));
		System.out.println("###birthday#### : " + userInfo.get("birthday"));
		System.out.println("###birthyear#### : " + userInfo.get("birthyear"));
		System.out.println("###phoneNumber#### : " + userInfo.get("phoneNumber"));
		System.out.println("###email#### : " + userInfo.get("email"));
    			
		try {
			
			
	    	String mbId = (String)userInfo.get("id") + "kAkAo";
	    	String name = (String)userInfo.get("name");
	    	String profileImage = (String)userInfo.get("profileImage");
	    	String email = (String)userInfo.get("email");
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    	Date date;
			date = sdf.parse((String)userInfo.get("birthyear") + (String)userInfo.get("birthday"));
			
	    	String phoneNumber = (String)userInfo.get("phoneNumber");
	    	String phone = "0" + phoneNumber.substring(4).replace("-", "");
	    	
	    	Member m;
	    	int result = aService.idCheck(mbId);
	    	System.out.println(mbId);
	    	System.out.println(name);
	    	System.out.println(profileImage);
	    	System.out.println(date);
	    	System.out.println(phone);
	    	int result2;
	    	
	    	if (result > 0) {
	    		
	    		m = new Member();
	    		m.setMbId(mbId);
	    		
	    	} else {
	    		m = new Member();
	    		m.setMbId(mbId);
	    		m.setMbPwd(bcrypt.encode(mbId));
	    		m.setMbName(name);
	    		m.setMbPhoto(profileImage);
	    		m.setMbNickName(mbId);
	    		m.setMbBirth(date);
	    		m.setMbTel(phone);
	    		m.setMbEmail(email);
	    		m.setIsKakao("Y");
	    		result2 = aService.signUpMember(m);
	    		
	    	}
	    	
	    	m = aService.login(m);
	    	model.addAttribute("loginUser", m);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
    	return "redirect:/";
    	
    }
    
    
	@GetMapping("/account/login")
	public String loginView(Model model) {
		
		System.out.println(bcrypt.encode("1"));

		return "login";
	}
	
	@PostMapping("login.dw")
	public String login(@ModelAttribute Member m, Model model, @RequestParam("beforeURL")String beforeURL, HttpSession response) {
		Member loginUser = aService.login(m);
	      
	      if(loginUser != null) {
	         if(bcrypt.matches(m.getMbPwd(), loginUser.getMbPwd())) {
	            model.addAttribute("loginUser",loginUser);
	            
	            if(!beforeURL.equals("http://192.168.20.217:8080/logout.dw") && !beforeURL.equals("http://92.168.20.217:8080/signUp.dw"))
	            {
	               return "redirect:" + beforeURL;
	            }else {
	               return "redirect:home.me";
	            }
	         }else {
	            model.addAttribute("msg", "로그인에 실패하였습니다.\n아이디와 비밀번호를 다시 확인해주세요.");
	            model.addAttribute("searchUrl","views/account/signUp");
	            return "redirect:/account/signUp";
	            
	         }
	         
	      }else {
	         model.addAttribute("msg", "로그인에 실패하였습니다.\n아이디와 비밀번호를 다시 확인해주세요.");
	         model.addAttribute("searchUrl","views/account/signUp");
	         return "redirect:/account/signUp";
	      }
	   }
	@GetMapping("/idCheck.dw")
	@ResponseBody
	public String idCheck(@RequestParam("id") String mbId) {
		
		int result = aService.idCheck(mbId);
		
		if(result > 0) {
			return "false";
		} else {
			return "true";
		}
	}
	
	@GetMapping("/nickCheck.dw")
	@ResponseBody
	public String nickCheck(@RequestParam("nickName")String mbNickName) {
		
		int result = aService.nickCheck(mbNickName);
		
		if(result > 0) {
			return "false";
		} else {
			return "true";
		}
	}
	
	@GetMapping("/account/signUp")
	public String signUpView() {
		return "signUp";
	}
	
	@PostMapping("/signUp.dw")
	public String signUp(@ModelAttribute Member m, @ModelAttribute MemberAddress ma, @RequestParam("mbAddress1")String postcode,@RequestParam("mbAddress2")String address,
			@RequestParam("mbAddress3")String detailAddress,@RequestParam("mbAddress4")String extraAddress) {
		
		ma.setPostcode(postcode);
		ma.setAddr(address);
		ma.setAddrDetail(detailAddress);
		m.setMbPhoto("default_profile.png");
		m.setIsKakao("N");
		
		String encPwd = bcrypt.encode(m.getMbPwd()); //사용자가 입력한 pwd를 bcrypt를 사용하여 암호화
		m.setMbPwd(encPwd); //암호환 pwd를 다시 멤버 객체에 담음 
		
		int result1 = aService.signUpMember(m);
		int result2 = aService.signUpMemberAddr(ma);
		
		if(result1 > 0 && result2 > 0) {
			return "redirect:/account/login";
		} else {
			throw new AccountException("회원가입을 실패하였습니다.");
		}
		
	}
	
	@GetMapping("/send-one")
	@ResponseBody
    public String sendOne(@RequestParam("pNum") String mbPhone) {
        Message message = new Message();
        Random r = new Random();
		int checkNum = r.nextInt(888888) + 111111; // 난수 생성
		
        message.setFrom("010"); // 해킹우려로 인해 지웠음
        message.setTo(mbPhone);
        message.setText("인증코드는 " + checkNum + "입니다");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);
        
        if(response.getStatusCode().equals("2000")) {
        	return "" + checkNum; //정상 발송되었다는 모달창
        			
        }else {
        	return "bad"; //발송 오류 모달창
        }
    }
	
	@GetMapping("/checkNameEmail.dw")
	@ResponseBody
	public String checkNameEmail(@RequestParam("mbName") String mbName, @RequestParam("mbEmail") String mbEmail) {
		Member m = new Member();
		m.setMbName(mbName);
		m.setMbEmail(mbEmail);
		
		int result = aService.checkNameEmail(m);
		
		if(result > 0) {
			return "true";
		} else {
			return "false";
		}
	}
	
	@GetMapping("mailInjeung.dw")
	@ResponseBody
	public String mailFindPwd(HttpServletRequest request,@RequestParam("mbEmail") String mbEmail) throws IOException{
		
        Random r = new Random();
        int checkNum = r.nextInt(888888) + 111111;
        
        
        String title = "Woofly 아이디/비밀번호 찾기 인증 이메일 입니다.";
        String from = "testyounjun@gmail.com";
        String to = mbEmail;
        String content =
                System.getProperty("line.separator")+
                System.getProperty("line.separator")+
                "안녕하세요 Woofly를 다시 찾아주셔서 감사합니다"
                +System.getProperty("line.separator")+
                System.getProperty("line.separator")+
                "인증번호는 " +checkNum+ " 입니다. " 
                +System.getProperty("line.separator");
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(title);
            messageHelper.setText(content); 
            
            mailSender.send(message);
    
        } catch (Exception e) {
            System.out.println(e);
        }
        return checkNum+"";
        
    }
	
	@PostMapping("/sendId.dw")
	public String findId(@RequestParam("mbEmail") String mbEmail, @RequestParam("mbName") String mbName) throws IOException{
		Member info = new Member();
		info.setMbName(mbName);
		info.setMbEmail(mbEmail);

	  	ArrayList<Member> mList = aService.sendId(info);
	  	String id = mList.get(0).getMbId();
    
		String title = "Woofly 아이디 정보 메일입니다.";
		String from = "testyounjun@gmail.com";
		String to = mbEmail;
		String content =
						System.getProperty("line.separator")+
						System.getProperty("line.separator")+
						"안녕하세요 Woofly를 다시 찾아주셔서 감사합니다"
						+System.getProperty("line.separator")+
						System.getProperty("line.separator")+
						mbName +"님의 이름으로 가입된 id 입니다. " 
						+System.getProperty("line.separator");
		for (Member m : mList) {
			content += System.getProperty("line.separator") + m.getMbId() + System.getProperty("line.separator");
		}
		
		try {
		    MimeMessage message = mailSender.createMimeMessage();
		    MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		
		    messageHelper.setFrom(from);
		    messageHelper.setTo(to);
		    messageHelper.setSubject(title);
		    messageHelper.setText(content); 
		    
		    mailSender.send(message);
		
		} catch (Exception e) {
		    System.out.println(e);
		}
		return "login";
		//리스트를 포문 돌려서 출력    
	}
	
	@GetMapping("checkIdEmail.dw")
	@ResponseBody
	public String checkIdEmail(@RequestParam("mbId")String mbId, @RequestParam("mbEmail")String mbEmail) {
		
		Member m = new Member();
		m.setMbId(mbId);
		m.setMbEmail(mbEmail);
		
		int result = aService.checkIdEmail(m);
		if(result > 0) {
			return "true";
		} else {
			return "false";
		}
	}
	
	@PostMapping("/sendPwd.dw")
	public String sendPwd(@ModelAttribute Member m) {
		
		String randomPwd = UUID.randomUUID().toString().replaceAll("-", ""); // -를 제거하고 uuid가 object 타입이기 때문에 toString 메서드 사용
		randomPwd = randomPwd.substring(0, 13); //uuid를 앞에서부터 13자리로 잘라주기
		
        String title = "Woofly 임시비밀번호 입니다.";
        String from = "testyounjun@gmail.com";
        String to = m.getMbEmail();
        String content =
                System.getProperty("line.separator")+
                System.getProperty("line.separator")+
                "안녕하세요 Woofly를 다시 찾아주셔서 감사합니다"
                +System.getProperty("line.separator")+
                System.getProperty("line.separator")+
                "회원님의 임시 비밀번호는 " +randomPwd+ "입니다. " 
                +System.getProperty("line.separator");
        
        String newRandomPwd = bcrypt.encode(randomPwd);
        m.setMbPwd(newRandomPwd);
        
        int result = aService.updatePwd(m);
        if(result > 0) {
        	System.out.println("하하하");
        	try {
        		
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

                messageHelper.setFrom(from);
                messageHelper.setTo(to);
                messageHelper.setSubject(title);
                messageHelper.setText(content); 
                
                mailSender.send(message);
        
            } catch (Exception e) {
                System.out.println(e);
            }
        	
            return "redirect:/account/login"; 
            
        }else {
        	throw new AccountException("임시 비밀번호 발급에 실패하였습니다.");
        }
	}
	
	@GetMapping("/account/findId")
	public String findIdView() { 
		return "findId";
	}
	
	
	@GetMapping("/account/findPwd")
	public String findPwdView() {
		return "findPwd";
	}
	
	
	@GetMapping("/account/logout")
	public String logout(SessionStatus status) {
		status.setComplete();
		return "redirect:/";
	}
	
	

}
