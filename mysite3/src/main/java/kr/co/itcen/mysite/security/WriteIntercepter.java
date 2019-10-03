package kr.co.itcen.mysite.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.itcen.mysite.service.BoardService;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.UserVo;

public class WriteIntercepter extends HandlerInterceptorAdapter {
	@Autowired
	private BoardService boardService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler)
			throws Exception {
		BoardVo vo = new BoardVo();
		vo.setTitle(request.getParameter("title"));
		vo.setContents(request.getParameter("contents"));

		if(request.getParameter("gNo") != null) {
			Long gNo = Long.parseLong(request.getParameter("gNo"));
			Long oNo = Long.parseLong(request.getParameter("oNo"));
			Long depth = Long.parseLong(request.getParameter("depth"));
			Long currentPage = Long.parseLong(request.getParameter("currentPage"));
			
			vo.setgNo(gNo);
			vo.setoNo(oNo);
			vo.setDepth(depth);
		}
		
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		
		vo.setUserNo(authUser.getNo());
		
		boardService.insert(vo);
		
		session.setAttribute("authUser", authUser);
		response.sendRedirect(request.getContextPath()+"/board/list");
		return false;
	}
}
