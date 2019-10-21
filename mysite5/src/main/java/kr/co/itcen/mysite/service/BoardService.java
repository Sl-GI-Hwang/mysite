package kr.co.itcen.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itcen.mysite.repository.BoardDao;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.PageVo;

@Service
public class BoardService {
	@Autowired
	private BoardDao boardDao;
	
	public List<BoardVo> get(long currentPage) {
		List<BoardVo> list = boardDao.getList(currentPage); 
		return list;
	}

	public void insert(BoardVo vo) {
		boardDao.insert(vo);
	}

	public BoardVo getContents(long no) {
		int flag = 0;
		
		BoardVo boardvo = boardDao.getContents(no, flag);
		return boardvo;
	}

	public void modify(BoardVo vo) {
		boardDao.Modify(vo);
	}

	public List<BoardVo> search(String kwd, long currentPage) {
		// TODO Auto-generated method stub
		List<BoardVo> list = boardDao.getSelectedList(kwd, currentPage);
		return list;
	}

	public PageVo findPage(long currentPage, String keyword) {
		PageVo pageList = boardDao.getBoardCount(currentPage, keyword);
		return pageList;
	}
	public void delete(BoardVo vo) {
		boardDao.delete(vo);
	}
}
