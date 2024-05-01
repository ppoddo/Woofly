package com.kh.woofly.member.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.woofly.common.PageInfo;
import com.kh.woofly.member.model.dao.MemberDAO;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.member.model.vo.MemberAddress;
import com.kh.woofly.member.model.vo.Notification;
import com.kh.woofly.member.model.vo.Payment;
import com.kh.woofly.member.model.vo.Point;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberDAO mDAO;
	
	@Override
	public ArrayList<Member> getBlackList(String id) {
		return mDAO.getBlackList(id);
	}

	@Override
	public int removeBlock(HashMap<String, String> map) {
		return mDAO.removeBlock(map);
	}

	@Override
	public int editNickName(Member loginUser) {
		return mDAO.editNickName(loginUser);
	}

	@Override
	public int editName(Member loginUser) {
		return mDAO.editName(loginUser);
	}

	@Override
	public int editIntro(Member loginUser) {
		return mDAO.editIntro(loginUser);
	}

	@Override
	public int editIsPrivate(Member loginUser) {
		return mDAO.editIsPrivate(loginUser);
	}

	@Override
	public int editMbPhoto(Member loginUser) {
		return mDAO.editMbPhoto(loginUser);
	}

	@Override
	public int updatePwd(Member loginUser) { 
		return mDAO.updatePwd(loginUser);
	}

	@Override
	public int updateEmail(Member loginUser) {
		return mDAO.updateEmail(loginUser);
	}

	@Override
	public int updatePhone(Member loginUser) {
		return mDAO.updatePhone(loginUser);
	}

	@Override
	public int updateMbStatus(Member member) {
		return mDAO.updateMbStatus(member);
	}

	@Override
	public int addAddress(MemberAddress mAddress) {
		return mDAO.addAddress(mAddress);
	}

	@Override
	public ArrayList<MemberAddress> selectMyAddress(String id) {
		return mDAO.selectMyAddress(id);
	}

	@Override
	public int checkAddrType(String mbId) {
		return mDAO.checkAddrType(mbId);
	}

	@Override
	public int checkAddr(MemberAddress mAddress) {
		return mDAO.checkAddr(mAddress);
	}

	@Override
	public int updateAddr(MemberAddress mAddress) {
		return mDAO.updateAddr(mAddress);
	}

	@Override
	public int deleteAddr(String addrId) {
		return mDAO.deleteAddr(addrId);
	}

	@Override
	public int deleteMbPhoto(Member loginUser) {
		return mDAO.deleteMbPhoto(loginUser);
	}

	@Override
	public int nicknameCheck(String nickname) {
		return mDAO.nicknameCheck(nickname);
	}

	@Override
	public int addPayment(HashMap<String, Object> map) {
		return mDAO.addPayment(map);
	}

	@Override
	public ArrayList<Payment> selectMyPayment(String id) {
		return mDAO.selectMyPayment(id);
	}

	@Override
	public int updatePayment(Payment payment) {
		return mDAO.updatePayment(payment);
	}

	@Override
	public int updatePaymentToN(Payment payment) {
		return mDAO.updatePaymentToN(payment);
	}

	@Override
	public int deletePayment(int paymentNo) {
		return mDAO.deletePayment(paymentNo);
	}

	@Override
	public int deletePoints(String id) {
		return mDAO.deleteExpiredPoints(id);
	}

	@Override
	public int getPointsCount(String id) {
		return mDAO.getPointsCount(id);
	}

	@Override
	public ArrayList<Point> selectMyPoints(PageInfo pi, String id) {
		int offset = (pi.getCurrentPage() - 1) * pi.getPageLimit();
		int limit = pi.getPageLimit();
		RowBounds rowbounds = new RowBounds(offset, limit);
		return mDAO.selectMyPoints(rowbounds, id);
	}

	@Override
	public ArrayList<Notification> selectNotification(HashMap<String, String> map) {
		return mDAO.selectNotification(map);
	}

	@Override
	public int readNotification(String id) {
		return mDAO.readNotification(id);
	}

	@Override
	public int deleteNotification(String notiNo) {
		return mDAO.deleteNotification(notiNo);
	}

}
