package com.kh.woofly.member.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.kh.woofly.common.PageInfo;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.member.model.vo.MemberAddress;
import com.kh.woofly.member.model.vo.Notification;
import com.kh.woofly.member.model.vo.Payment;
import com.kh.woofly.member.model.vo.Point;

public interface MemberService {

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

	int addAddress(MemberAddress mAddress);

	ArrayList<MemberAddress> selectMyAddress(String id);

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

	int deletePoints(String id);

	int getPointsCount(String id);

	ArrayList<Point> selectMyPoints(PageInfo pi, String id);

	ArrayList<Notification> selectNotification(HashMap<String, String> map);

	int readNotification(String id);

	int deleteNotification(String notiNo);

}
