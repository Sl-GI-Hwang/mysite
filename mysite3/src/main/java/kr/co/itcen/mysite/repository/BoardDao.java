package kr.co.itcen.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.GuestbookVo;
import kr.co.itcen.mysite.vo.PageVo;
import kr.co.itcen.mysite.vo.UserVo;

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

	private Connection getConnection() throws SQLException {
		Connection connection = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		
			String url = "jdbc:mariadb://192.168.1.88:3306/webdb?characterEncoding=utf8";
			connection = DriverManager.getConnection(url, "webdb", "webdb");
		
		} catch (ClassNotFoundException e) {
			System.out.println("Fail to Loading Driver:" + e);
		}
		
		return connection;
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
	
	public int chkDelete(long vo, long l, long m, Connection connection) throws SQLException {
				
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int stat = 0;
		int count = 0;
		Long oNo = l+1;
		Long depth = m;
		
		String sql = null;
		List<Long> list = new ArrayList<Long>();
		
		// 하위 List에 대한 검색
		while (true) {
			long res = 0L;
			sql = 
					"	select no from board " + 
					"	where g_no = ?" + 
					"	and depth = ?" + 
					"	and o_no = ?; ";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, vo);
			pstmt.setLong(2, depth);
			pstmt.setLong(3, oNo);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				res = rs.getLong(1);
			}	
			if (res > 0) {
				list.add(res);
				count++;
				oNo++;			
			} else {
				break;
			}
		}
		
		if(list.isEmpty()) {
			stat = 3;	
			oNo = oNo - 1;
		
			sql = 
					"	update board set o_no = o_no - 1 " + 
					"	where g_no = ? " + 
					"	and o_no > ?; ";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, vo);
			pstmt.setLong(2, oNo);
			pstmt.executeUpdate();
			
			return stat;
		}	
		return stat;
	}

	public void delete(BoardVo vo) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
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
		
		try {
			connection = getConnection();
			String sql = null;
			
			Long depth = vo.getDepth();
			Long gNo = vo.getgNo();
												
			// depth까지 chkDelete를 돌린다 (역순으로 돌려야 함)
			// gNo고정, oNo은 자신+부모 보내고, depth는 -1처리, connection 고정
			Long buf = depth;
			for(long i = 0; i <= depth; i++) {
				System.out.println("delo :" + (buf+1-i));
				int res = chkDelete( gNo, parentOno.get((int)i).getoNo(), buf+1-i, connection);

				// update set o_no = o_no -1 
				// 완전삭제필요 판정나면 update문 다시돌려서 완전삭제시킴
				if (res == 3) {
					Long no = parentOno.get((int)i).getNo();
					Long oNo = parentOno.get((int)i).getoNo();
					System.out.println("noono : " + no + ":" + oNo);
					sql = 
							"	update board"  +
							"   set status = ?, o_no = ?" + 
							"	where no = ?"; 
					pstmt = connection.prepareStatement(sql);
					pstmt.setInt(1, 3);
					pstmt.setLong(2, -oNo);
					pstmt.setLong(3, no);
					pstmt.executeUpdate();
				}
			}	
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
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
