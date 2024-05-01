package com.kh.woofly.contest.model.dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.woofly.contest.model.vo.Contest;
import com.kh.woofly.contest.model.vo.ContestAttm;
import com.kh.woofly.contest.model.vo.ContestItem;
import com.kh.woofly.contest.model.vo.Participants;
import com.kh.woofly.pet.model.vo.Pet;

@Mapper
public interface ContestDAO {
	
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

	ArrayList<ContestItem> itemList(String id, RowBounds rowBounds);

	int insertAttm(ArrayList<ContestAttm> list);

	String memberNick(String id);

	ArrayList<String> petList(String id);

	Participants thisParticipant(Integer id);

	String petName(Integer pId);

	Integer todayContestNo();

	ArrayList<Participants> participantstList(RowBounds rowBounds, int cNo);

	ArrayList<ContestAttm> selectAttmNList();

	int getListCount(int cNo);

	Participants selectParticipants(int pNo);

	int updateCount(String id);

	ArrayList<ContestAttm> selectAttmPList(int pNum);

	int countList(Participants p);

	void contestPoint(Participants p);

	void contestPointList(Participants p);

	ArrayList<Participants> bestParticipantstList(RowBounds rowBounds, int cNo);
	
	ArrayList<String> cPetList(String id, int cNo);

	ArrayList<String> cPetList(Map<String, Object> map);

	ArrayList<Integer> allContestNo();

	ArrayList<Participants> allTimeBestList(RowBounds rowBounds, int generationNo);

	ArrayList<Participants> best3Dog(int generationNo);

	ArrayList<Participants> searchParticipantstList(Map<String, Object> map);

	ArrayList<Participants> searchBestParticipantstList(Map<String, Object> map);

	int bestDogVote(Map<String, Object> map);

	int bestDogCountUpdate(int pNo);

	Integer  voteCheck(Map<String, Object> map);

	Integer wOProductList(String aa);

	ContestAttm productAttm(Integer outO);


	ArrayList<Participants> topFiveBest(int cNo);

	ArrayList<Pet> selectDog(String mbId);

//	Date sDate();




	
	
}
