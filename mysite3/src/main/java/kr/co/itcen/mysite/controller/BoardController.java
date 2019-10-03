package kr.co.itcen.mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.itcen.mysite.service.BoardService;
import kr.co.itcen.mysite.vo.BoardVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(@RequestParam(value = "kwd", required = false, defaultValue = "") String kwd, 
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") long currentPage ,Model model) {
		
		if(kwd.equals("")) {
			model.addAttribute("list", boardService.get(currentPage));
			kwd = null;
		} else {
			model.addAttribute("list", boardService.search(kwd, currentPage));
			model.addAttribute("keyword", kwd);
		}
		model.addAttribute("pvo", boardService.findPage(currentPage, kwd));
		return "board/list";
	}
	
	
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String insert() {
		return "board/write";
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
