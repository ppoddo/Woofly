package com.kh.woofly.shop.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.woofly.cart.model.vo.Cart;
import com.kh.woofly.common.PageInfo;
import com.kh.woofly.common.Reply;
import com.kh.woofly.common.ReplyLike;
import com.kh.woofly.order.model.vo.OrderDetail;
import com.kh.woofly.shop.model.dao.ShopDAO;
import com.kh.woofly.shop.model.vo.Product;
import com.kh.woofly.shop.model.vo.ProductAttm;
import com.kh.woofly.shop.model.vo.ProductCategory;

@Service
public class ShopServiceImpl implements ShopService {
	
	@Autowired
	private ShopDAO sDAO;

	@Override
	public int insertProduct(Product p) {
		return sDAO.insertProduct(p);
	}

	@Override
	public ArrayList<ProductCategory> selectCategory(Integer i) {
		return sDAO.selectCategory(i);
	}

	@Override
	public int insertAttm(ArrayList<ProductAttm> list) {
		return sDAO.insertAttm(list);
	}

	@Override
	public ArrayList<Product> selectProducts(PageInfo pi, HashMap<String, Object> sortMap) {
		int offset = (pi.getCurrentPage() -1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();
		
		RowBounds rowBounds = new RowBounds(offset, limit);
		return sDAO.selectProducts(rowBounds, sortMap);
	}

	@Override
	public ArrayList<ProductAttm> selectProductAttm(Integer productId) {
		return sDAO.selectProductAttm(productId);
	}

	@Override
	public int getProductCount(String string) {
		return sDAO.getProductCount(string);
	}

	@Override
	public int getDetailCount(int cNo) {
		return sDAO.getDetailCount(cNo);
	}

	@Override
	public ArrayList<ProductCategory> selectedCategory(Integer cNo) {
		return sDAO.selectedCategory(cNo);
	}

	@Override
	public Product selectDetailProduct(int productId) {
		return sDAO.selectDetailProduct(productId);
	}

	@Override
	public ProductCategory selectDetailCategory(int productDetailNo) {
		return sDAO.selectDetailCategory(productDetailNo);
	}

	@Override
	public int insertReply(Reply r) {
		return sDAO.insertReply(r);
	}

	@Override
	public ArrayList<Reply> selectReply(Reply r) {
		return sDAO.selectReply(r);
	}

	@Override
	public int insertCart(Cart selectC) {
		return sDAO.insertCart(selectC);
	}

	@Override
	public ArrayList<Cart> selectUserCart(String mbId) {
		return sDAO.selectUserCart(mbId);
	}

	@Override
	public int updateCartQuantity(Cart c) {
		return sDAO.updateCartQuantity(c);
	}

	@Override
	public int deleteAttm(ArrayList<String> delRename) {
		return sDAO.deleteAttm(delRename);
	}

	@Override
	public int updateProduct(Product p) {
		return sDAO.updateProduct(p);
	}

	@Override
	public int updateStock(Product p) {
		return sDAO.updateStock(p);
	}

	@Override
	public int deleteProduct(int pId) {
		return sDAO.deleteProduct(pId);
	}

	@Override
	public int attmStatusYN(int pId) {
		return sDAO.attmStatusYN(pId);
	}

	@Override
	public int insertReplyCount(HashMap<String, Object> map) {
		
		int result = sDAO.insertReplyCount(map);
		
		if(result > 0) {
			int result2 = sDAO.updateReplyCount(map);
			if(result2 > 0) {
				return result2;
			}
		}
		
		return result;
	}

	@Override
	public int downReplyCount(HashMap<String, Object> map) {
		
		int result = sDAO.downReplyCount(map);
		
		if(result > 0) {
			int result2 = sDAO.downCountReplyCount(map);
			if(result2 > 0) {
				return result2;
			}
		}
		
		return result;
		
	}

	@Override
	public ArrayList<ReplyLike> selectReplyLike(ArrayList<Integer> rNos) {
		return sDAO.selectReplyLike(rNos);
	}

	@Override
	public int updateReply(Reply r) {
		return sDAO.updateReply(r);
	}

	@Override
	public int deleteReply(int rNo) {
		return sDAO.deleteReply(rNo);
	}

	@Override
	public int insertStampProduct(HashMap<String, Object> stamp) {
		return sDAO.insertStampProduct(stamp);
	}

	@Override
	public int selectMyStampProduct(HashMap<String, Object> stampParam) {
		return sDAO.selectMyStampProduct(stampParam);
	}

	@Override
	public int selectSavedProduct(int productId) {
		return sDAO.selectSavedProduct(productId);
	}

	@Override
	public int deleteStampProduct(HashMap<String, Object> stamp) {
		return sDAO.deleteStampProduct(stamp);
	}

	@Override
	public int insertNotify(HashMap<String, Object> notifyMap) {
		return sDAO.insertNotify(notifyMap);
	}

	@Override
	public ArrayList<Product> recentlyProductFive() {
		return sDAO.recentlyProductFive();
	}

	@Override
	public ArrayList<Product> popularityProductFive() {
		return sDAO.popularityProductFive();
	}

	@Override
	public int selectMyOrders(HashMap<String, Object> orderConfirm) {
		return sDAO.selectMyOrder(orderConfirm);
	}

}
