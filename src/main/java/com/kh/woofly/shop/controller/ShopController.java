package com.kh.woofly.shop.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.kh.woofly.cart.model.vo.Cart;
import com.kh.woofly.common.PageInfo;
import com.kh.woofly.common.Pagination;
import com.kh.woofly.common.Reply;
import com.kh.woofly.common.ReplyLike;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.order.model.vo.OrderDetail;
import com.kh.woofly.shop.model.exception.ShopException;
import com.kh.woofly.shop.model.service.ShopService;
import com.kh.woofly.shop.model.vo.Product;
import com.kh.woofly.shop.model.vo.ProductAttm;
import com.kh.woofly.shop.model.vo.ProductCategory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ShopController {
	
	@Autowired
	private ShopService sService;

	@GetMapping("/shopMain")
	public String movoToShopMain(Model model, @RequestParam(value = "page", defaultValue="1")int page,
								@RequestParam(value="sort", required=false) String sort,
								@RequestParam(value="direction", required=false) String direction,
								HttpServletRequest request) {
		// 쇼핑 상품리스트 전체 가져와서 뿌리기
		// 상품리스트, 상품에 따른 썸네일 가져오고, 상품의 카테고리 가져오고 // 페이징처리(무한스크롤)
		// 가져올 페이지, 한 페이지에 표현될 상품 개수			// 나중에 무한스크롤 완성시키자...
		HashMap<String, Object> sortMap = new HashMap<>();
		sortMap.put("sort", sort);			// 검색 기준
		sortMap.put("direction", direction); // 정렬 기준
		sortMap.put("cNo", null);
		int listCount = sService.getProductCount("P");
		PageInfo pi = Pagination.getPageInfo(page, listCount, 12);
		
		
		ArrayList<Product> pList = sService.selectProducts(pi, sortMap);
		
		// topCategory용 - 선택된 카테고리! 자체를 선별 null 보내면 전체 카테고리 1개씩
		ArrayList<ProductCategory> sList = sService.selectedCategory(null);
		
		ArrayList<ProductCategory> cList = sService.selectCategory(9999);
		// 대분류 카테고리 리스트
		ArrayList<ProductCategory> bList = sService.selectCategory(null);
		// 썸네일의 첫번째만 가져오기....
		ArrayList<ProductAttm> aList = sService.selectProductAttm(null);
		
		model.addAttribute("pi", pi);
		model.addAttribute("pList", pList);
		model.addAttribute("cList", cList);
		model.addAttribute("aList", aList);
		model.addAttribute("bList", bList);
		model.addAttribute("sList", sList);
		model.addAttribute("loc", request.getRequestURI());
		model.addAttribute("sort", sort);
		model.addAttribute("direction", direction);
		
		
		return "shopMain";
	}
	
	@GetMapping("/shop/shopWrite")
	public String shopWrite(Model model) {
		// 상세카테고리의 전체 리스트
		ArrayList<ProductCategory> cList = sService.selectCategory(9999);
		
		// 대분류 카테고리 리스트
		ArrayList<ProductCategory> bList = sService.selectCategory(null);
		model.addAttribute("cList", cList);
		model.addAttribute("bList", bList);
		
		return "shopWrite";
	}
	
	@PostMapping("/shop/insertProduct")
	public String insertProduct(@ModelAttribute Product p,
								@RequestParam("thumbnailFile") ArrayList<MultipartFile> thumbFiles,
								@RequestParam("contentFile") ArrayList<MultipartFile> contentFiles) {
		if(p.getColor() == null) {
			p.setColor("N");
		}
		
		int result1 = sService.insertProduct(p);
		
		// 썸네일 리네임 과정
		ArrayList<ProductAttm> list = new ArrayList<ProductAttm>();
		for(int i = 0; i < thumbFiles.size(); i++) {
			MultipartFile upload = thumbFiles.get(i);
			if(!upload.getOriginalFilename().equals("")) {
				String[] returnArr = saveFile(upload);
				if(returnArr[1] != null) {
					ProductAttm t = new ProductAttm();
					t.setOriginalName(upload.getOriginalFilename());
					t.setRenameName(returnArr[1]);
					t.setAttmPath(returnArr[0]);
					t.setAttmRefNo(p.getProductId());
					t.setAttmLevel(1);
					
					list.add(t);
				}
			}
		}
		
		// 내용 이미지 리네임 과정
		for(int i = 0; i < contentFiles.size(); i++) {
			MultipartFile upload = contentFiles.get(i);
			if(!upload.getOriginalFilename().equals("")) {
				String[] returnArr = saveFile(upload);
				if(returnArr[1] != null) {
					ProductAttm c = new ProductAttm();
					c.setOriginalName(upload.getOriginalFilename());
					c.setRenameName(returnArr[1]);
					c.setAttmPath(returnArr[0]);
					c.setAttmRefNo(p.getProductId());
					
					// 썸네일(제목이미지)는 레벨이 1
					
					c.setAttmLevel(2);
					
					list.add(c);
				}
			}
		}
		
		int result2 = sService.insertAttm(list);
		
		if(result1 + result2 == list.size() + 2) {
			return "redirect:/shopMain";
		} else {
			for(ProductAttm a : list) {
				deleteFile(a.getRenameName());
			}
			throw new ShopException("상품 등록을 실패하였습니다.");
		}
	}
	
	private String[] saveFile(MultipartFile upload) {
		String os = System.getProperty("os.name").toLowerCase();
		String savePath = null;
		if (os.contains("win")) {
			savePath = "C:\\woofly\\shopFiles";
		} else if (os.contains("mac")) {
			savePath = "/Users/younjun/Desktop/WorkStation/uploadFiles/woofly";
		}
		File folder = new File(savePath);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		
		Date time = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		int ranNum = (int)(Math.random()*100000);
		
		String originFileName = upload.getOriginalFilename();
		String renameFileName = sdf.format(time) + ranNum + originFileName.substring(originFileName.lastIndexOf("."));
		
		String renamePath = folder + "\\" + renameFileName;
		
		try {
			upload.transferTo(new File(renamePath));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] returnArr = new String[2];
		returnArr[0] = savePath;
		returnArr[1] = renameFileName;
		
		return returnArr;
	}
	
	
	private void deleteFile(String renameName) {
		String root = "C:\\woofly\\";
		String savePath = root + "\\shopFiles";
		
		File f = new File(savePath + "\\" + renameName);
		if(f.exists()) {
			f.delete();
		}
	}
	
	@GetMapping("/shop/selectDetail")
	public String selectDetailCategory(@RequestParam("productDetailNo") int cNo,
										@RequestParam(value="page", defaultValue="1") int page,
										@RequestParam(value="sort", required=false) String sort,
										@RequestParam(value="direction", required=false) String direction,
										HttpServletRequest request,
										Model model) {
		
		// 받아 온 카테고리에 해당하는 상품으로 교체
		HashMap<String, Object> sortMap = new HashMap<>();
		sortMap.put("sort", sort);			// 검색 기준
		sortMap.put("direction", direction); // 정렬 기준
		sortMap.put("cNo", cNo);

		int listCount = sService.getDetailCount(cNo);
		PageInfo pi = Pagination.getPageInfo(page, listCount, 12);
		ArrayList<Product> pList = sService.selectProducts(pi, sortMap);
		
		// topCategory용 - 선택된 카테고리! 자체를 선별 null 보내면 전체 카테고리 1개씩
		ArrayList<ProductCategory> sList = sService.selectedCategory(cNo);
		
		ArrayList<ProductCategory> cList = sService.selectCategory(9999);
		// 대분류 카테고리 리스트 - 근데 이건 나중에 nav바 따로 빠지면 그쪽으로 보내놔도 될듯?
		ArrayList<ProductCategory> bList = sService.selectCategory(null);
		// 썸네일의 첫번째만 가져오기....
		ArrayList<ProductAttm> aList = sService.selectProductAttm(null);
		
		if(pList != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("pList", pList);
			model.addAttribute("cList", cList);
			model.addAttribute("aList", aList);
			model.addAttribute("bList", bList);
			model.addAttribute("sList", sList);
			model.addAttribute("loc", request.getRequestURI());
			model.addAttribute("isDetail", cNo);
			model.addAttribute("sort", sort);
			model.addAttribute("direction", direction);
			
			return "shopMain";
		} else {
			throw new ShopException("카테고리 선택에 실패하였습니다.");
		}
	}
	
	@GetMapping("/shop/productDetail")
	public String selectDetailProduct(@RequestParam("pId") int productId,
										@RequestParam("page") int page,
										Model model, Reply r,
										HttpSession session) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		String memberId = null;
		
		Product p = sService.selectDetailProduct(productId);
		ArrayList<ProductAttm> aList = sService.selectProductAttm(productId);
		ProductCategory c = sService.selectDetailCategory(p.getProductDetailNo());
		r.setBType("P");
		r.setBNo(productId);
		ArrayList<Reply> rList = sService.selectReply(r);
		
		int savedCount = sService.selectSavedProduct(productId);
		
		if(loginUser != null) {
			memberId = loginUser.getMbId();
		}
		
		HashMap<String, Object> stampParam = new HashMap<>();
		stampParam.put("memberId", memberId);
		stampParam.put("productId", productId);
		int stampCount = sService.selectMyStampProduct(stampParam);
		
		ArrayList<Integer> rNos = new ArrayList<>();
		ArrayList<ReplyLike> likes = null;
		if(!rList.isEmpty()) {
			for(int i = 0; i < rList.size(); i++) {
				rNos.add(rList.get(i).getRNo());
			}
			likes = sService.selectReplyLike(rNos);
			
			if(loginUser != null) {
				for(int i = 0; i < rList.size(); i++) {
					for(int j = 0; j < likes.size(); j ++) {
						
						if((rList.get(i).getRNo() == likes.get(j).getLikeRefBoard()) && likes.get(j).getLikeUser().equals(loginUser.getMbId())) {
							rList.get(i).setUserLiked(true);
						}
					}
	 			}
			}
		}
		
		String[] colors = null;
		if(!p.getColor().equals("N")) {
			colors = p.getColor().split(",");
		}
		
		ArrayList<ProductAttm> tList = new ArrayList<>();
		ArrayList<ProductAttm> conList = new ArrayList<>();
		
		for(ProductAttm a : aList) {
			if(a.getAttmLevel() == 1) {
				tList.add(a);
			} else {
				conList.add(a);
			}
		}
		
		int oListCount = 0;
		String isBought = "N";

		if(loginUser != null) {
			HashMap<String, Object> orderConfirm = new HashMap<>();
			orderConfirm.put("mbId", loginUser.getMbId());
			orderConfirm.put("pId", productId);
			oListCount = sService.selectMyOrders(orderConfirm);
			if(oListCount > 0) {
				isBought = "Y";
			}
		}
		model.addAttribute("isBought", isBought);
		model.addAttribute("page", page);
		model.addAttribute("p", p);
		model.addAttribute("tList", tList);
		model.addAttribute("conList", conList);
		model.addAttribute("aList", aList);
		model.addAttribute("rList", rList);
		model.addAttribute("c", c);
		model.addAttribute("colors", colors);
		model.addAttribute("likes", likes);
		model.addAttribute("stampCount", stampCount);
		model.addAttribute("savedCount", savedCount);
		
		return "shopDetail";
	}
	
	@GetMapping("/shop/payment")
	public void myCartUpdate(@RequestParam(value="productId") int productId,
								@RequestParam(value="pSize", required=false) String pSize,
								@RequestParam(value="colors", required=false) String color,
								@RequestParam(value="quantity") int quantity,
								@RequestParam("isMove") String isMove,
								HttpSession session,
								HttpServletResponse response) {
		String mbId = ((Member)session.getAttribute("loginUser")).getMbId();
		//사용자의 카트를 가져와서 같은 상품의 같은 옵션이 완전무결하면 수량만 업데이트
		// 하나라도 다르면 insert
		
		ArrayList<Cart> cartList = sService.selectUserCart(mbId);
		// 상품번호 + 옵션 2가지 가 같으면 같은 상품의 같은 조건 => 원래 있던 카트의 수량만 업데이트
		
		boolean isEmpty = true;
		int result1 = 0;
		int result2 = 0;
		
		// 이거 발동 안됨
		
		Cart selectC = new Cart();
		
		for(Cart c : cartList) {
			if(c.getProductId() == productId && c.getColor().equals(color) && c.getPSize().equals(pSize)) {
				c.setQuantity(c.getQuantity() + quantity);
				result1 = sService.updateCartQuantity(c);
				isEmpty = false;
				selectC.setCartId(c.getCartId());
				break;
			}
		}
		
		// true면 같은 항목이 비어있음을 나타냄 //productName
		if(isEmpty) {
			Properties prop = new Properties();
			
			if(pSize == null) {
				pSize = "N";
			}
			if(color == null) {
				color = "N";
			}
			
			selectC.setMbId(mbId);
			selectC.setPSize(pSize);
			selectC.setColor(color);
			selectC.setQuantity(quantity);
			selectC.setProductId(productId);

			System.out.println(selectC);
			result2 = sService.insertCart(selectC);
		}
		
		try {
			if(isMove.equals("Y")) {
				if(result1 > 0 || result2 >0) {
					// 나중에 연준이가 만든 장바구니 + // cartId 셀렉트키 해와서 url 뒤에 붙이기
					response.sendRedirect("/my/cart/checkout/" + selectC.getCartId());
				} else {
					throw new ShopException("구매하기를 실패하였습니다.");
				}
				
			} else {
				// ajax통신
				String result = (result1 + result2) != 0 ? "success" : "fail";
				Gson gson = new GsonBuilder().create();
				response.setContentType("application/json; charset=UTF-8");
				gson.toJson(result, response.getWriter());
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/shop/paymentPage")
	public String moveToPayment(HttpSession session, Model model) {
		String mbId = ((Member)session.getAttribute("loginUser")).getMbId();
		ArrayList<Cart> cartList = sService.selectUserCart(mbId);
		return "shopPayment";
	}
	
	@GetMapping(value="/shop/insertReply")
	public void insertReply(@ModelAttribute Reply r, HttpServletResponse response,
							HttpSession session) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		r.setBType("P");
		int result = sService.insertReply(r);
		
		ArrayList<Reply> rList = sService.selectReply(r);
		ArrayList<Integer> rNos = new ArrayList<>();
		ArrayList<ReplyLike> likes = null;
		if(!rList.isEmpty()) {
			for(int i = 0; i < rList.size(); i++) {
				rNos.add(rList.get(i).getRNo());
			}
			likes = sService.selectReplyLike(rNos);
			
			if(loginUser != null) {
				for(int i = 0; i < rList.size(); i++) {
					for(int j = 0; j < likes.size(); j ++) {
						
						if((rList.get(i).getRNo() == likes.get(j).getLikeRefBoard()) && likes.get(j).getLikeUser().equals(loginUser.getMbId())) {
							rList.get(i).setUserLiked(true);
						}
					}
	 			}
			}
		}
		
		HashMap<String, Object> responseData = new HashMap<>();
		responseData.put("rList", rList);
		responseData.put("likes", likes);
		
		if(result > 0) {
			HashMap<String, Object> notifyMap = new HashMap<>();
			notifyMap.put("mbId", "admin");	// 타겟
			notifyMap.put("notiType", "SR");
			notifyMap.put("notiContent", "해당 상품에 리뷰가 달렸습니다. / " + r.getReContent());
			notifyMap.put("fromUser", r.getMbId());		// 보내는 사람
			notifyMap.put("refNo", r.getBNo());
			
			int notify = sService.insertNotify(notifyMap);
		}
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		
		try {
			response.setContentType("application/json; charset=UTF-8");
			gson.toJson(responseData, response.getWriter());
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/shop/moveToUpdate")
	public String moveToUpdateProduct(@RequestParam("productId") int pId, Model model,
									@RequestParam("page") int page) {
		
		// p + stock
		Product p = sService.selectDetailProduct(pId);
		ArrayList<ProductAttm> aList = sService.selectProductAttm(pId);
		// 선택되어 있어야 하는 상품 카테고리
		ProductCategory c = sService.selectDetailCategory(p.getProductDetailNo());
		ArrayList<ProductCategory> cList = sService.selectCategory(9999);
		// 대분류 1개씩 뿌리기 위한 bList
		ArrayList<ProductCategory> bList = sService.selectCategory(null);
		String[] colors = null;
		if(!p.getColor().equals("N")) {
			colors = p.getColor().split(",");
		}
		
		model.addAttribute("page", page);
		model.addAttribute("p", p);
		model.addAttribute("aList", aList);
		model.addAttribute("cList", cList);
		model.addAttribute("c", c);
		model.addAttribute("bList", bList);
		model.addAttribute("colors", colors);
		
		return "shopEdit";
	}
	
	@PostMapping("/shop/updateProduct")
	public String updateProduct(@ModelAttribute Product p,
								@RequestParam(value="thumbnailFile", required=false) ArrayList<MultipartFile> thumbFiles,
								@RequestParam(value="contentFile", required=false) ArrayList<MultipartFile> contentFiles,
								@RequestParam("deleteAttm") String[] deleteAttm,
								RedirectAttributes re,
								@RequestParam("page") int page) {
		
		if(p.getColor() == null) {
			p.setColor("N");
		}
		
		int result1 = sService.updateProduct(p);
		int result2 = sService.updateStock(p);
		
		// 썸네일 리네임 과정
		ArrayList<ProductAttm> list = new ArrayList<ProductAttm>();
		if(thumbFiles != null) {
			for(int i = 0; i < thumbFiles.size(); i++) {
				MultipartFile upload = thumbFiles.get(i);
				if(!upload.getOriginalFilename().equals("")) {
					String[] returnArr = saveFile(upload);
					if(returnArr[1] != null) {
						ProductAttm t = new ProductAttm();
						t.setOriginalName(upload.getOriginalFilename());
						t.setRenameName(returnArr[1]);
						t.setAttmPath(returnArr[0]);
						t.setAttmRefNo(p.getProductId());
						t.setAttmLevel(1);
						
						list.add(t);
					}
				}
			}
		}
		
		// 내용 이미지 리네임 과정
		if(contentFiles != null) {
			for(int i = 0; i < contentFiles.size(); i++) {
				MultipartFile upload = contentFiles.get(i);
				if(!upload.getOriginalFilename().equals("")) {
					String[] returnArr = saveFile(upload);
					if(returnArr[1] != null) {
						ProductAttm c = new ProductAttm();
						c.setOriginalName(upload.getOriginalFilename());
						c.setRenameName(returnArr[1]);
						c.setAttmPath(returnArr[0]);
						c.setAttmRefNo(p.getProductId());
						
						c.setAttmLevel(2);
						
						list.add(c);
					}
				}
			}
		}
		
		// 삭제하기로 선택된 원본 리네임 String[], 유지하기로 한 비교값은 "none"
		ArrayList<String> delRename = new ArrayList<>();
		for(int i = 0; i < deleteAttm.length; i++) {
			String rename = deleteAttm[i];
			if(!rename.equals("none")) {
				String[] split = rename.split("/");
				System.out.println(split);
				delRename.add(split[0]);
			}
		}
		
		int deleteAttmResult = 0;
		int insertAttmResult = 0;
		// 삭제하기로 한게 있으면~
		if(!delRename.isEmpty()) {
			deleteAttmResult = sService.deleteAttm(delRename);
			if(deleteAttmResult > 0) {
				for(String rename : delRename) {
					deleteFile(rename);
				}
			}
		}
		
		if(!list.isEmpty()) {
			insertAttmResult = sService.insertAttm(list);
		}
		
		if(result1 + result2 + insertAttmResult == list.size() + 2) {
			re.addAttribute("pId", p.getProductId());
			re.addAttribute("page", page);
			return "redirect:/shop/productDetail";
		} else {
			throw new ShopException("상품 수정에 실패하였습니다.");
		}
	}
	
	@GetMapping("/shop/deleteProduct")
	public String deleteProduct(@RequestParam("page") int page,
								@RequestParam("pId") int pId,
								HttpSession session,
								RedirectAttributes re) {
		
		Member m = ((Member)session.getAttribute("loginUser"));
		if(m.getIsAdmin().equals("Y")) {
			int result = sService.deleteProduct(pId);
			int result2 = sService.attmStatusYN(pId);
			if(result > 0 && result2 > 0) {
				re.addAttribute("page", page);
				return "redirect:/shopMain";
			} else if(result == 0){
				throw new ShopException("상품 삭제에 실패하였습니다.");
			} else if(result2 == 0) {
				throw new ShopException("이미지 삭제에 실패하였습니다.");
			} else {
				throw new ShopException("알수없는 이유에 의해 상품 삭제에 실패하였습니다.");
			}
		} else {
			throw new ShopException("권한이 없습니다.");
		}
	}
	
	@GetMapping(value="/shop/insertReplyCount")
	public void insertReplyCount(@RequestParam("mId") String mId,
									@ModelAttribute Reply r,
									HttpServletResponse response) {
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("mId", mId);
		map.put("r", r);
		
		int result = sService.insertReplyCount(map);
		String out = result != 0 ? "success" : "fail";
		
		if(result > 0) {
			// mbId="rNo.mbId",notiType= rl noti_content is_read ="N" from_user = "mId", REF_NO = "rNo" Sysdate
			HashMap<String, Object> notifyMap = new HashMap<>();
			notifyMap.put("mbId", r.getMbId());
			notifyMap.put("notiType", "RL");
			notifyMap.put("notiContent", "회원님의 댓글에 좋아요!!");
			notifyMap.put("fromUser", mId);
			notifyMap.put("refNo", r.getRNo());
			
			int notify = sService.insertNotify(notifyMap);
		}
		
		Gson gson = new GsonBuilder().create();
		
		try {
			response.setContentType("application/json; charset=UTF-8");
			gson.toJson(out, response.getWriter());
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@GetMapping(value="/shop/downReplyCount")
	public void downReplyCount(@RequestParam("mId") String mId,
								@RequestParam("rNo") int rNo,
								HttpServletResponse response) {
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("mId", mId);
		map.put("rNo", rNo);
		
		int result = sService.downReplyCount(map);
		
		String out = result != 0 ? "success" : "fail";
		
		Gson gson = new GsonBuilder().create();
		
		try {
			response.setContentType("application/json; charset=UTF-8");
			gson.toJson(out, response.getWriter());
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping(value="/shop/updateReply")
	public void updateReply(@ModelAttribute Reply r,
							HttpServletResponse response) {
		
		int result = sService.updateReply(r);
		
		String out = result != 0 ? "success" : "fail";
		
		Gson gson = new GsonBuilder().create();
		
		try {
			response.setContentType("application/json; charset=UTF-8");
			gson.toJson(out, response.getWriter());
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping(value="/shop/deleteReply")
	public void deleteReply(@RequestParam("rNo") int rNo,
							HttpServletResponse response) {
		
		int result = sService.deleteReply(rNo);
		
		String out = result != 0 ? "success" : "fail";
		
		Gson gson = new GsonBuilder().create();
		
		try {
			response.setContentType("application/json; charset=UTF-8");
			gson.toJson(out, response.getWriter());
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping(value="/shop/insertStampProduct")
	@ResponseBody
	public String insertStampProduct(@RequestParam("mbId") String mbId,
									@RequestParam("pId") int pId,
									@RequestParam("type") String type) {
		
		HashMap<String, Object> stamp = new HashMap<>();
		stamp.put("mbId", mbId);
		stamp.put("pId", pId);
		stamp.put("type", type);
		
		int result = sService.insertStampProduct(stamp);
		String out = result == 0 ? "fail" : "success";
		
		return out;
	}
	
	@GetMapping("/shop/deleteStampProduct")
	@ResponseBody
	public String deleteStampProduct(@RequestParam("mbId") String mbId,
									@RequestParam("pId") int pId,
									@RequestParam("type") String type) {
		
		HashMap<String, Object> stamp = new HashMap<>();
		stamp.put("mbId", mbId);
		stamp.put("pId", pId);
		stamp.put("type", type);
		
		int result = sService.deleteStampProduct(stamp);
		String out = result == 0 ? "fail" : "success";
		
		return out;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
