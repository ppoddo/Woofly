
package com.kh.woofly.cart.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.woofly.cart.model.service.CartService;
import com.kh.woofly.cart.model.vo.Cart;
import com.kh.woofly.member.model.vo.Member;
import com.kh.woofly.member.model.vo.MemberAddress;
import com.kh.woofly.member.model.vo.Payment;
import com.kh.woofly.order.model.vo.Order;
import com.kh.woofly.order.model.vo.OrderDetail;
import com.kh.woofly.shop.model.vo.ProductAttm;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	
	@Autowired
	private CartService cService;
	
	@GetMapping("my/cart")
	public String cardView(HttpSession session, Model model) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		ArrayList<Cart> cList = cService.selectMyCart(id);
		ArrayList<ProductAttm> paList = new ArrayList<>();
		int total = 0;
		boolean selectedAll = true;
		int selectedCount = 0;
		for (Cart c : cList) {
			paList.add(cService.selectCartAttm(c));
			if(c.getSelected().equals("N")) {
				selectedAll = false;
			} else {
				total += c.getPrice() * c.getQuantity();
				selectedCount++;
			}
		}
	
		model.addAttribute("selectedAll", selectedAll);
		model.addAttribute("selectedCount", selectedCount);
		model.addAttribute("total", total);
		model.addAttribute("paList", paList);
		model.addAttribute("cList", cList);
		return "myCart";
	}
	
	@GetMapping("updateMyCart.yj")
	@ResponseBody
	public String updateMyCart(HttpSession session, @RequestParam(value="all", required=false) String all,
							   @RequestParam(value="selected", required=false) String selected, 
							   @RequestParam(value="cartId", required=false) String cartId,
							   @RequestParam(value="quantity", required=false) Integer quantity) {
		HashMap<String, Object> map = new HashMap<>();
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		if(all != null) {
			map.put("all", all.equals("Y") ? "N" : "Y");
			map.put("id", id);
		} else if (selected != null) {
			map.put("selected", selected);
			map.put("cartId", cartId);
		} else if (quantity != null){
			map.put("quantity", quantity);
			map.put("cartId", cartId);
			
		}
		
		int result = cService.updateMyCart(map);
		if(result > 0) {
			return "good";
		} else {
			return "bad";
		}
	}
	
	@GetMapping("my/cart/checkout/{cartId}")
	public String checkOut(@PathVariable(value="cartId") int cartId, Model model, HttpSession session) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		
		ArrayList<Cart> cList = new ArrayList<Cart>();
		Cart cart = cService.selectCart(cartId);
		cList.add(cart);
		
		ArrayList<ProductAttm> paList = new ArrayList<>();
		paList.add(cService.selectCartAttm(cart));
		
		ArrayList<MemberAddress> maList = cService.selectDefaultAddr(id);
		// 내 결제 정보
		ArrayList<Payment> pList = cService.selectPayment(id);
		model.addAttribute("pList", pList);
		model.addAttribute("total", cart.getPrice() * cart.getQuantity() + 3000);
		model.addAttribute("cList", cList);
		model.addAttribute("paList", paList);
		model.addAttribute("maList", maList);
		model.addAttribute("points", Math.round(cart.getPrice() * cart.getQuantity() * 0.1));

		return "checkOut";
	}
	
	@GetMapping("my/cart/checkout")
	public String checkOutAll(Model model, HttpSession session) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		int total = 0;
		
		ArrayList<Cart> cList = cService.selectAllCart(id);;
		
		ArrayList<ProductAttm> paList = new ArrayList<>();
		for(Cart c : cList) {
			total += c.getPrice() * c.getQuantity();
			paList.add(cService.selectCartAttm(c));
		}
		
		
		ArrayList<MemberAddress> maList = cService.selectDefaultAddr(id);
		// 내 결제 정보
		ArrayList<Payment> pList = cService.selectPayment(id);
		model.addAttribute("points", Math.round(total * 0.1));
		model.addAttribute("pList", pList);
		model.addAttribute("total", total+3000);
		model.addAttribute("cList", cList);
		model.addAttribute("paList", paList);
		model.addAttribute("maList", maList);
		
		return "checkOut";
	}
	
	@PostMapping("checkout.yj")
	public String sendPayment(@RequestParam("amount") int amount, @RequestParam("cartId") int[] cartIds, @RequestParam("billingKey") String billingKey, HttpSession session,
							  @RequestParam("addrId") int addrId, @RequestParam("orderRequest") String orderRequest, @RequestParam("points") int points, @RequestParam("usedPoints") int usedPoints) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		HashMap<String, Object> map = new HashMap<>();
		map.put("id", loginUser.getMbId());
		map.put("points", points);
		if (usedPoints >= 1000) {
			map.put("usedPoints", usedPoints);
			amount -= usedPoints;
			int upResult = cService.usePoints(map);
		}
		int apResult = cService.addPoints(map);
		MemberAddress ma = cService.selectAddr(addrId);
		Order o = new Order(0, null, amount, loginUser.getMbId(), 3000, 0, ma.getMbName(), ma.getMbTel(), "("+ma.getPostcode()+")"+ma.getAddr()+ma.getAddrDetail(), "카드", orderRequest, "0", cartIds.length);
		int oResult = cService.insertOrder(o);
		
		int odResult = 0;
		int cResult = 0;
		for(int i : cartIds) {
			Cart c = cService.selectCart(i);
			OrderDetail od = new OrderDetail(0, c.getQuantity(), 0, c.getProductId(), o.getOrderId(), null);
			odResult += cService.insertOrderDetail(od);
			cResult += cService.deleteCart(c);
			
		}
		
		
		System.out.println(billingKey);
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.tosspayments.com/v1/billing/" + billingKey))
			    .header("Authorization", "Basic dGVzdF9za19rWUc1N0ViYTNHNkFlRG40NXFhOThwV0RPeG1BOg==")
			    .header("Content-Type", "application/json")
			    .method("POST", HttpRequest.BodyPublishers.ofString("{\"customerKey\":\"" + loginUser.getMbId() + 
			    													"\",\"amount\":" + (amount) + 
			    													",\"orderId\":\"" + o.getOrderId() + 
			    													"\",\"orderName\":\"" + "WOOFLY" + 
			    													"\",\"customerEmail\":\"" + loginUser.getMbEmail() +
			    													"\",\"customerName\":\"" + loginUser.getMbName() + 
			    													"\",\"taxFreeAmount\":0}"))
			    .build();
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "redirect:/my/buying";
	}
	
	@GetMapping("deleteOne.yj")
	public String deleteOneCart(@RequestParam("cartId") int cartId) {
		Cart c = new Cart();
		c.setCartId(cartId);
		int result = cService.deleteCart(c);
		
		return "redirect:/my/cart";
	}
	
	@GetMapping("deleteAll.yj")
	public String deleteAllCart(HttpSession session) {
		String id = ((Member)session.getAttribute("loginUser")).getMbId();
		int result = cService.deleteAllCart(id);
		
		return "redirect:/my/cart";
	}
		
		
}
























