package com.kh.woofly.cart.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.kh.woofly.cart.model.vo.Cart;
import com.kh.woofly.member.model.vo.MemberAddress;
import com.kh.woofly.member.model.vo.Payment;
import com.kh.woofly.order.model.vo.Order;
import com.kh.woofly.order.model.vo.OrderDetail;
import com.kh.woofly.shop.model.vo.ProductAttm;

public interface CartService {

	ArrayList<Cart> selectMyCart(String id);

	ProductAttm selectCartAttm(Cart c);

	int updateMyCart(HashMap<String, Object> map);

	Cart selectCart(int cartId);

	ArrayList<MemberAddress> selectDefaultAddr(String id);

	ArrayList<Payment> selectPayment(String id);

	int insertOrder(Order o);

	ArrayList<Cart> selectAllCart(String id);

	int addPoints(HashMap<String, Object> map);

	MemberAddress selectAddr(int addrId);

	int insertOrderDetail(OrderDetail od);

	int deleteCart(Cart c);

	int usePoints(HashMap<String, Object> map);

	int deleteAllCart(String id);

}
