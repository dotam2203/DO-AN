package AnVatPTIT.Controller.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import AnVatPTIT.Entity.Users;
import AnVatPTIT.Service.User.AccountServiceImpl;

@Controller
public class UserController extends BaseController {
	@Autowired
	AccountServiceImpl accountService = new AccountServiceImpl();

	@RequestMapping(value = "/dang-ky", method = RequestMethod.GET)
	public ModelAndView Register() {
		_mvShare.addObject("messageResponse", "");
		_mvShare.setViewName("user/account/register");
		_mvShare.addObject("user", new Users());
		
		
		return _mvShare;
	}

	@RequestMapping(value = "/dang-ky", method = RequestMethod.POST)
	public ModelAndView CreateAccount(@ModelAttribute("user") Users user) {
		int count = accountService.AddAccount(user);
		if (count > 0 ) {
			_mvShare.addObject("messageResponse", "Đăng ký tài khoản thành công");
			_mvShare.addObject("alert", "seccess");
			
		} else {
			_mvShare.addObject("messageResponse", "Đăng ký tài khoản không thành công");
			_mvShare.addObject("alert", "danger");
		}
		_mvShare.setViewName("user/account/register");
		return _mvShare;
	}
	
	@RequestMapping(value = "/dang-nhap", method = RequestMethod.POST)
	public ModelAndView Login(HttpServletRequest request, HttpSession session, @ModelAttribute("user") Users user) {
		user = accountService.CheckAccount(user);
		if (user != null) {
			_mvShare.setViewName("redirect:/");
			session.setAttribute("LoginInfo", user);
			
		} else {
			_mvShare.addObject("messageResponse", "Đăng nhập không thành công");
			_mvShare.addObject("alert", "danger");
		}
		
//		session.removeAttribute("Cart");
//		session.removeAttribute("TotalQuantyCart");
//		session.removeAttribute("TotalPriceCart");
		return _mvShare;
	}
	
	@RequestMapping(value = "/dang-xuat", method = RequestMethod.GET)
	public String Login(HttpSession session, HttpServletRequest request) {
		session.removeAttribute("LoginInfo");
		session.removeAttribute("Cart");
		session.removeAttribute("TotalQuantyCart");
		session.removeAttribute("TotalPriceCart");
		
		session.removeAttribute("Cart");
		session.removeAttribute("TotalQuantyCart");
		session.removeAttribute("TotalPriceCart");
		return 	"redirect:" + request.getHeader("Referer");
	}
}
