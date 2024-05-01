package com.kh.woofly.contest.model.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import com.kh.woofly.common.PageInfo;
import com.kh.woofly.contest.model.vo.Contest;
import com.kh.woofly.contest.model.vo.ContestAttm;
import com.kh.woofly.contest.model.vo.ContestItem;
import com.kh.woofly.contest.model.vo.Participants;
import com.kh.woofly.pet.model.vo.Pet;
	
public interface ContestService {
	
	int contestOpen(Contest c);

	Contest contestNewList();

	ArrayList<Contest> contestList();

	int contestUpdate(Contest c);

	void updateContestStatus(LocalDate today);

	ArrayList<Contest> updateBestContest(LocalDate today);

	int contestEnroll(Participants p);

	Contest contestId(LocalDate today);

	ArrayList<ContestItem> itemList(String id);

	ArrayList<ContestItem> searchItem(String pSearch);

	int insertAttm(ArrayList<ContestAttm> list);

	String memberNick(String id);

	ArrayList<String> petList(String id);

	Participants thisParticipant(Integer id);

	String petName(Integer pId);

	Integer todayContestNo();

	ArrayList<Participants> participantstList(int cNo, PageInfo pi);

	ArrayList<ContestAttm> selectAttmNList();

	int getListCount(int cNo);

	Participants selectParticipants(int pNo, String id);

	ArrayList<ContestAttm> selectAttmPList(int pNum);

	int countList(Participants p);

	void contestPoint(Participants p);

	ArrayList<Participants> bestParticipantstList(int cNo, PageInfo pi);

	ArrayList<Participants> searchParticipantstList(Map<String, Object> map, PageInfo pi);
	
	ArrayList<String> cPetList(Map<String, Object> map);

	ArrayList<Integer> allContestNo();

	ArrayList<Participants> allTimeBestList(int generationNo, PageInfo pi);

	ArrayList<Participants> best3Dog(int generationNo);

	ArrayList<Participants> searchBestParticipantstList(Map<String, Object> map, PageInfo pi);

	int bestDogVote(Map<String, Object> map);

	int bestDogCountUpdate(int pNo);

	Integer voteCheck(Map<String, Object> map);

	Integer wOProductList(String aa);

	ContestAttm productAttm(Integer outO);

	ArrayList<Participants> topFiveBest(int cNo);

	ArrayList<Pet> selectDog(String mbId);

	


//	Date sDate();


	
	
}
