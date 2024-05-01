package com.kh.woofly.order.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.woofly.board.model.vo.UsedBoard;
import com.kh.woofly.common.PageInfo;
import com.kh.woofly.common.Pagination;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.member.model.vo.Point;
import com.kh.woofly.order.model.service.OrderService;
import com.kh.woofly.order.model.vo.Order;
import com.kh.woofly.order.model.vo.OrderDetail;
import com.kh.woofly.order.model.vo.Saved;
import com.kh.woofly.shop.model.vo.Product;
import com.kh.woofly.shop.model.vo.ProductAttm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

	@Autowired
	private OrderService oService;
	
	@GetMapping("/my")
	public String profileHomeView(HttpSession session, Model model) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		HashMap<String, Object> map = new HashMap<>();
		map.put("id", id);
		Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Subtract 6 months from the current date

        // Print the result
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        map.put("startDate", null);
		map.put("endDate", currentDate);
		
		map.put("orderDate", "desc");
		
		ArrayList<Order> oList = oService.selectMyBuying(null, map);
		ArrayList<ProductAttm> paList = new ArrayList<>();
		ArrayList<Product> pList = new ArrayList<>();
		
		for(Order o : oList) {
			paList.add(oService.selectOrderAttm(o));
			pList.add(oService.selectMostExpensive(o));
		}
		
        int result = oService.deletePoints(id);
        ArrayList<Point> pointList = oService.selectMyPoints(id);
        int pointsUsable = 0;
        for (Point p : pointList) {
        	if(p.getTransactionType().equals("A")) {
        		pointsUsable += p.getPointChange();
        	} else {
        		pointsUsable -= p.getPointChange();
        	}
        }
        
        ArrayList<UsedBoard> uList = oService.selectMySelling(map);
        ArrayList<Saved> sList = oService.selectMySavedHome(map);
        
        
        model.addAttribute("pointsUsable", pointsUsable);
        model.addAttribute("sList", sList);
        model.addAttribute("uList", uList);
		model.addAttribute("pList", pList);
		model.addAttribute("paList", paList);
		model.addAttribute("oList", oList);
		
		return "myHome";
	}
	
	@GetMapping("my/buying")
	public String buyingView(HttpSession session, Model model,@RequestParam(value="page", defaultValue="1") int page,
							 @RequestParam(value="startDate", required=false) String startDate, 
							 @RequestParam(value="endDate", required=false) String endDate, HttpServletRequest request,
							 @RequestParam(value="sort", defaultValue="orderDate desc") String sort) {
		
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		HashMap<String, Object> map = new HashMap<>();
		map.put("id", id);
		
		try {
	        Date currentDate = new Date();
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(currentDate);
	        calendar.add(Calendar.DAY_OF_MONTH, 1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if (startDate == null) {
				map.put("startDate", null);
			} else {
				map.put("startDate", sdf.parse(startDate));
			}
			
			if (endDate == null) {
				map.put("endDate", sdf.format(calendar.getTime()));
			} else {
				map.put("endDate", sdf.parse(endDate));
				
				Date newDate = sdf.parse(endDate);
				if (newDate.after(new Date())) {
					
					Calendar newCalendar = Calendar.getInstance();
					newCalendar.setTime(newDate);
					newCalendar.add(Calendar.DAY_OF_MONTH, -1);
					endDate = sdf.format(newCalendar.getTime());
				}
				
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		int listCount = oService.getBuyingCount(map);
		PageInfo pi = Pagination.getPageInfo(page, listCount, 10);
		map.put(sort.split(" ")[0], sort.split(" ")[1]);

		ArrayList<Order> oList = oService.selectMyBuying(pi, map);
		ArrayList<ProductAttm> paList = new ArrayList<>();
		ArrayList<Product> pList = new ArrayList<>();
		
		
		for(Order o : oList) {
			paList.add(oService.selectOrderAttm(o));
			pList.add(oService.selectMostExpensive(o));
		}
		
		model.addAttribute("sort", sort);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("loc", request.getRequestURI());
		model.addAttribute("pi", pi);
		model.addAttribute("pList", pList);
		model.addAttribute("paList", paList);
		model.addAttribute("oList", oList);
		return "myBuying";
	}
	
	@GetMapping("my/buying/{orderNo}")
	public String buyingDetailView(@PathVariable("orderNo") int orderNo, Model model) {
		System.out.println(orderNo);
		Order order = oService.selectOrder(orderNo);
		ArrayList<OrderDetail> oDetail = oService.selectOrderDetails(orderNo);
		ArrayList<ProductAttm> paList = new ArrayList<>();
		for(OrderDetail detail : oDetail) {
			paList.add(oService.selectOrderProductsAttm(detail));
		}
		System.out.println(paList);
		
		model.addAttribute("paList", paList);
		model.addAttribute("order", order);
		model.addAttribute("details", oDetail);
		return "myBuyingDetail";
	}
	
	@GetMapping("my/saved")
	public String savedView(HttpSession session, Model model) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		HashMap<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("savedType", "P");
		ArrayList<Saved> list = oService.selectMySaved(map);
		model.addAttribute("list", list);
		return "mySaved";
	}
	
	@GetMapping("my/saved/used")
	public String usedSaved(HttpSession session, Model model) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		HashMap<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("savedType", "U");
		ArrayList<Saved> list = oService.selectMySaved(map);
		model.addAttribute("list", list);
		return "mySavedUsed";
	}

}