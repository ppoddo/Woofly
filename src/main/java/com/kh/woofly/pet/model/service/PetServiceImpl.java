package com.kh.woofly.pet.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.woofly.admin.model.vo.Report;
import com.kh.woofly.board.model.vo.Attachment;
import com.kh.woofly.common.PageInfo;
import com.kh.woofly.common.Reply;
import com.kh.woofly.common.ReplyLike;
import com.kh.woofly.contest.model.vo.Participants;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.pet.model.dao.PetDAO;
import com.kh.woofly.pet.model.vo.Album;
import com.kh.woofly.pet.model.vo.Diary;
import com.kh.woofly.pet.model.vo.Pet;

@Service
public class PetServiceImpl implements PetService{
	
	@Autowired
	private PetDAO pDAO;
	
	@Override
	public int petAdd(Pet p) {
		return pDAO.petAdd(p);
	}

	@Override
	public ArrayList<Pet> petInfoList(String id) {
		return pDAO.petInfoList(id);
	}

	@Override
	public Pet petDetail(int petId) {
		return pDAO.petDetail(petId);
	}


	@Override
	public int petEditName(HashMap<String, String> petInfo) {
		return pDAO.petEditName(petInfo);
	}

	@Override
	public int petEditIntro(HashMap<String, String> petInfo) {
		return pDAO.petEditIntro(petInfo);
	}

	@Override
	public int petEditBirth(HashMap<String, String> petInfo) {
		return pDAO.petEditBirth(petInfo);
	}

	@Override
	public int petEditBreed(HashMap<String, String> petInfo) {
		return pDAO.petEditBreed(petInfo);
	}

	@Override
	public int petEditWeight(HashMap<String, String> petInfo) {
		return pDAO.petEditWeight(petInfo);
	}

	@Override
	public int petEditGender(HashMap<String, String> petInfo) {
		return pDAO.petEditGender(petInfo);
	}

	@Override
	public int petEditMemo(HashMap<String, String> petInfo) {
		return pDAO.petEditMemo(petInfo);
	}

	@Override
	public int petDelete(int petId) {
		return pDAO.petDelete(petId);
	}

	@Override
	public int editPetPhoto(Pet p) {
		return pDAO.editPetPhoto(p);
	}

	@Override
	public int deletePetPhoto(int petId) {
		return pDAO.deletePetPhoto(petId);
	}

	@Override
	public int petDiaryWrite(Diary d) {
		return pDAO.petDiaryWrite(d);
	}

	@Override
	public Diary petDiaryDetail(int drNo) {
		return pDAO.petDiaryDetail(drNo);
	}

	@Override
	public int petDiaryEdit(Diary d) {
		return pDAO.petDiaryEdit(d);
	}

	@Override
	public int petDiaryDelete(int drNo) {
		return pDAO.petDiaryDelete(drNo);
	}

	@Override
	public int insertPetPhoto(Album a) {
		return pDAO.insertPetPhoto(a);
	}

	@Override
	public int insertPetAlbum(Attachment att) {
		return pDAO.insertPetAlbum(att);
	}

	@Override
	public ArrayList<Album> selectMyAlbum(String id) {
		return pDAO.selectMyAlbum(id);
	}

	@Override
	public ArrayList<Attachment> selectMyAlbumAttm(HashMap<String, Object> map) {
		return pDAO.selectMyAlbumAttm(map);
	}

	@Override
	public ArrayList<Album> selectMyAlbums(HashMap<String, Object> map) {
		return pDAO.selectMyAlbums(map);
	}

	@Override
	public ArrayList<Album> petPhotoDetail(int abNo) {
		return pDAO.petPhotoDetail(abNo);
	}

	@Override
	public ArrayList<Pet> petInfo(int abNo) {
		return pDAO.petInfo(abNo);
	}

	@Override
	public Album petAlbumDetail(int abNo) {
		return pDAO.petAlbumDetail(abNo);
	}

	@Override
	public ArrayList<Attachment> petAttmList(int abNo) {
		return pDAO.petAttmList(abNo);
	}

	@Override
	public int petPhotoDelete(int abNo) {
		return pDAO.petPhotoDelete(abNo);
	}

	@Override
	public int insertAlbumReply(Reply r) {
		return pDAO.insertAlbumReply(r);
	}

	@Override
	public ArrayList<Reply> replyList(int abNo) {
		return pDAO.replyList(abNo);
	}

	@Override
	public int updateReply(Reply r) {
		return pDAO.updateReply(r);
	}

	@Override
	public int deleteReply(Reply r) {
		return pDAO.deleteReply(r);
	}

	@Override
	public int insertReport(Report rt) {
		return pDAO.insertReport(rt);
	}

	@Override
	public int checkResult(Report rt) {
		return pDAO.checkResult(rt);
	}

	@Override
	public int getDiaryCount(HashMap<String, Object> map) {
		return pDAO.getDiaryCount(map);
	}

	@Override
	public ArrayList<Diary> petDiaryList(PageInfo pi, HashMap<String, Object> map) {
		int offset = (pi.getCurrentPage() - 1) * pi.getPageLimit();
		RowBounds rowbounds = new RowBounds(offset, pi.getPageLimit());
		return pDAO.petDiaryList(rowbounds, map);
	}

	@Override
	public ReplyLike selectReplyLike(Reply r) {
		return pDAO.selectReplyLike(r);
	}

	@Override
	public int deleteReplyLike(HashMap<String, Object> map) {
		return pDAO.deleteReplyLike(map);
	}

	@Override
	public int insertReplyLike(HashMap<String, Object> map) {
		return pDAO.insertReplyLike(map);
	}

	@Override
	public ArrayList<Participants> petContestList(HashMap<String, Object> map) {
		return pDAO.petContestList(map);
	}

	@Override
	public int albumLike(HashMap<String, Object> map) {
		return pDAO.albumLike(map);
	}

	@Override
	public int deleteBoardLike(HashMap<String, Object> map) {
		return pDAO.deleteBoardLike(map);
	}

	@Override
	public int insertBoardLike(HashMap<String, Object> map) {
		return pDAO.insertBoardLike(map);
	}

	@Override
	public ArrayList<Reply> repliesList(String id) {
		return pDAO.repiesList(id);
	}

	@Override
	public int deleteImage(Attachment a) {
		return pDAO.deleteImage(a);
	}

	@Override
	public int updatePetPhoto(Album a) {
		return pDAO.updatePetPhoto(a);
	}

	@Override
	public int getListCount(int i) {
		return pDAO.getListCount(i);
	}

	@Override
	public int insertReplyNotice(HashMap<String, Object> map) {
		return pDAO.insertReplyNotice(map);
	}

	@Override
	public int insertBoardNotice(HashMap<String, Object> map) {
		return pDAO.insertBoardNotice(map);
	}

	@Override
	public Member selectMember(String mbId) {
		return pDAO.selectMember(mbId);
	}


}
