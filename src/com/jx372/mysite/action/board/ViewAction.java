package com.jx372.mysite.action.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jx372.mysite.dao.BoardDao;
import com.jx372.mysite.vo.BoardVo;
import com.jx372.mysite.vo.UserVo;
import com.jx372.web.action.Action;
import com.jx372.web.util.WebUtils;

public class ViewAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		if( session == null ) {
			WebUtils.redirect( request.getContextPath() + "/board?a=list", request, response);
			return;
		}
		UserVo authUser = (UserVo)session.getAttribute( "authUser" );
		if( authUser == null ) {
			WebUtils.redirect( request.getContextPath() + "/board?a=list", request, response);
			return;
		}
		BoardVo boardVo = new BoardVo();
		Long boardNo = Long.parseLong(request.getParameter("bno"));
		System.out.println(boardNo);		
		boardVo = new BoardDao().getList(boardNo);
		
		String title = boardVo.getTitle();
		String content = boardVo.getContent();
		
		request.setAttribute("title",title);
		request.setAttribute("content",content);
		request.getRequestDispatcher( "/views/board/view.jsp" ).forward( request, response );
		WebUtils.redirect("/WEB-INF/views/board/view.jsp", request, response);

	}

}
