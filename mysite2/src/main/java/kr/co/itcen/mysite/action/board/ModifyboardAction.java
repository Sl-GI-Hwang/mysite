package kr.co.itcen.mysite.action.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.itcen.mysite.dao.BoardDao;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.PageVo;
import kr.co.itcen.web.WebUtils;
import kr.co.itcen.web.mvc.Action;

public class ModifyboardAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<BoardVo> list = null;
		Long no = Long.parseLong(request.getParameter("no"));
		Long userNo = Long.parseLong(request.getParameter("userNo"));
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		BoardVo readboard = new BoardVo();
		readboard.setNo(no);
		readboard.setUserNo(userNo);
		readboard.setTitle(title);
		readboard.setContents(content);
				
		new BoardDao().Modify(readboard);
		
		PageVo pvo = new PageVo();
		
		long currentPage = Long.parseLong(request.getParameter("currentPage"));
		long boardCount = new BoardDao().getBoardCount();
		long pageCount = (boardCount/10)+1;
		long firstPage = ((currentPage-1) / 10)* 10 + 1;
			
		pvo.setBoardCount(boardCount);
		pvo.setCurrentPage(currentPage);
		pvo.setPageCount(pageCount);
		pvo.setFirstPage(firstPage);
		
		request.setAttribute("pvo", pvo);
		
		//검색도 여기에서
		
		if(request.getParameter("kwd") == "") {
			list = new BoardDao().getList(currentPage);
		} else {
			String keyword = request.getParameter("kwd");
			list = new BoardDao().getSelectedList(keyword, currentPage);
		}
		
		request.setAttribute("list", list);

		WebUtils.redirect(request, response, request.getContextPath()+"/board?a=board&kwd=&currentPage="+currentPage);
		
//		readboard = new BoardDao().getContents(no, userNo);
//		request.setAttribute("readboard", readboard);
//				
//		WebUtils.forward(request, response, "/WEB-INF/views/board/view.jsp");
	}

}
