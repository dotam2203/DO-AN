package AnVatPTIT.Controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MessageController_Admin extends BaseController_Admin{
	@RequestMapping("/admin/message")
	public ModelAndView Message() {
		_mvShare.setViewName("admin/errorpage");
		return _mvShare;
	}
}
