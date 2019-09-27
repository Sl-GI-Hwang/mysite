package kr.co.itcen.mysite.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.itcen.mysite.service.BoardService;
import kr.co.itcen.mysite.service.GuestbookService;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.GuestbookVo;
import kr.co.itcen.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(
			@RequestParam(value = "currentPage", defaultValue = "1", required = false) long currentPage ,Model model) {
		
		model.addAttribute("pvo", boardService.findPage(currentPage));
		model.addAttribute("list", boardService.get(currentPage));
		return "board/list";
	}
	
	@RequestMapping(value="/list", method=RequestMethod.POST)
	public String list(@RequestParam(value = "kwd", required = false) String kwd, 
			@RequestParam(value = "currentPage", required = false) long currentPage ,Model model) {

		model.addAttribute("pvo", boardService.findPage(currentPage));
		model.addAttribute("list", boardService.get(currentPage));
		return "board/list";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String insert() {
		return "board/write";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String insert(@ModelAttribute BoardVo vo, HttpSession session) {
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		vo.setUserNo(authUser.getNo());
		
		boardService.insert(vo);
		return "redirect:/board/list";
	}
	
	@RequestMapping(value="/read/no={no}&currentPage={currentPage}", method=RequestMethod.GET)
	public String read(@PathVariable long no,
			@PathVariable long currentPage, Model model) {
		model.addAttribute("readboard", boardService.getContents(no));
		return "board/view";
	}
	
	@RequestMapping(value="/modify/no={no}&currentPage={currentPage}", method=RequestMethod.GET)
	public String modify(@PathVariable long no,
			@PathVariable long currentPage, Model model) {
		model.addAttribute("readboard", boardService.getContents(no));
		return "board/modify";
	}
	
	@RequestMapping(value="/reply/gNo={gNo}&oNo={oNo}&depth={depth}&currentPage={currentPage}", method=RequestMethod.GET)
	public String reply(@PathVariable long gNo, @PathVariable long oNo,
			@PathVariable long depth, @PathVariable long currentPage, Model model) {
		BoardVo vo = new BoardVo();
		vo.setgNo(gNo);
		vo.setoNo(oNo);
		vo.setDepth(depth);
		
		model.addAttribute("list", vo);
		model.addAttribute("currentPage", currentPage);
		return "board/write";
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(@ModelAttribute BoardVo vo, Model model) {
		boardService.modify(vo);	
		return "redirect:/board/list";
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String search(@RequestParam(value = "kwd", required = false) String kwd, 
			@RequestParam(value = "currentPage", required = false) long currentPage, Model model) {
		model.addAttribute("list", boardService.search(kwd, currentPage));
		return "board/list";
	}
	
	
	@RequestMapping(value="/delete/no={no}&gNo={gNo}&oNo={oNo}&depth={depth}", method=RequestMethod.GET)
	public String delete(@PathVariable long no, 
			@PathVariable long gNo, @PathVariable long oNo,
			@PathVariable long depth, Model model) {
		BoardVo vo = new BoardVo();
		vo.setNo(no);
		vo.setgNo(gNo);
		vo.setoNo(oNo);
		vo.setDepth(depth);
		
		boardService.delete(vo);
		return "redirect:/board/list";
	}
	
}
