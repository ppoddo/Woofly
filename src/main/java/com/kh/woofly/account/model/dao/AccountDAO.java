package com.kh.woofly.account.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.member.model.vo.MemberAddress;

@Mapper
public interface AccountDAO {
	public Member login(Member m);

	public int idCheck(String mbId);

	public int nickCheck(String mbNickName);

	public int signUp(Member m);

	public int signUp(MemberAddress ma);

	public int signUpMember(Member m);

	public int signUpMemberAddr(MemberAddress ma);

	public int checkNameEmail(Member m);

	public ArrayList<Member> sendId(Member info);

	public int checkIdEmail(Member m);

	public int updatePwd(Member m);
	
}
