package kr.co.itcen.mysite.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.itcen.mysite.security.Auth;
import kr.co.itcen.mysite.security.AuthUser;
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
	
	@Auth("USER")
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public String update(
			@AuthUser UserVo authUser, Model model) {
		authUser = userService.getUser(authUser);
		model.addAttribute("userVo", authUser);
		return "user/update";
	}

	@Auth("USER")
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(	
		@ModelAttribute @Valid UserVo vo,
		BindingResult result) {
		userService.update(vo);
		return "user/update";
	}
	
}