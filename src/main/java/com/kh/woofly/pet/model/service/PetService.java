package com.kh.woofly.pet.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.kh.woofly.admin.model.vo.Report;
import com.kh.woofly.board.model.vo.Attachment;
import com.kh.woofly.common.PageInfo;
import com.kh.woofly.common.Reply;
import com.kh.woofly.common.ReplyLike;
import com.kh.woofly.contest.model.vo.Participants;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.pet.model.vo.Album;
import com.kh.woofly.pet.model.vo.Diary;
import com.kh.woofly.pet.model.vo.Pet;

public interface PetService {

	int petAdd(Pet p);

	ArrayList<Pet> petInfoList(String id);

	Pet petDetail(int petId);

	int petEditName(HashMap<String, String> petInfo);

	int petEditIntro(HashMap<String, String> petInfo);

	int petEditBirth(HashMap<String, String> petInfo);

	int petEditBreed(HashMap<String, String> petInfo);

	int petEditWeight(HashMap<String, String> petInfo);

	int petEditGender(HashMap<String, String> petInfo);

	int petEditMemo(HashMap<String, String> petInfo);

	int petDelete(int petId);

	int editPetPhoto(Pet p);

	int deletePetPhoto(int petId);

	int petDiaryWrite(Diary d);

	Diary petDiaryDetail(int drNo);

	int petDiaryEdit(Diary d);

	int petDiaryDelete(int drNo);

	int insertPetPhoto(Album a);

	int insertPetAlbum(Attachment att);

	ArrayList<Album> selectMyAlbum(String id);

	ArrayList<Attachment> selectMyAlbumAttm(HashMap<String, Object> map);

	ArrayList<Album> selectMyAlbums(HashMap<String, Object> map);

	ArrayList<Album> petPhotoDetail(int abNo);

	ArrayList<Pet> petInfo(int abNo);

	Album petAlbumDetail(int abNo);

	ArrayList<Attachment> petAttmList(int abNo);

	int petPhotoDelete(int abNo);

	int insertAlbumReply(Reply r);

	ArrayList<Reply> replyList(int abNo);

	int updateReply(Reply r);

	int deleteReply(Reply r);

	int insertReport(Report rt);

	int checkResult(Report rt);

	int getDiaryCount(HashMap<String, Object> map);

	ArrayList<Diary> petDiaryList(PageInfo pi, HashMap<String, Object> map);

	ReplyLike selectReplyLike(Reply r);

	int deleteReplyLike(HashMap<String, Object> map);

	int insertReplyLike(HashMap<String, Object> map);

	ArrayList<Participants> petContestList(HashMap<String, Object> map);

	int albumLike(HashMap<String, Object> map);

	int deleteBoardLike(HashMap<String, Object> map);

	int insertBoardLike(HashMap<String, Object> map);

	ArrayList<Reply> repliesList(String id);

	int deleteImage(Attachment a);

	int updatePetPhoto(Album a);

	int getListCount(int i);

	int insertReplyNotice(HashMap<String, Object> map);

	int insertBoardNotice(HashMap<String, Object> map);

	Member selectMember(String mbId);

	//int getPhotoCount(HashMap<String, String> map);

}
