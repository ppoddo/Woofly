package com.kh.woofly.contest.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.kh.woofly.contest.model.service.ContestService;

@EnableScheduling
@SpringBootApplication
public class SchedulerApplication{
	
	@Autowired
	private ContestService cService;
	
	@Scheduled(cron = "0 0 * * * *") //> 매일 자정에 실행   // @Scheduled(cron = "0 * * * * *")  < 매일 분마다
    public void updateDate() {
		
		LocalDate today = LocalDate.now(); // 오늘 날짜 가져오기
		
		System.out.println(today);
		
	 	cService.updateContestStatus(today);
    }
	
	
	
	
	
}
