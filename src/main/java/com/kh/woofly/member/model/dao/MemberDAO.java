package com.kh.woofly.member.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.member.model.vo.MemberAddress;
import com.kh.woofly.member.model.vo.Notification;
import com.kh.woofly.member.model.vo.Payment;
import com.kh.woofly.member.model.vo.Point;

@Mapper
public interface MemberDAO {

	ArrayList<Member> getBlackList(String id);

	int removeBlock(HashMap<String, String> map);

	int editNickName(Member loginUser);

	int editName(Member loginUser);

	int editIntro(Member loginUser);

	int editIsPrivate(Member loginUser);

	int editMbPhoto(Member loginUser);

	int updatePwd(Member loginUser);

	int updateEmail(Member loginUser);

	int updatePhone(Member loginUser);

	int updateMbStatus(Member member);

	ArrayList<MemberAddress> selectMyAddress(String id);

	int addAddress(MemberAddress mAddress);

	int checkAddrType(String mbId);

	int checkAddr(MemberAddress mAddress);

	int updateAddr(MemberAddress mAddress);

	int deleteAddr(String addrId);

	int deleteMbPhoto(Member loginUser);

	int nicknameCheck(String nickname);

	int addPayment(HashMap<String, Object> map);

	ArrayList<Payment> selectMyPayment(String id);

	int updatePayment(Payment payment);

	int updatePaymentToN(Payment payment);

	int deletePayment(int paymentNo);

	int deleteExpiredPoints(String id);

	int getPointsCount(String id);

	ArrayList<Point> selectMyPoints(RowBounds rowbounds, String id);

	ArrayList<Notification> selectNotification(HashMap<String, String> map);

	int readNotification(String id);

	int deleteNotification(String notiNo);

}
