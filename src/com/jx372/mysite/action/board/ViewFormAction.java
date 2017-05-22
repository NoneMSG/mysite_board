package com.jx372.mysite.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jx372.mysite.dao.BoardDao;
import com.jx372.mysite.vo.UserVo;
import com.jx372.web.action.Action;
import com.jx372.web.util.WebUtils;

public class ViewFormAction implements Action {

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
		Long bno =  Long.parseLong(request.getParameter("bno"));
		new BoardDao().update( bno );
		request.setAttribute("bno", bno);
		WebUtils.forward("/WEB-INF/views/board/view.jsp", request, response);
	}

}
