
package AnVatPTIT.Controller.User; 

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import AnVatPTIT.Dto.CartDto;
import AnVatPTIT.Dto.ProductsDto;
import AnVatPTIT.Entity.BillDetail;
import AnVatPTIT.Entity.Bills;
import AnVatPTIT.Entity.Users;
import AnVatPTIT.Service.User.BillsServiceImpl;
import AnVatPTIT.Service.User.CartServiceImpl;
import AnVatPTIT.Service.User.ProductServiceImpl;
import AnVatPTIT.Service.User.AccountServiceImpl;

@Controller
public class CartController extends BaseController {

	@Autowired
	private CartServiceImpl cartService = new CartServiceImpl();

	@Autowired
	private BillsServiceImpl billsService = new BillsServiceImpl();
	
	@Autowired
	private AccountServiceImpl accountService = new AccountServiceImpl();
	
	@Autowired
	private ProductServiceImpl productService = new ProductServiceImpl();
	
	@RequestMapping(value = "gio-hang")
	public ModelAndView Index() {
		_mvShare.addObject("slides", _homeService.GetDataSlide());
		_mvShare.addObject("products", _homeService.GetDataProducts());
		_mvShare.setViewName("user/cart/list_cart");
		return _mvShare;
	}

	@RequestMapping(value = "AddCart/{id}")
	public String AddCart(HttpServletRequest request, HttpSession session, @PathVariable long id) {
		HashMap<Long, CartDto> cart = (HashMap<Long, CartDto>) session.getAttribute("Cart");
		if (cart == null) {
			cart = new HashMap<Long, CartDto>();
		}
		cart = cartService.AddCart(id, cart);
		session.setAttribute("Cart", cart);
		session.setAttribute("TotalQuantyCart", cartService.TotalQuanty(cart));
		session.setAttribute("TotalPriceCart", cartService.TotalPrice(cart));
		return "redirect:" + request.getHeader("Referer");
	}

	@RequestMapping(value = "EditCart/{id}/{quanty}")
	public String EditCart(HttpServletRequest request, HttpSession session, @PathVariable long id,

			@PathVariable int quanty) {
		HashMap<Long, CartDto> cart = (HashMap<Long, CartDto>) session.getAttribute("Cart");
		if (cart == null) {
			cart = new HashMap<Long, CartDto>();
		}
		cart = cartService.EditCart(id, quanty, cart);
		session.setAttribute("Cart", cart);
		session.setAttribute("TotalQuantyCart", cartService.TotalQuanty(cart));
		session.setAttribute("TotalPriceCart", cartService.TotalPrice(cart));
		return "redirect:" + request.getHeader("Referer");
	}

	@RequestMapping(value = "DeleteCart/{id}")
	public String DeleteCart(HttpServletRequest request, HttpSession session, @PathVariable long id) {
		HashMap<Long, CartDto> cart = (HashMap<Long, CartDto>) session.getAttribute("Cart");
		if (cart == null) {
			cart = new HashMap<Long, CartDto>();
		}
		cart = cartService.DeleteCart(id, cart);
		session.setAttribute("Cart", cart);
		session.setAttribute("TotalQuantyCart", cartService.TotalQuanty(cart));
		session.setAttribute("TotalPriceCart", cartService.TotalPrice(cart));
		return "redirect:" + request.getHeader("Referer");
	}

	@RequestMapping(value = "checkout", method = RequestMethod.GET)
	public ModelAndView CheckOut(HttpServletRequest request, HttpSession session) {
		_mvShare.setViewName("user/bills/checkout");
		Bills bills = new Bills();
		Users loginInfo = (Users) session.getAttribute("LoginInfo");
		if (loginInfo != null) {
			bills.setAddress(loginInfo.getAddress());
			bills.setDisplay_name(loginInfo.getDisplay_name());
			bills.setUser(loginInfo.getUser());
		}
		_mvShare.addObject("bills", bills);
		return _mvShare;
	}

	@RequestMapping(value = "checkout", method = RequestMethod.POST)
	public ModelAndView CheckOutBill(HttpServletRequest request, HttpSession session, @ModelAttribute("bills") Bills bill) {
		
		Users acc = accountService.AddEmail(bill.getUser().trim());
		_mvShare.setViewName("redirect:chi-tiet-don-hang?id="+acc.getId());
		
		
		Users loginInfo = (Users) session.getAttribute("LoginInfo");
		bill.setId_user(loginInfo.getId());
		bill.setQuanty(Integer.parseInt(session.getAttribute("TotalQuantyCart").toString()));
		bill.setTotal(Double.parseDouble(session.getAttribute("TotalPriceCart").toString()));
		bill.setId_status(1);
		if (billsService.AddBills(bill) > 0) {
			HashMap<Long, CartDto> carts = (HashMap<Long, CartDto>) session.getAttribute("Cart");
			billsService.AddBillDetail(carts);
		}
		session.removeAttribute("Cart");
		session.removeAttribute("TotalQuantyCart");
		session.removeAttribute("TotalPriceCart");

		return _mvShare;
	}

	@RequestMapping(value = "chi-tiet-don-hang", params = "id", method = RequestMethod.GET)
	public ModelAndView getBillDetail(@RequestParam("id") String id) {
		List<Bills> bill = billsService.SeclectAllByUserId(id);
		
		_mvShare.addObject("billsInfoUser", bill.get((bill.size()-1)));
		
		List<BillDetail> billDetail = new ArrayList<BillDetail>();
//		System.out.println("id bill la: "+bill.get(0).getId());
		try
		{
	
			 billDetail = billsService.SeclectAllByBillId(""+bill.get(0).getId());
		} catch(Exception e){}
	
		
		List<ProductsDto> arrayProduct = new ArrayList<ProductsDto>();
		
//		System.out.println("billDetail la: "+billDetail.size());
//		for(BillDetail billDetail1:billDetail)
//		{
//			List<ProductsDto> products = productService.GetProductByIDProduct(""+billDetail1.getId_product());
//			
//			arrayProduct.addAll(products);
//		}
		_mvShare.addObject("billsProduct", arrayProduct);
		_mvShare.setViewName("user/bills/billDetail");

//		session.removeAttribute("Cart");
//		session.removeAttribute("TotalQuantyCart");
//		session.removeAttribute("TotalPriceCart");
		return _mvShare;
	}
}
