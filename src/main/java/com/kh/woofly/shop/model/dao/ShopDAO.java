package com.kh.woofly.shop.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.woofly.cart.model.vo.Cart;
import com.kh.woofly.common.Reply;
import com.kh.woofly.common.ReplyLike;
import com.kh.woofly.order.model.vo.OrderDetail;
import com.kh.woofly.shop.model.vo.Product;
import com.kh.woofly.shop.model.vo.ProductAttm;
import com.kh.woofly.shop.model.vo.ProductCategory;

@Mapper
public interface ShopDAO {

	int insertProduct(Product p);

	ArrayList<ProductCategory> selectCategory(Integer i);

	int insertAttm(ArrayList<ProductAttm> list);

	ArrayList<Product> selectProducts(RowBounds rowBounds, HashMap<String, Object> sortMap);

	ArrayList<ProductAttm> selectProductAttm(Integer productId);

	int getProductCount(String string);

	int getDetailCount(int cNumber);

	ArrayList<ProductCategory> selectedCategory(Integer cNo);

	Product selectDetailProduct(int productId);

	ProductCategory selectDetailCategory(int productDetailNo);

	int insertReply(Reply r);

	ArrayList<Reply> selectReply(Reply r);

	int insertCart(Cart selectC);

	ArrayList<Cart> selectUserCart(String mbId);

	int updateCartQuantity(Cart c);

	int deleteAttm(ArrayList<String> delRename);

	int updateProduct(Product p);

	int updateStock(Product p);

	int deleteProduct(int pId);

	int attmStatusYN(int pId);

	int insertReplyCount(HashMap<String, Object> map);

	int updateReplyCount(HashMap<String, Object> map);

	int downReplyCount(HashMap<String, Object> map);

	int downCountReplyCount(HashMap<String, Object> map);

	ArrayList<ReplyLike> selectReplyLike(ArrayList<Integer> rNos);

	int updateReply(Reply r);

	int deleteReply(int rNo);

	int insertStampProduct(HashMap<String, Object> stamp);

	int selectMyStampProduct(HashMap<String, Object> stampParam);

	int selectSavedProduct(int productId);

	int deleteStampProduct(HashMap<String, Object> stamp);

	int insertNotify(HashMap<String, Object> notifyMap);

	ArrayList<Product> recentlyProductFive();

	ArrayList<Product> popularityProductFive();

	int selectMyOrder(HashMap<String, Object> orderConfirm);

}
