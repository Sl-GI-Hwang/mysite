package kr.co.itcen.mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;

import kr.co.itcen.mysite.exception.UserDaoException;
import kr.co.itcen.mysite.vo.UserVo;

@Repository
public class UserDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public Boolean insert(UserVo vo) throws UserDaoException{
		int count = sqlSession.insert("user.insert", vo);
		Boolean result = (count == 1);
		return result;		
	}
	
	public UserVo get(String email) {
		UserVo result = sqlSession.selectOne("user.getByEmail", email);
		return result; 
	}
	
	public UserVo get(UserVo vo) {
		UserVo result = sqlSession.selectOne("user.getByEmailAndPassword1", vo);
		return result; 
	}
	
	public UserVo get(String email, String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("password", password);
		
		UserVo result = sqlSession.selectOne("user.getByEmailAndPassword2", map);
		return result;		
	}
	
	public Boolean update(UserVo vo) {
		int count = sqlSession.update("user.update", vo);
		return count == 1;		
	}
}