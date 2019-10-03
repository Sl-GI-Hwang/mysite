package kr.co.itcen.mysite.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.itcen.web.WebUtils;
import kr.co.itcen.web.mvc.Action;

public class ReplyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setAttribute("gNo", request.getParameter("gNo"));
		request.setAttribute("oNo", request.getParameter("oNo"));
		request.setAttribute("depth", request.getParameter("depth"));
		WebUtils.forward(request, response, "/WEB-INF/views/board/write.jsp");
	}

}
