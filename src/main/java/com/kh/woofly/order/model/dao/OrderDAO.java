package com.kh.woofly.order.model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.woofly.board.model.vo.UsedBoard;
import com.kh.woofly.common.PageInfo;
import com.kh.woofly.member.model.vo.Point;
import com.kh.woofly.order.model.vo.Order;
import com.kh.woofly.order.model.vo.OrderDetail;
import com.kh.woofly.order.model.vo.Saved;
import com.kh.woofly.shop.model.vo.Product;
import com.kh.woofly.shop.model.vo.ProductAttm;

@Mapper
public interface OrderDAO {

	ArrayList<Order> selectMyBuying(RowBounds rowbounds, HashMap<String, Object> map);

	Order selectOrder(int orderNo);

	ArrayList<OrderDetail> selectOrderDetails(int orderNo);

	ProductAttm selectOrderProductsAttm(OrderDetail detail);

	ProductAttm selectOrderAttm(Order order);

	Product selectMostExpensive(Order order);

	int getBuyingCount(HashMap<String, Object> id);

	int deletePoints(String id);

	ArrayList<Point> selectMyPoints(String id);

	ArrayList<Saved> selectMySaved(HashMap<String, Object> map);

	ArrayList<UsedBoard> selectMySelling(HashMap<String, Object> map);

	ArrayList<Saved> selectMySavedHome(HashMap<String, Object> map);

}
