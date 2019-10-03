package kr.co.itcen.mysite.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.itcen.mysite.dao.BoardDao;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.PageVo;
import kr.co.itcen.mysite.vo.UserVo;
import kr.co.itcen.web.WebUtils;
import kr.co.itcen.web.mvc.Action;

public class WriteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		long currentPage = 1L;
		String title = request.getParameter("title");
		String contents = request.getParameter("content");
		
		long gNo = 0L;
		long oNo = 0L;
		long depth = 0L;
		long userNo = authUser.getNo();
		if (request.getParameter("gNo") != null) {
			gNo = Long.parseLong(request.getParameter("gNo"));
			oNo = Long.parseLong(request.getParameter("oNo"));
			depth = Long.parseLong(request.getParameter("depth"));
			currentPage = Long.parseLong(request.getParameter("currentPage"));
			
		} else {
			oNo = 0L; 
			depth = 0L;
		}
		
		BoardVo vo = new BoardVo();
		
		vo.setTitle(title);
		vo.setContents(contents);
		vo.setUserNo(userNo);
		vo.setDepth(depth);
		vo.setoNo(oNo);
		vo.setgNo(gNo);
			
		new BoardDao().insert(vo);
				
		PageVo pvo = new PageVo();
		
		long boardCount = new BoardDao().getBoardCount();
		long pageCount = (boardCount/10)+1;
		long firstPage = ((currentPage-1) / 10)* 10 + 1;
			
		pvo.setBoardCount(boardCount);
		pvo.setCurrentPage(currentPage);
		pvo.setPageCount(pageCount);
		pvo.setFirstPage(firstPage);
		
		request.setAttribute("pvo", pvo);
		
		WebUtils.redirect(request, response, request.getContextPath()+"/board?a=board&kwd=&currentPage="+currentPage);	
	}

}
