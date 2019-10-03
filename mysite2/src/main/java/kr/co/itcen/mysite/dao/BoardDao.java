package kr.co.itcen.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import kr.co.itcen.mysite.vo.BoardVo;

public class BoardDao {

	public Boolean insert(BoardVo vo) {
		Boolean result = false;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			String sql = null;
			
			long onVal = vo.getoNo()+1;
			long depVal = vo.getDepth()+1;
												
			sql = "update board set o_no=o_no+1 where g_no=? and o_no >= ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, vo.getgNo());
			pstmt.setLong(2, onVal);
			pstmt.executeUpdate();
			pstmt.close();
			
			if(vo.getgNo() == 0) {
				sql = "insert into board values (null, ?, ?, 0, now(), (select ifnull(max(g_no+1), 1) from board as b) , ?, 0, ?, 1);";
			} else {
				sql = "insert into board values (null, ?, ?, 0, now(), ?, ?, ?, ?, 1);";
			}
			
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			if(vo.getgNo() == 0) {
				pstmt.setLong(3, onVal);
				pstmt.setLong(4, vo.getUserNo());
			} else {
				pstmt.setLong(3, vo.getgNo());
				pstmt.setLong(4, onVal);
				pstmt.setLong(5, depVal);
				pstmt.setLong(6, vo.getUserNo());
			}
			
			System.out.println(vo.getgNo()+ ":" + onVal);
			int count = pstmt.executeUpdate();
			result = (count == 1);
			pstmt.close();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
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
		
		return result;		
	}
	
	public List<BoardVo> getSelectedList(String keyword, long currentPage) {
		List<BoardVo> result = new ArrayList<BoardVo>();
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String keys = "%"+keyword+"%";
		
		try {
			connection = getConnection();
			
			String sql = 
				"	select a.no as user_no, a.name, b.no, b.g_no, b.title, b.o_no, b.depth, b.hit, date_format(b.reg_date, '%Y-%m-%d %h:%i:%s'), b.status "  +
				"   from user a, board b" +
				"   where (b.contents like ?"+ 
				"   or b.title like ?)"+
				"	and a.no = b.user_no" +
				"	and (status = ? or status = ?)" +	
				"	order by b.g_no desc, b.o_no asc" + 
				"	limit ?, ?";
			
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, keys);
			pstmt.setString(2, keys);
			pstmt.setLong(3, 0);
			pstmt.setLong(4, 1);
			pstmt.setLong(5, (currentPage-1)*10);
			pstmt.setLong(6, 10);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Long userNo = rs.getLong(1);
				String name = rs.getString(2);
				Long no = rs.getLong(3);				
				Long gNo = rs.getLong(4);
				String title = rs.getString(5);
				Long oNo = rs.getLong(6);				
				Long depth = rs.getLong(7);
				Long hit = rs.getLong(8);
				String regDate = rs.getString(9);
				int status = rs.getInt(10);
				
				BoardVo vo= new BoardVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setUserNo(userNo);
				vo.setgNo(gNo);
				vo.setTitle(title);
				vo.setoNo(oNo);
				vo.setDepth(depth);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setStatus(status);
				
				result.add(vo);
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
		
		return result;
	}	
	
	public List<BoardVo> getList(long currentPage) {
		List<BoardVo> result = new ArrayList<BoardVo>();
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql = 
				"	select a.no as user_no, a.name, b.no, b.g_no, b.title, b.o_no, b.depth, b.hit, date_format(b.reg_date, '%Y-%m-%d %h:%i:%s'), b.status "  +
				"   from user a, board b" +
				"   where a.no = b.user_no "+ 
				"	and (status = ? or status = ?)" +
				"   order by b.g_no desc, b.o_no asc" + 
				"	limit ?, ?";
			
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, 0);
			pstmt.setLong(2, 1);
			pstmt.setLong(3, (currentPage-1)*10);
			pstmt.setLong(4, 10);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Long userNo = rs.getLong(1);
				String name = rs.getString(2);
				Long no = rs.getLong(3);				
				Long gNo = rs.getLong(4);
				String title = rs.getString(5);
				Long oNo = rs.getLong(6);				
				Long depth = rs.getLong(7);
				Long hit = rs.getLong(8);
				String regDate = rs.getString(9);
				int status = rs.getInt(10);
				
				BoardVo vo= new BoardVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setUserNo(userNo);
				vo.setgNo(gNo);
				vo.setTitle(title);
				vo.setoNo(oNo);
				vo.setDepth(depth);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setStatus(status);
				
				result.add(vo);
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

	public BoardVo getContents(Long nos, Long userNo, int flag) {
		// TODO Auto-generated method stub
		BoardVo vo = null;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			String sql = null;
			if(flag == 0) {
				sql =
						"	update board"  +
						"   set hit=hit+1" + 
						"	where no = ?"		; 
				pstmt = connection.prepareStatement(sql);
				pstmt.setLong(1, nos);
				pstmt.executeUpdate();
				pstmt.close();
			}
			
			sql = 
				"	select no, title, contents, g_no, o_no, depth"  +
				"   from board" + 
				"	where no = ?"		;
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, nos);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Long no =  rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				Long gNo =  rs.getLong(4);
				Long oNo =  rs.getLong(5);
				Long depth =  rs.getLong(6);
				
				vo= new BoardVo();
				vo.setNo(no);
				vo.setgNo(nos);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setgNo(gNo);
				vo.setUserNo(userNo);
				vo.setoNo(oNo);
				vo.setDepth(depth);
				
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
		
		return vo;
	}

	public void Modify(BoardVo vo) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql = 
					"	update board"  +
					"   set title=?, contents=?" + 
					"	where no = ?"		; 
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getNo());
			pstmt.executeUpdate();
			
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
	
	public List<BoardVo> parentSearch(BoardVo vo, Connection connection) throws SQLException {
		List<BoardVo> list = new ArrayList<BoardVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		
		Long oNo = vo.getoNo();
		Long no = vo.getNo();
		Long depth = vo.getDepth();
		
		System.out.println(oNo + ":" + depth);

		//자기 자신 포함 후
		BoardVo bvo = new BoardVo();
		bvo.setNo(no);
		bvo.setoNo(oNo);
		list.add(bvo);
		
		//부모 포함시킬 예정
		for(int i = 1; i <= vo.getDepth(); i++) {
			depth = vo.getDepth() - i;
			System.out.println(i + ":" + depth);
			
			sql = 
					"		select no, o_no from board" + 
					"		where g_no = ?" + 
					"		and depth = ?" + 
					"		and o_no < ?" + 
					"		order by o_no desc" + 
					"		limit 1;";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, vo.getgNo());
			pstmt.setLong(2, depth);
			pstmt.setLong(3, oNo);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				no = rs.getLong(1);
				oNo = rs.getLong(2);
				
				bvo = new BoardVo();
				bvo.setNo(no);
				bvo.setoNo(oNo);
				
				list.add(bvo);
			}
		}
		
		return list;
	}

	public void delete(BoardVo vo) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			String sql = null;
			
			Long depth = vo.getDepth();
			Long gNo = vo.getgNo();
			
			//일단 지워놓고 (삭제된 메시지입니다 출력)
			sql = 
					"	update board"  +
					"   set status = ?" + 
					"	where no = ?"; 
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, 0);
			pstmt.setLong(2, vo.getNo());
			pstmt.executeUpdate();
			
			// 부모의 no와 oNo를 가져옴
			List <BoardVo> parentOno = new ArrayList<BoardVo>();
			parentOno = parentSearch(vo, connection);
									
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

	public long getBoardCount() {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long result = 0L;
		
		try {
			connection = getConnection();
			
			String sql = 
				"	select count(*) "  +
				"   from board" +
				"   where status = ?";
				
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, 1);
			
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getLong(1);
			
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
		return result;
	}
}
