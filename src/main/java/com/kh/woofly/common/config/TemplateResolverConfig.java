package com.kh.woofly.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class TemplateResolverConfig {
	
	@Bean
	public ClassLoaderTemplateResolver defaultResolver() {
		ClassLoaderTemplateResolver slash = new ClassLoaderTemplateResolver();
		slash.setPrefix("templates/views/");
		slash.setSuffix(".html");
		slash.setTemplateMode(TemplateMode.HTML);
		slash.setCharacterEncoding("UTF-8");
		slash.setOrder(1);
		slash.setCacheable(false);
		slash.setCheckExistence(true);
		
		return slash;
	}
	
	@Bean
	public ClassLoaderTemplateResolver myResolver() {
		ClassLoaderTemplateResolver my = new ClassLoaderTemplateResolver();
		my.setPrefix("templates/views/my/");
		my.setSuffix(".html");
		my.setTemplateMode(TemplateMode.HTML);
		my.setCharacterEncoding("UTF-8");
		my.setOrder(2);
		my.setCacheable(false);
		my.setCheckExistence(true);
		
		return my;
	}
	
	@Bean
	public ClassLoaderTemplateResolver boardResolver() {
		ClassLoaderTemplateResolver bo = new ClassLoaderTemplateResolver();
		bo.setPrefix("templates/views/board/");
		bo.setSuffix(".html");
		bo.setTemplateMode(TemplateMode.HTML);
		bo.setCharacterEncoding("UTF-8");
		bo.setOrder(3);
		bo.setCacheable(false);
		bo.setCheckExistence(true);
		
		return bo;
	}
	

	@Bean
	public ClassLoaderTemplateResolver PetResolver() {
		ClassLoaderTemplateResolver pet = new ClassLoaderTemplateResolver();
		pet.setPrefix("templates/views/pet/");
		pet.setSuffix(".html");
		pet.setTemplateMode(TemplateMode.HTML);
		pet.setCharacterEncoding("UTF-8");
		pet.setOrder(2);
		pet.setCacheable(false);
		pet.setCheckExistence(true);
		
		return pet;
	}
	
	@Bean
	public ClassLoaderTemplateResolver accountResolver() {
		ClassLoaderTemplateResolver account = new ClassLoaderTemplateResolver();
		account.setPrefix("templates/views/account/");
		account.setSuffix(".html");
		account.setTemplateMode(TemplateMode.HTML);
		account.setCharacterEncoding("UTF-8");
		account.setOrder(2);
		account.setCacheable(false);
		account.setCheckExistence(true);
		
		return account;
	}
	
	@Bean
	public ClassLoaderTemplateResolver contestResolver() {
		ClassLoaderTemplateResolver contest = new ClassLoaderTemplateResolver();
		contest.setPrefix("templates/views/contest/");
		contest.setSuffix(".html");
		contest.setTemplateMode(TemplateMode.HTML);
		contest.setCharacterEncoding("UTF-8");
		contest.setOrder(2);
		contest.setCacheable(false);
		contest.setCheckExistence(true);
		
		return contest;
	}
	
	@Bean
	public ClassLoaderTemplateResolver infoResolver() {
		ClassLoaderTemplateResolver info = new ClassLoaderTemplateResolver();
		info.setPrefix("templates/views/info/");
		info.setSuffix(".html");
		info.setTemplateMode(TemplateMode.HTML);
		info.setCharacterEncoding("UTF-8");
		info.setOrder(2);
		info.setCacheable(false);
		info.setCheckExistence(true);
		
		return info;
	}

	@Bean
	public ClassLoaderTemplateResolver adminResolver() {
		ClassLoaderTemplateResolver admin = new ClassLoaderTemplateResolver();
		admin.setPrefix("templates/views/admin/");
		admin.setSuffix(".html");
		admin.setTemplateMode(TemplateMode.HTML);
		admin.setCharacterEncoding("UTF-8");
		admin.setOrder(2);
		admin.setCacheable(false);
		admin.setCheckExistence(true);
		
		return admin;
	}
	
	@Bean
	public ClassLoaderTemplateResolver shopResolver() {
		ClassLoaderTemplateResolver shop = new ClassLoaderTemplateResolver();
		shop.setPrefix("templates/views/shop/");
		shop.setSuffix(".html");
		shop.setTemplateMode(TemplateMode.HTML);
		shop.setCharacterEncoding("UTF-8");
		shop.setOrder(2);
		shop.setCacheable(false);
		shop.setCheckExistence(true);
		
		return shop;
	}
	

}
