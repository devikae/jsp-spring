package com.study.free.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.study.common.util.MybatisSqlSessionFactory;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;
import com.study.free.dao.IFreeBoardDao;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;

public class FreeBoardServiceImpl implements IFreeBoardService{

//	IFreeBoardDao freeBoardDao = new FreeBoardDaoOracle();
	// 이젠 mapper폴더의 .xml을 이용해 쿼리문 실행 위 코드 필요없음 
	
	SqlSessionFactory sqlSessionFactory = MybatisSqlSessionFactory.getSqlSesstionFactory();
	
	@Override
	public List<FreeBoardVO> getBoardList(FreeBoardSearchVO searchVO) {
		// 
		try(SqlSession session = sqlSessionFactory.openSession(true)){
			IFreeBoardDao freeBoardDao = session.getMapper(IFreeBoardDao.class);
			// mapper를 통해서 얻음, 
			//IFreeBoardDao와 freeBoard.xml 연결
			int totalRowCount = freeBoardDao.getTotalRowCount(searchVO);
			searchVO.setTotalRowCount(totalRowCount);
			searchVO.pageSetting();
			
			List<FreeBoardVO> freeBoardList = freeBoardDao.getBoardList(searchVO);
			return freeBoardList;
		}
	}

	@Override
	public FreeBoardVO getBoard(int boNo) throws BizNotFoundException {
		try(SqlSession session = sqlSessionFactory.openSession(true)){
			IFreeBoardDao freeBoardDao = session.getMapper(IFreeBoardDao.class);
		FreeBoardVO freeBoard = freeBoardDao.getBoard(boNo);
		if(freeBoard == null)throw new BizNotFoundException(); 
		return freeBoard;
		}
	}

	@Override
	public void increaseHit(int boNo) throws BizNotEffectedException {
		try(SqlSession session = sqlSessionFactory.openSession(true)){
			IFreeBoardDao freeBoardDao = session.getMapper(IFreeBoardDao.class);
		int cnt = freeBoardDao.increaseHit(boNo);
		if(cnt ==0)throw new BizNotEffectedException();
		}
	}

	@Override
	public void modifyBoard(FreeBoardVO freeBoard)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		// 수정 실패시, 비밀번호가 틀릴시, 해당 글이 존재하지 않을 시, 오류 발생 >> 3개의 에외
		
		// 사용자가 글 등록했을 때의 비밀번호랑 지금 수정하려는 사람이 입력한 비밀번호가 같으면 그 때만 수정 
		
		try(SqlSession session = sqlSessionFactory.openSession(true)){
			IFreeBoardDao freeBoardDao = session.getMapper(IFreeBoardDao.class);

		FreeBoardVO vo = freeBoardDao.getBoard(freeBoard.getBoNo());
		// 글 등록 시 비밀번호를 가지고 있는 객체   (vo)
		// 지금 수정하려는 사람이 입력한 비밀번호를 가지고 있는 객체 > freeBoard (매개변수)
		
		if(vo==null) throw new BizNotFoundException();
		
		if(!vo.getBoPass().equals(freeBoard.getBoPass())) throw new BizPasswordNotMatchedException();
		
		int resultCnt = freeBoardDao.updateBoard(freeBoard);
		
		if(resultCnt == 0) throw new BizNotEffectedException();
		}
	}

	@Override
	public void removeBoard(FreeBoardVO freeBoard)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		// 비밀번호가 같으면 삭제
		
		try(SqlSession session = sqlSessionFactory.openSession(true)){
			IFreeBoardDao freeBoardDao = session.getMapper(IFreeBoardDao.class);
		
		FreeBoardVO vo = freeBoardDao.getBoard(freeBoard.getBoNo());
		
		if(vo==null) throw new BizNotFoundException();
		
		if(!vo.getBoPass().equals(freeBoard.getBoPass())) throw new BizPasswordNotMatchedException();
		
		int resultCnt = freeBoardDao.deleteBoard(freeBoard);
		
		if(resultCnt == 0) throw new BizNotEffectedException();
		}
		
	}

	@Override
	public void registBoard(FreeBoardVO freeBoard) throws BizNotEffectedException {
		try(SqlSession session = sqlSessionFactory.openSession(true)){
			IFreeBoardDao freeBoardDao = session.getMapper(IFreeBoardDao.class);
		int resultCnt = freeBoardDao.insertBoard(freeBoard);
		if(resultCnt == 0) throw new BizNotEffectedException();
		}
		
	}
	// 인터페이스를 상속 받았기 때문에 인터페이스에 있는 메소드를 재정의 해줘야한다.  
	
}
