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
		
		int currentPage = Integer.parseInt(request.getParameter("p"));
		String keyword = request.getParameter("keyword");
		BoardDao dao = new BoardDao();
		
		int totalCount = dao.getTotalCount( keyword ); 
		int pageCount = (int)Math.ceil( (double)totalCount / LIST_SIZE );
		int blockCount = (int)Math.ceil( (double)pageCount / PAGE_SIZE );
		int currentBlock = (int)Math.ceil( (double)currentPage / PAGE_SIZE );
		
		if( currentPage < 1 ) {
			currentPage = 1;
			currentBlock = 1;
		} else if( currentPage > pageCount ) {
			currentPage = pageCount;
			currentBlock = (int)Math.ceil( (double)currentPage / PAGE_SIZE );
		}
		
		int beginPage = currentBlock == 0 ? 1 : (currentBlock - 1)*PAGE_SIZE + 1;
		int prevPage = ( currentBlock > 1 ) ? ( currentBlock - 1 ) * PAGE_SIZE : 0;
		int nextPage = ( currentBlock < blockCount ) ? currentBlock * PAGE_SIZE + 1 : 0;
		int endPage = ( nextPage > 0 ) ? ( beginPage - 1 ) + LIST_SIZE : pageCount;
		
		List<BoardVo> list = new BoardDao().getList();
		request.setAttribute( "blist", list );
		
		request.setAttribute( "totalCount", totalCount );
		request.setAttribute( "listSize", LIST_SIZE );
		request.setAttribute( "currentPage", currentPage );
		request.setAttribute( "beginPage", beginPage );
		request.setAttribute( "endPage", endPage );
		request.setAttribute( "prevPage", prevPage );
		request.setAttribute( "nextPage", nextPage );
		request.setAttribute( "keyword", keyword );
		
		WebUtils.forward("/WEB-INF/views/board/list.jsp", request, response);
	}

}
