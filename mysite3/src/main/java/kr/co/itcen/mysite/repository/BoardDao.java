package kr.co.itcen.mysite.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.PageVo;

@Repository
public class BoardDao {
	@Autowired
	private SqlSession sqlSession;

	public List<BoardVo> getList(long currentPage) {
		currentPage = (currentPage - 1)*10;
		List<BoardVo> result = sqlSession.selectList("board.getlist", currentPage);
		return result;
		
	}	
	
	public Boolean insert(BoardVo vo) {
		if(vo.getoNo() != null && vo.getDepth() != null) {
			vo.setoNo(vo.getoNo()+1);
			vo.setDepth(vo.getDepth()+1);
			sqlSession.update("board.updateOno", vo);
		}

		int result = sqlSession.insert("board.insertBoard", vo);
		return result == 1;
	}
	
	public List<BoardVo> getSelectedList(String kwd, long currentPage) {
		kwd = "%"+kwd+"%";
		currentPage = (currentPage-1)*10;

		Map<String, String> map = new HashMap<String, String>();
		map.put("kwd", kwd);
		map.put("currentPage", Long.toString(currentPage));
		
		List<BoardVo> result = sqlSession.selectList("board.getSelect", map);
		return result;
	}	

	public BoardVo getContents(long no, int flag) {
		if (flag == 0) {
			sqlSession.update("board.updateHit", no);
			flag++;
		}
		BoardVo result = sqlSession.selectOne("board.selectContents", no);
		return result;
	}

	public void Modify(BoardVo vo) {
		sqlSession.update("board.modify", vo);
	}
	
	public int chkDelete(BoardVo vo){
		
		int stat = 0;
		
		List<Long> list = new ArrayList<Long>();
		
		// 하위 List에 대한 검색
		while (true) {
			long res = 0L;
			res = sqlSession.selectOne("childSearch", vo);
			if (res > 0) {
				list.add(res);
				vo.setoNo(vo.getoNo()+1);
			} else {
				break;
			}
		}
		
		if(list.isEmpty()) {
			stat = 3;	
			vo.setoNo(vo.getoNo()-1);
			sqlSession.update("reArray", vo);
				
			return stat;
		}	
		return stat;
	}

	public void delete(BoardVo vo) {
		
		sqlSession.update("board.deletetoZero", vo);
		long buff = vo.getDepth();
		
		// 부모의 no와 oNo를 가져옴
		List <BoardVo> parentOno = new ArrayList<BoardVo>();
		parentOno.add(vo);
		for(int i = 1; i <= buff; i++) {
			vo.setDepth(buff-i);
			BoardVo result = sqlSession.selectOne("board.parentSearch", vo);
			parentOno.add(result);
		}

		vo.setDepth(buff);
		
		Long depth = vo.getDepth();
		Long gNo = vo.getgNo();

		// depth까지 chkDelete를 돌린다 (역순으로 돌려야 함)
		// gNo고정, oNo은 자신+부모 보내고, depth는 -1처리, connection 고정
		Long buf = depth;
		for(long i = 0; i <= depth; i++) {

			parentOno.get((int)i).setgNo(gNo);
			parentOno.get((int)i).setDepth(buf+1-i);
			int res = chkDelete(parentOno.get((int)i));

			// update set o_no = o_no -1 
			// 완전삭제필요 판정나면 update문 다시돌려서 완전삭제시킴
			if (res == 3) {
				parentOno.get((int)i).setoNo(-parentOno.get((int)i).getoNo());
				sqlSession.update("invalidateDelete", parentOno.get((int)i));
			}
		}	

	}

	public PageVo getBoardCount(Long currentPage) {
		Long result = sqlSession.selectOne("board.boardCount");
		PageVo pageList = new PageVo();
		
		pageList.setBoardCount(result);
		pageList.setCurrentPage(currentPage);
		pageList.setPageCount((result/10)+1);
		pageList.setFirstPage(((currentPage-1) / 10)* 10 + 1);
		
		return pageList;
	}
}
