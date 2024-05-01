package com.kh.woofly.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.kh.woofly.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CheckAdminInterceptor implements HandlerInterceptor {
	 @Override
	   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	         throws Exception {
		   		//interceptor가 언제 끼어드느냐? >> url 처리하기 전에 
		   
	         HttpSession session = request.getSession();
	         Member loginUser = (Member)session.getAttribute("loginUser");
	         if(loginUser == null || loginUser.getIsAdmin().equals("N")) {
	        	 response.setContentType("text/html; charset=UTF-8");
	        	 response.getWriter().write("<script>alert('접근이 불가합니다.'); location.href='/';</script>");
	            	return false;
	         }
	      
	      return HandlerInterceptor.super.preHandle(request, response, handler);
	   }
	   
	   @Override
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
				ModelAndView modelAndView) throws Exception {
		   
			HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
		}
	}