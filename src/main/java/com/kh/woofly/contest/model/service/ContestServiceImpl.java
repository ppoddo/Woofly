package com.kh.woofly.contest.model.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.woofly.common.PageInfo;
import com.kh.woofly.contest.model.dao.ContestDAO;
import com.kh.woofly.contest.model.vo.Contest;
import com.kh.woofly.contest.model.vo.ContestAttm;
import com.kh.woofly.contest.model.vo.ContestItem;
import com.kh.woofly.contest.model.vo.Participants;
import com.kh.woofly.info.model.vo.Notice;
import com.kh.woofly.pet.model.vo.Pet;

@Service
public class ContestServiceImpl implements ContestService {
	
	@Autowired
	private ContestDAO cDAO;
	
	@Override
	public Contest contestNewList() {
		
		return cDAO.contestNewList();
	}
	
	@Override
	public int contestOpen(Contest c) {
		
		return cDAO.contestOpen(c);
	}

	@Override
	public ArrayList<Contest> contestList() {
		
		return cDAO.contestList();
	}

	@Override
	public int contestUpdate(Contest c) {
		
		return cDAO.contestUpdate(c);
	}

	@Override
	public void updateContestStatus(LocalDate today) {
		
		cDAO.updateContestStatus(today);
	}

	@Override
	public ArrayList<Contest> updateBestContest(LocalDate today) {
		
		return cDAO.updateBestContest(today);
	}

	@Override
	public int contestEnroll(Participants p) {
		
		return cDAO.contestEnroll(p);
	}

	@Override
	public Contest contestId(LocalDate today) {
		
		return cDAO.contestId(today);
	}

	@Override
	public ArrayList<ContestItem> searchItem(String pSearch) {
		
		return cDAO.searchItem(pSearch);
	}

	@Override
	public ArrayList<ContestItem> itemList(String id) {
		
		return cDAO.itemList(id);
	}

	@Override
	public int insertAttm(ArrayList<ContestAttm> list) {
		
		return cDAO.insertAttm(list);
	}

	@Override
	public String memberNick(String id) {
		
		return cDAO.memberNick(id);
	}

	@Override
	public ArrayList<String> petList(String id) {
		
		return cDAO.petList(id);
	}

	@Override
	public Participants thisParticipant(Integer id) {
		 
		return cDAO.thisParticipant(id);
	}

	@Override
	public String petName(Integer pId) {
		 
		return cDAO.petName(pId);
	}

	@Override
	public Integer todayContestNo() {
		
		return cDAO.todayContestNo();
	}

	@Override
	public ArrayList<Participants> participantstList(int cNo, PageInfo pi) {
		
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return cDAO.participantstList(rowBounds, cNo);
	}

	@Override
	public ArrayList<ContestAttm> selectAttmNList() {
		
		return cDAO.selectAttmNList();
	}

	@Override
	public int getListCount(int cNo) {
		
		return cDAO.getListCount(cNo);
	}

	@Override
	public Participants selectParticipants(int pNo,  String id) {

		return cDAO.selectParticipants(pNo);
	}

	@Override
	public ArrayList<ContestAttm> selectAttmPList(int pNum) {
		
		return cDAO.selectAttmPList(pNum);
	}

	@Override
	public int countList(Participants p) {
		
		return cDAO.countList(p);
	}

	@Override
	public void contestPoint(Participants p) {
		
		cDAO.contestPoint(p);
		cDAO.contestPointList(p);
	}

	@Override
	public ArrayList<Participants> bestParticipantstList(int cNo, PageInfo pi) {
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return cDAO.bestParticipantstList(rowBounds, cNo);
	}

	@Override
	public ArrayList<Participants> searchParticipantstList(Map<String, Object> map, PageInfo pi) {
		if(pi != null) {
			int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
			int limit = pi.getBoardLimit();		
			RowBounds rowBounds = new RowBounds(offset, limit);
		}
		return cDAO.searchParticipantstList(map);
	}

	@Override
	public ArrayList<String> cPetList(Map<String, Object> map) {
		
		return cDAO.cPetList(map);
	}

	@Override
	public ArrayList<Integer> allContestNo() {
		
		return cDAO.allContestNo();
	}

	@Override
	public ArrayList<Participants> allTimeBestList(int generationNo, PageInfo pi) {
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return cDAO.allTimeBestList(rowBounds, generationNo);
	}

	@Override
	public ArrayList<Participants> best3Dog(int generationNo) {
		// TODO Auto-generated method stub
		return cDAO.best3Dog(generationNo);
	}

	@Override
	public ArrayList<Participants> searchBestParticipantstList(Map<String, Object> map, PageInfo pi) {
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return cDAO.searchBestParticipantstList(map);
	}

	@Override
	public int bestDogVote(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return cDAO.bestDogVote(map);
	}

	@Override
	public int bestDogCountUpdate(int pNo) {
		// TODO Auto-generated method stub
		return cDAO.bestDogCountUpdate(pNo);
	}

	@Override
	public Integer voteCheck(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return cDAO.voteCheck(map);
	}

	@Override
	public Integer wOProductList(String aa) {
		// TODO Auto-generated method stub
		return cDAO.wOProductList(aa);
	}

	@Override
	public ContestAttm productAttm(Integer outO) {
		// TODO Auto-generated method stub
		return cDAO.productAttm(outO);
	}

  @Override 
	public ArrayList<Participants> topFiveBest(int cNo) {
		return cDAO.topFiveBest(cNo);
	}

@Override
public ArrayList<Pet> selectDog(String mbId) {
	// TODO Auto-generated method stub
	return cDAO.selectDog(mbId);
}

	


//	@Override
//	public Date sDate() {
//		// TODO Auto-generated method stub
//		return cDAO.sDate();
//	}




	
	
	
}
