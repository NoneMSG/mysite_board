package com.jx372.mysite.action.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jx372.mysite.dao.BoardDao;
import com.jx372.mysite.vo.BoardVo;
import com.jx372.web.action.Action;
import com.jx372.web.util.WebUtils;

public class ListAction implements Action {
	private static final int LIST_SIZE = 5;
	private static final int PAGE_SIZE = 5;
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		
		
		List<BoardVo> list = new BoardDao().getList();
		request.setAttribute( "blist", list );
		WebUtils.forward("/WEB-INF/views/board/list.jsp", request, response);
	}

}
