package kr.co.itcen.mysite.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.itcen.mysite.dao.BoardDao;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.web.WebUtils;
import kr.co.itcen.web.mvc.Action;

public class DeleteboardAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BoardVo vo = new BoardVo();
		
		Long no = Long.parseLong(request.getParameter("no"));
		Long gNo = Long.parseLong(request.getParameter("gNo")); 
		Long oNo = Long.parseLong(request.getParameter("oNo")); 
		Long depth = Long.parseLong(request.getParameter("depth")); 
		
		vo.setNo(no);
		vo.setgNo(gNo);
		vo.setoNo(oNo);
		vo.setDepth(depth);
		
		new BoardDao().delete(vo);
		
		WebUtils.forward(request, response, "/WEB-INF/views/board/deletesuccess.jsp");		
	}

}
