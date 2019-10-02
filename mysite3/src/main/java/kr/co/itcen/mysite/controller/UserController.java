package kr.co.itcen.mysite.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.eclipse.jdt.internal.compiler.lookup.ReductionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.itcen.mysite.exception.UserDaoException;
import kr.co.itcen.mysite.service.UserService;
import kr.co.itcen.mysite.vo.UserVo;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/joinsuccess", method=RequestMethod.GET)
	public String joinsuccess() {
		return "user/joinsuccess";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String join(@Valid UserVo vo, BindingResult result) {
		if(result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for(ObjectError error : list) {
				System.out.println(error);
			}
		}
		return "user/join";
	}

	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(@ModelAttribute UserVo vo,
			BindingResult result,
			Model model) {
		if(result.hasErrors()) {
			model.addAllAttributes(result.getModel());
			
			Map<String, Object> m = result.getModel();
			for(String key : m.keySet()) {
				model.addAttribute("", m.get(key));
			}
			return "user/join";
		}
		userService.join(vo);
		return "redirect:/user/joinsuccess";
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login() {
		return "user/login";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public String update() {
		return "user/update";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(
			@ModelAttribute UserVo vo
			) {
		userService.update(vo);
		return "";
	}

//	@RequestMapping(value="/login", method=RequestMethod.POST)
//	public String login(UserVo vo, HttpSession session, Model model) {
//		UserVo userVo = userService.getUser(vo);
//		if(userVo == null) {
//			model.addAttribute("result", "fail");
//			return "user/login";
//		}
//		// 로그인 처리
//		session.setAttribute("authUser", userVo);
//		return "redirect:/";
//	}

//	@RequestMapping(value="/logout", method=RequestMethod.GET)
//	public String logout(HttpSession session) {
//		//접근 제어(ACL)
//		UserVo authUser = (UserVo)session.getAttribute("authUser");
//		if(authUser != null) {
//			session.removeAttribute("authUser");
//			session.invalidate();
//		}
//
//		return "redirect:/";
//	}
	
//	@ExceptionHandler(UserDaoException.class)
//	public String handlerException() {
//		return "error/exception";
//	}
	
}