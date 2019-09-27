package kr.co.itcen.mysite.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.itcen.mysite.dao.BoardDao;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.web.WebUtils;
import kr.co.itcen.web.mvc.Action;

public class ModifyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Long no = Long.parseLong(request.getParameter("no"));
		Long userNo = Long.parseLong(request.getParameter("userNo"));
		Long currentPage = Long.parseLong(request.getParameter("currentPage"));
		
		BoardVo readboard = new BoardVo();
		readboard = new BoardDao().getContents(no, userNo ,1);
		
		request.setAttribute("readboard", readboard);
		request.setAttribute("currentPage", currentPage);
		
		WebUtils.forward(request, response, "/WEB-INF/views/board/modify.jsp");
	}

}
