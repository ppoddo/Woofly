package com.kh.woofly.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kh.woofly.common.interceptor.CheckAdminInterceptor;
import com.kh.woofly.common.interceptor.CheckLoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("win")) {

			registry.addResourceHandler("/image/**").addResourceLocations("file:///C:/woofly/");

		} else if (os.contains("mac")){
			registry.addResourceHandler("/image/**")
					.addResourceLocations("file:/Users/younjun/Desktop/WorkStation/uploadFiles/woofly/");
		}
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) { // Interceptor 등록해주는 메소드
		// 일반계정용
		registry.addInterceptor(new CheckLoginInterceptor())
			.addPathPatterns("/my/**")
			.addPathPatterns("/pet/**");

		registry.addInterceptor(new CheckAdminInterceptor()).addPathPatterns("/*.ad");
	}
}
