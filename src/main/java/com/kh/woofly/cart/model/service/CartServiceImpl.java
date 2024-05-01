package com.kh.woofly.cart.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.woofly.cart.model.dao.CartDAO;
import com.kh.woofly.cart.model.vo.Cart;
import com.kh.woofly.member.model.vo.MemberAddress;
import com.kh.woofly.member.model.vo.Payment;
import com.kh.woofly.order.model.vo.Order;
import com.kh.woofly.order.model.vo.OrderDetail;
import com.kh.woofly.shop.model.vo.ProductAttm;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartDAO cDAO;

	@Override
	public ArrayList<Cart> selectMyCart(String id) {
		return cDAO.selectMyCart(id);
	}

	@Override
	public ProductAttm selectCartAttm(Cart c) {
		return cDAO.selectCartAttm(c);
	}

	@Override
	public int updateMyCart(HashMap<String, Object> map) {
		return cDAO.updateMyCart(map);
	}

	@Override
	public Cart selectCart(int cartId) {
		return cDAO.selectCart(cartId);
	}

	@Override
	public ArrayList<MemberAddress> selectDefaultAddr(String id) {
		return cDAO.selectDefaultAddr(id);
	}

	@Override
	public ArrayList<Payment> selectPayment(String id) {
		return cDAO.selectPayment(id);
	}

	@Override
	public int insertOrder(Order o) {
		return cDAO.insertOrder(o);
	}

	@Override
	public ArrayList<Cart> selectAllCart(String id) {
		return cDAO.selectAllCart(id);
	}

	@Override
	public int addPoints(HashMap<String, Object> map) {
		return cDAO.addPoints(map);
	}

	@Override
	public MemberAddress selectAddr(int addrId) {
		return cDAO.selectAddr(addrId);
	}

	@Override
	public int insertOrderDetail(OrderDetail od) {
		return cDAO.insertOrderDetail(od);
	}

	@Override
	public int deleteCart(Cart c) {
		return cDAO.deleteCart(c);
	}

	@Override
	public int usePoints(HashMap<String, Object> map) {
		return cDAO.usePoints(map);
	}

	@Override
	public int deleteAllCart(String id) {
		return cDAO.deleteAllCart(id);
	}

}
