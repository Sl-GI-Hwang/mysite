package kr.co.itcen.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itcen.mysite.repository.GuestbookDao;
import kr.co.itcen.mysite.vo.GuestbookVo;

@Service
public class GuestbookService {
	@Autowired
	private GuestbookDao guestbookDao;

	public void insert(GuestbookVo vo) {
		// TODO Auto-generated method stub
		guestbookDao.insert(vo);
	}

	public List<GuestbookVo> get() {
		// TODO Auto-generated method stub
		List<GuestbookVo> list = guestbookDao.getList(); 
		return list;
	}

	public void delete(GuestbookVo vo) {
		// TODO Auto-generated method stub
		guestbookDao.delete(vo);
	}
	
}
